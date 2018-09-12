package betterwithmods.module.hardcore.beacons;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRegistry;
import betterwithmods.common.blocks.BlockBeacon;
import betterwithmods.common.items.tools.ItemSoulforgeArmor;
import betterwithmods.common.registry.block.recipe.BlockDropIngredient;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.block.recipe.IngredientSpecial;
import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

import static betterwithmods.module.hardcore.beacons.EnderchestCap.ENDERCHEST_CAPABILITY;


/**
 * Created by primetoxinz on 7/17/17.
 */
@Mod.EventBusSubscriber
public class HCBeacons extends Feature {


    public static final List<BeaconEffect> BEACON_EFFECTS = Lists.newArrayList();
    public static ResourceLocation WORLD1 = new ResourceLocation(BWMod.MODID, "world_enderchest");
    public static ResourceLocation WORLD2 = new ResourceLocation(BWMod.MODID, "world2_enderchest");
    public static ResourceLocation GLOBAL = new ResourceLocation(BWMod.MODID, "global_enderchest");
    private static boolean enderchestBeacon;
    private static boolean enableBeaconCustomization;

    public static BeaconEffect getEffect(World world, BlockPos pos, IBlockState blockState) {
        for (BeaconEffect beaconEffect : BEACON_EFFECTS) {
            if (beaconEffect.isBlockStateValid(world, pos, blockState)) {
                return beaconEffect;
            }
        }

        return null;
    }

    public static boolean isValidBeaconBase(IBlockState blockState) {
        return getEffect(null, null, blockState) != null;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        enableBeaconCustomization = loadProperty("Enable Beacon Customization", true)
                .setComment("Allows you to customize parts of beacons, and disable specific ones. Requires restart to generate additional configs").get();
        enderchestBeacon = loadProperty("Enderchest Beacon", true)
                .setComment("Rework how Enderchests work. Enderchests on their own work like normal chests. When placed on a beacon made of Ender Block the chest functions depending on level, more info in the Manual.").get();

        BWMBlocks.registerBlock(new BlockBeacon().setRegistryName("minecraft:beacon"));
        if (enderchestBeacon) {
            BWMBlocks.registerBlock(new BlockEnderchest().setRegistryName("minecraft:ender_chest"));
            CapabilityManager.INSTANCE.register(EnderchestCap.class, new EnderchestCap.Storage(), EnderchestCap::new);
        }
        CapabilityManager.INSTANCE.register(CapabilityBeacon.class, new CapabilityBeacon.Storage(), CapabilityBeacon::new);
    }

