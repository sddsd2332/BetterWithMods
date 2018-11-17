package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.EntityHCFishHook;
import betterwithmods.common.registry.crafting.BaitingRecipe;
import betterwithmods.lib.ModLib;
import betterwithmods.lib.TooltipLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.utils.TooltipUtils;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by primetoxinz on 7/23/17.
 */


public class HCFishing extends Feature {
    public static final ResourceLocation HCFISHING_LOOT = LootTableList.register(new ResourceLocation(ModLib.MODID, "gameplay/fishing"));
    private static final ResourceLocation BAITED_FISHING_ROD = new ResourceLocation(ModLib.MODID, "baited_fishing_rod");
    public static boolean requireBait, restrictToOpenWater;
    public static int minimumWaterDepth;
    public static Ingredient BAIT = Ingredient.EMPTY;
    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(FishingBait.class)
    public static Capability<FishingBait> FISHING_ROD_CAP = null;
    public static FishingTimes configuration;

    private static ActionResult<ItemStack> throwLine(Item item, EntityPlayer player, EnumHand hand, World world, Random rand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (player.fishEntity != null) {
            int i = player.fishEntity.handleHookRetraction();
            itemstack.damageItem(i, player);
            player.swingArm(hand);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
        } else {
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

            if (!world.isRemote) {
                EntityHCFishHook entityfishhook = new EntityHCFishHook(world, player);
                int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);

                if (j > 0) {
                    entityfishhook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                if (k > 0) {
                    entityfishhook.setLuck(k);
                }

                world.spawnEntity(entityfishhook);
            }

            player.swingArm(hand);
            player.addStat(StatList.getObjectUseStats(item));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    //Override loottables

    public static boolean isFishingRod(ItemStack stack) {
        return stack.getItem() instanceof ItemFishingRod;
    }

    public static ItemStack getMostRelevantFishingRod(EntityPlayer player) {
        ItemStack itemMain = player.getHeldItemMainhand();
        if (isFishingRod(itemMain)) {
            FishingBait cap = itemMain.getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            if (cap != null && cap.hasBait()) {
                return itemMain;
            }
        } else {
            return player.getHeldItemOffhand();
        }
        return ItemStack.EMPTY;
    }

    public static boolean isBaited(ItemStack stack, boolean baited) {
        if (isFishingRod(stack)) {
            FishingBait cap = stack.getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            return cap != null && cap.hasBait() == baited;
        }
        return false;
    }

    public static ItemStack setBaited(ItemStack rod, boolean baited) {
        FishingBait cap = rod.getCapability(FISHING_ROD_CAP, EnumFacing.UP);
        if (cap != null) {
            cap.setBait(baited);
        }
        if (rod.getTagCompound() == null) {
            rod.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = rod.getTagCompound();
        tag.setBoolean("bait", baited);
        return new ItemStack(rod.serializeNBT());
    }

    public static BlockPos getHookSurfacePos(EntityFishHook hookEntity) {
        World world = hookEntity.getEntityWorld();
        BlockPos hookPos = hookEntity.getPosition();
        int heightOffset = 0;
        while (isWaterBlock(world, hookPos.add(0, heightOffset, 0)) && (hookPos.getY() + heightOffset < 255)) {
            heightOffset++;
        }
        return hookPos.add(0, heightOffset, 0);
    }

    public static boolean isWaterBlock(World world, BlockPos pos) {
        return (world.getBlockState(pos).getBlock() == Blocks.WATER || world.getBlockState(pos).getBlock() == Blocks.FLOWING_WATER);
    }

    public static boolean isAirBlock(World world, BlockPos pos) {
        return (world.getBlockState(pos).getBlock() == Blocks.AIR);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(LootTableList.GAMEPLAY_FISHING)) {
            LootTable table = event.getLootTableManager().getLootTableFromLocation(HCFISHING_LOOT);
            //FIXME this is a shitty hack to stop the overriding loottable from being frozen immediately and stopping other modded events from being able to apply their additions to to the fishing loottable
            ReflectionHelper.setPrivateValue(LootTable.class, table, false, "isFrozen");
            event.setTable(table);
        }
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ItemFishingRod) {
            event.addCapability(BAITED_FISHING_ROD, new FishingBait());
        }
    }

    @SubscribeEvent
    public static void onFished(ItemFishedEvent event) {
        BlockPos hookPos = getHookSurfacePos(event.getHookEntity());
        if (restrictToOpenWater) {
            if (event.getHookEntity().getEntityWorld().getHeight(hookPos.getX(), hookPos.getZ()) > hookPos.getY() || !isAirBlock(event.getHookEntity().getEntityWorld(), hookPos)) {
                event.setCanceled(true);
                event.getEntityPlayer().sendMessage(TooltipUtils.getMessageComponent(ModLib.MODID, TooltipLib.FISHING_NEEDS_OPEN_SKY));
                return;
            }
        }
        if (minimumWaterDepth > 1) {
            for (int i = 1; i <= minimumWaterDepth; i++) {
                if (!isWaterBlock(event.getHookEntity().getEntityWorld(), hookPos.add(0, (i * -1), 0))) {
                    event.setCanceled(true);
                    event.getEntityPlayer().sendMessage(TooltipUtils.getMessageComponent(ModLib.MODID, TooltipLib.FISHING_NEEDS_DEEP_WATER));
                    return;
                }
            }
        }
        if (requireBait) {
            ItemStack stack = getMostRelevantFishingRod(event.getEntityPlayer());
            if (isFishingRod(stack)) {
                FishingBait cap = stack.getCapability(FISHING_ROD_CAP, EnumFacing.UP);

                if (cap != null && cap.hasBait()) {
                    cap.setBait(false);
                    NBTTagCompound tag = stack.getTagCompound();
                    if (tag != null && tag.hasKey("bait")) {
                        tag.setBoolean("bait", false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void useFishingRod(PlayerInteractEvent.RightClickItem event) {
        if (requireBait) {
            if (isFishingRod(event.getItemStack())) {
                FishingBait cap = event.getItemStack().getCapability(FISHING_ROD_CAP, EnumFacing.UP);
                event.setCanceled(true);
                event.setResult(Event.Result.ALLOW);
                if (cap != null) {
                    if (cap.hasBait() || event.getEntityPlayer().isCreative()) {
                        throwLine(event.getItemStack().getItem(), event.getEntityPlayer(), event.getHand(), event.getWorld(), event.getWorld().rand).getType();
                    } else if (!event.getWorld().isRemote && (event.getHand() == EnumHand.MAIN_HAND || event.getHand() == EnumHand.OFF_HAND)) {
                        event.getEntityPlayer().sendMessage(TooltipUtils.getMessageComponent(ModLib.MODID, TooltipLib.FISHING_NEEDS_BAIT));
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (requireBait) {
            ItemStack stack = event.getItemStack();
            if (isFishingRod(stack)) {
                FishingBait cap = event.getItemStack().getCapability(FISHING_ROD_CAP, EnumFacing.UP);
                if (cap != null) {
                    boolean bait = cap.hasBait();
                    String tooltip = bait ? TooltipLib.FISHING_ROD_BAITED : TooltipLib.FISHING_ROD_UNBAITED;
                    if (!bait) {
                        //Used to sync this on the client.
                        NBTTagCompound tag = stack.getTagCompound();
                        if (tag != null && tag.hasKey("bait")) {
                            tooltip = tag.getBoolean("bait") ? TooltipLib.FISHING_ROD_BAITED : TooltipLib.FISHING_ROD_UNBAITED;
                        }
                    }
                    event.getToolTip().add(TooltipUtils.getTooltip(ModLib.MODID, tooltip));
                }
            }
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        requireBait = loadProperty("Require Bait", true).setComment("Change Fishing Rods to require being Baited with certain items to entice fish, they won't nibble without it!").get();
        restrictToOpenWater = loadProperty("Restrict to Open Water", true).setComment("Fishing on underground locations won't work, hook must be placed on a water block with line of sight to the sky.").get();
        minimumWaterDepth = loadProperty("Minimum Water Depth", 3).setComment("If higher than 1, requires bodies of water to have a minimum depth for fishing to be successful.").get();

        configuration = new FishingTimes();

        CapabilityManager.INSTANCE.register(FishingBait.class, new CapabilityFishingRod(), FishingBait::new);
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:fishing_rod"));

    }

    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        BAIT = StackIngredient.fromStacks(config().loadItemStackArray("Bait", getCategory(), "Add items as valid fishing bait", new ItemStack[]{
                new ItemStack(Items.SPIDER_EYE),
                new ItemStack(BWMItems.CREEPER_OYSTER),
                new ItemStack(Items.FISH, 1, 2),
                new ItemStack(Items.FISH, 1, 3),
                new ItemStack(BWMItems.BAT_WING, 1),
                new ItemStack(BWMItems.COOKED_BAT_WING, 1),
                new ItemStack(Items.ROTTEN_FLESH)
        }));
        event.getRegistry().register(new BaitingRecipe());
    }

    @Override
    public String getDescription() {
        return "Change Fishing Rods to require bait and a large enough water source exposed to the sky.";
    }

    public static class CapabilityFishingRod implements Capability.IStorage<FishingBait> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<FishingBait> capability, FishingBait instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<FishingBait> capability, FishingBait instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }

    public static class FishingBait implements ICapabilitySerializable<NBTTagCompound> {
        private boolean bait;

        public FishingBait() {
        }

        public boolean hasBait() {
            return bait;
        }

        public void setBait(boolean bait) {
            this.bait = bait;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == FISHING_ROD_CAP;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == FISHING_ROD_CAP)
                return FISHING_ROD_CAP.cast(this);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("bait", hasBait());
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            setBait(nbt.getBoolean("bait"));
        }
    }

    public class FishingTimes {
        public double initialTime;
        public double nightModifier, rainModifier, fullMoonModifier, dawnModifier, duskModifier;

        FishingTimes() {
            initialTime = loadProperty("Base Time", 1).setComment("Starting time for which fishing events are calculated in minutes").get();
            nightModifier = loadProperty("Night Modifier", 2).setComment("Scale the baseTime by this when it is night time").get();
            rainModifier = loadProperty("Rain Modifier", 0.75).setComment("Scale the baseTime by this when it is raining").get();
            fullMoonModifier = loadProperty("Full Moon Modifier", 0.5).setComment("Scale the baseTime by this when it is a full moon").get();
            dawnModifier = loadProperty("Dawn Modifier", 0.5).setComment("Scale the baseTime by this when it is dawn").get();
            duskModifier = loadProperty("Dusk Modifier", 0.5).setComment("Scale the baseTime by this when it is dusk").get();
        }
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
