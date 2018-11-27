package betterwithmods.module.compat.patchouli;

import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ingredient.NBTIngredient;
import betterwithmods.module.internal.player.PlayerInfo;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.IItemHandler;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.UUID;

public class Patchouli extends Feature {

    private static boolean giveJournal;

    public Patchouli() {
    }

    @Override
    public String getDescription() {
        return "Patchouli API registration";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }


    private static IMultiblock kilnTube, kilnCorner;

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        giveJournal = loadProperty("Give Journal", true).setComment("give the player the journal on their first respawn and on each new HCSpawn.").get();
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        PatchouliAPI.IPatchouliAPI api = PatchouliAPI.instance;


        Object[] kilnMatchers = new Object[]{
                'E', api.airMatcher(),
                ' ', api.anyMatcher(),
                'B', api.predicateMatcher(Blocks.BRICK_BLOCK, KilnStructureManager::isKilnBlock)
        };

        kilnTube = api.registerMultiblock(
                new ResourceLocation(ModLib.MODID, "kiln_tube"),
                api.makeMultiblock(new String[][]{
                        {"   ", " B ", "   "},
                        {"   ", "BEB", "   "},
                        {"   ", " B ", "   "},
                        {"   ", " 0 ", "   "},
                }, kilnMatchers))
                .setSymmetrical(true);

        kilnCorner = api.registerMultiblock(
                new ResourceLocation(ModLib.MODID, "kiln_corner"),
                api.makeMultiblock(new String[][]{
                        {"   ", " B ", "   "},
                        {"   ", "BE ", " B "},
                        {"   ", " B ", "   "},
                        {"   ", " 0 ", "   "}
                }, kilnMatchers))
                .setSymmetrical(true);

    }

    private static boolean isJournal(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().getRegistryName().equals(new ResourceLocation("patchouli:guide_book"))) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                String book = tag.getString("patchouli:book");
                return book.equals("betterwithmods:journal");
            }
        }
        return false;
    }

    private static UUID getOwner(ItemStack stack) {
        if (isJournal(stack)) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                return UUID.fromString(tag.getString("betterwithmods:owner"));
            }
        }
        return null;
    }

    @GameRegistry.ObjectHolder("patchouli:guide_book")
    public static final Item GUIDE_BOOK = null;

    @GameRegistry.ObjectHolder("patchouli:book_flip")
    public static final SoundEvent BOOK_FLIP = null;

    public static ItemStack getJournal(EntityPlayer player) {
        if (GUIDE_BOOK == null)
            return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(GUIDE_BOOK);
        NBTTagCompound tag = new NBTTagCompound();
        String uuid = player.getUniqueID().toString();
        tag.setString("betterwithmods:owner", uuid);
        tag.setString("patchouli:book", "betterwithmods:journal");
        stack.setTagCompound(tag);
        return stack;
    }

    public static void giveJournal(EntityPlayer player) {
        if (!giveJournal)
            return;

        ItemStack journal = Patchouli.getJournal(player);
        if (journal.isEmpty())
            return;
        IItemHandler inv = CapabilityUtils.getFullPlayerInventory(player);
        if (!InventoryUtils.containsIngredient(new NBTIngredient(journal), inv)) {
            InventoryUtils.insert(inv, journal, false);
        }
    }

    @SubscribeEvent
    public void onItemClicked(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (isJournal(stack)) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && !tag.hasKey("betterwithmods:owner")) {
                tag.setString("betterwithmods:owner", event.getEntityPlayer().getUniqueID().toString());
            }
        }
    }

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Ingredient journal = new NBTIngredient(getJournal(player));


        EntityItem entity = event.getItem();
        if (journal.apply(entity.getItem()) && InventoryUtils.containsIngredient(journal, CapabilityUtils.getFullPlayerInventory(player))) {
            if (BOOK_FLIP != null) {
                player.getEntityWorld().playSound(null, entity.getPosition(), BOOK_FLIP, SoundCategory.PLAYERS, 1, 1);
            }
            entity.getEntityWorld().playEvent(2005, entity.getPosition(), 0);

            entity.getItem().shrink(1);
        }
    }


    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            PlayerInfo info = PlayerInfo.getPlayerInfo(player);
            if (!info.givenManual) {
                giveJournal(player);
            }
        }
    }
}