    @SubscribeEvent
    public static void attachTileCapability(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof TileEnderchest && !event.getObject().hasCapability(ENDERCHEST_CAPABILITY, EnumFacing.UP)) {
            event.addCapability(new ResourceLocation(BWMod.MODID, "enderchest"), new EnderchestCap(EnumFacing.UP));
        }
    }

    @Override
    public String getDescription() {
        return "Overhauls the function of Beacons. Beacons have extended range, no longer have a GUI, and require the same material throughout the pyramid. The pyramid material determines the beacon effect, and additional tiers increase the range and strength of the effects. Some beacon types may also cause side effects to occur while a beacon is active.";
    }

    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();

        //Capability for tracking beacon ranges
        if (!world.hasCapability(CapabilityBeacon.BEACON_CAPABILITY, EnumFacing.UP)) {
            event.addCapability(new ResourceLocation(BWMod.MODID, "beacons"), new CapabilityBeacon());
        }
        if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
            if (!world.hasCapability(ENDERCHEST_CAPABILITY, EnumFacing.DOWN)) {
                event.addCapability(GLOBAL, new EnderchestCap(EnumFacing.DOWN));
            }
        }
        if (!world.hasCapability(ENDERCHEST_CAPABILITY, EnumFacing.SOUTH)) {
            event.addCapability(WORLD1, new EnderchestCap(EnumFacing.SOUTH));
        }
        if (!world.hasCapability(ENDERCHEST_CAPABILITY, EnumFacing.NORTH)) {
            event.addCapability(WORLD2, new EnderchestCap(EnumFacing.NORTH));
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        BEACON_EFFECTS.add(new CosmeticBeaconEffect("glass", new BlockDropIngredient("blockGlass")));
        BEACON_EFFECTS.add(new CosmeticBeaconEffect("wool", new BlockDropIngredient("wool")));
        BEACON_EFFECTS.add(new CosmeticBeaconEffect("terracotta", new BlockDropIngredient("terracotta")));
        BEACON_EFFECTS.add(new CosmeticBeaconEffect("concrete", new BlockDropIngredient("concrete")));

        BEACON_EFFECTS.add(new PotionBeaconEffect("iron", new BlockIngredient("blockIron"), EntityMob.class)
                .addPotionEffect(MobEffects.GLOWING, 25000, PotionBeaconEffect.Amplification.ZERO)
                .setBaseBeamColor(Color.WHITE)
                .setTickRate(3600));

        BEACON_EFFECTS.add(new PotionBeaconEffect("emerald", new BlockIngredient("blockEmerald"), EntityLivingBase.class)
                .addPotionEffect(BWMRegistry.POTION_LOOTING, 125, PotionBeaconEffect.Amplification.LEVEL)
                .setBaseBeamColor(Color.GREEN));

        BEACON_EFFECTS.add(new PotionBeaconEffect("lapis", new BlockIngredient("blockLapis"), EntityPlayer.class)
                .addPotionEffect(BWMRegistry.POTION_TRUESIGHT, 125, PotionBeaconEffect.Amplification.NONE)
                .setBaseBeamColor(Color.BLUE));

        BEACON_EFFECTS.add(new PotionBeaconEffect("diamond", new BlockIngredient("blockDiamond"), EntityPlayer.class)
                .addPotionEffect(BWMRegistry.POTION_FORTUNE, 125, PotionBeaconEffect.Amplification.LEVEL_REDUCED)
                .setBaseBeamColor(Color.CYAN));

        BEACON_EFFECTS.add(new PotionBeaconEffect("glowstone", new BlockIngredient("glowstone"), EntityPlayer.class)
                .addPotionEffect(MobEffects.NIGHT_VISION, 400, PotionBeaconEffect.Amplification.LEVEL_REDUCED)
                .setBaseBeamColor(Color.YELLOW));

        BEACON_EFFECTS.add(new PotionBeaconEffect("gold", new BlockIngredient("blockGold"), EntityPlayer.class)
                .addPotionEffect(MobEffects.HASTE, 120, PotionBeaconEffect.Amplification.LEVEL_REDUCED)
                .setBaseBeamColor(Color.YELLOW));

        BEACON_EFFECTS.add(new PotionBeaconEffect("slime", new BlockIngredient("blockSlime"), EntityPlayer.class)
                .addPotionEffect(MobEffects.JUMP_BOOST, 120, PotionBeaconEffect.Amplification.LEVEL)
                .setBaseBeamColor(Color.GREEN));

        BEACON_EFFECTS.add(new PotionBeaconEffect("dung", new BlockIngredient("blockDung"), EntityPlayer.class)
                .setCanApply((entityPlayer) -> !PlayerHelper.hasFullSet(((EntityPlayer) entityPlayer), ItemSoulforgeArmor.class))
                .addPotionEffect(MobEffects.POISON, 120, PotionBeaconEffect.Amplification.LEVEL)
                .addPotionEffect(MobEffects.NAUSEA, 120, PotionBeaconEffect.Amplification.LEVEL)
                .setBaseBeamColor(Color.BLACK));

        BEACON_EFFECTS.add(new PotionBeaconEffect("coal", new BlockIngredient("blockCoal"), EntityPlayer.class)
                .setCanApply((entityPlayer) -> !PlayerHelper.hasPart(entityPlayer, EntityEquipmentSlot.HEAD, ItemSoulforgeArmor.class))
                .addPotionEffect(MobEffects.BLINDNESS, 120, PotionBeaconEffect.Amplification.LEVEL)
                .setBaseBeamColor(Color.BLACK));

        BEACON_EFFECTS.add(new HellfireBeaconEffect());

        BEACON_EFFECTS.add(new PotionBeaconEffect("prismarine", new BlockIngredient("blockPrismarine"), EntityPlayer.class)
                .addPotionEffect(MobEffects.WATER_BREATHING, 120, PotionBeaconEffect.Amplification.LEVEL)
                .setBaseBeamColor(Color.BLUE));

        BEACON_EFFECTS.add(new PotionBeaconEffect("padding", new BlockIngredient("blockPadding"), EntityPlayer.class)
                .addPotionEffect(BWMRegistry.POTION_SLOWFALL, 120, PotionBeaconEffect.Amplification.LEVEL)
                .setBaseBeamColor(Color.PINK));

        BEACON_EFFECTS.add(new SpawnBeaconEffect());

        if (enderchestBeacon) {
            BEACON_EFFECTS.add(new EnderBeaconEffect());
        }

        BEACON_EFFECTS.add(new CosmeticBeaconEffect("compatibility", new BlockIngredient(new IngredientSpecial(
                stack -> {
                    try {
                        return stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock().isBeaconBase(null, null, null);
                    } catch (NullPointerException e) {
                        return false;
                    }
                }))));

        if (enableBeaconCustomization) {
            for (BeaconEffect beaconEffect : BEACON_EFFECTS) {
                beaconEffect.setupConfig(this);
            }
        }
    }
}
