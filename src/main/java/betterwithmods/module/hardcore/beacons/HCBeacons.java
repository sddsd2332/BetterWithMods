package betterwithmods.module.hardcore.beacons;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockBeacon;
import betterwithmods.common.blocks.BlockEnderchest;
import betterwithmods.common.blocks.BlockSteel;
import betterwithmods.common.blocks.tile.TileEnderchest;
import betterwithmods.common.items.tools.ItemSoulforgeArmor;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static betterwithmods.module.hardcore.beacons.EnderchestCap.ENDERCHEST_CAPABILITY;


/**
 * Created by primetoxinz on 7/17/17.
 */
@GameRegistry.ObjectHolder("minecraft")
public class HCBeacons extends Feature {

    private static final List<BeaconEffect> BEACON_EFFECTS = Lists.newArrayList();

    private static final Block ENDERCHEST = new BlockEnderchest().setRegistryName("minecraft:ender_chest");
    private static final Block BEACON = new BlockBeacon().setRegistryName("minecraft:beacon");

    private static boolean enderchestBeacon;
    private static boolean extendedConfig;


    @Override
    public void setupConfig() {
        enderchestBeacon = loadPropBool("Enderchest Beacon", "Rework how Enderchests work. Enderchests on their own work like normal chests. When placed on a beacon made of Ender Block the chest functions depending on level, more info in the Book of Single.", true);

    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMBlocks.registerBlock(BEACON);
        if (enderchestBeacon) {
            BWMBlocks.registerBlock(ENDERCHEST);
            CapabilityManager.INSTANCE.register(EnderchestCap.class, new EnderchestCap.Storage(), EnderchestCap::new);
        }
        CapabilityManager.INSTANCE.register(CapabilityBeacon.class, new CapabilityBeacon.Storage(), CapabilityBeacon::new);
    }

    @Override
    public void init(FMLInitializationEvent event) {

//        Items.COMPASS.addPropertyOverride(new ResourceLocation("angle"), new CompassProperty());

        /*
        BEACON_EFFECTS.put(Blocks.GLASS.getDefaultState(), (world, pos, level) -> {
        });
        BEACON_EFFECTS.put(Blocks.IRON_BLOCK.getDefaultState(), (world, pos, level) -> {
            //TODO substitute ItemCompass.
        });
        */

        BEACON_EFFECTS.add(new PotionBeaconEffect(new BlockIngredient("blockEmerald"), EntityLivingBase.class).addPotionEffect(BWRegistry.POTION_LOOTING, 125, PotionBeaconEffect.Amplification.LEVEL).setBaseBeamColor(Color.GREEN));
        BEACON_EFFECTS.add(new PotionBeaconEffect(new BlockIngredient("blockLapis"), EntityLivingBase.class).addPotionEffect(BWRegistry.POTION_TRUESIGHT, 125, PotionBeaconEffect.Amplification.NONE).setBaseBeamColor(Color.BLUE));
        BEACON_EFFECTS.add(new PotionBeaconEffect(new BlockIngredient("blockDiamond"), EntityLivingBase.class).addPotionEffect(BWRegistry.POTION_FORTUNE, 125, PotionBeaconEffect.Amplification.LEVEL_REDUCED).setBaseBeamColor(Color.CYAN));
        BEACON_EFFECTS.add(new PotionBeaconEffect(new BlockIngredient("glowstone"), EntityLivingBase.class).addPotionEffect(MobEffects.NIGHT_VISION, 400, PotionBeaconEffect.Amplification.LEVEL_REDUCED).setBaseBeamColor(Color.YELLOW));
        BEACON_EFFECTS.add(new PotionBeaconEffect(new BlockIngredient("blockGold"), EntityLivingBase.class).addPotionEffect(MobEffects.HASTE, 120, PotionBeaconEffect.Amplification.LEVEL_REDUCED).setBaseBeamColor(Color.YELLOW));

        /*
        BEACON_EFFECTS.put(Blocks.SLIME_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 120, level))));
        BEACON_EFFECTS.put(BlockAesthetic.getVariant(BlockAesthetic.EnumType.DUNG), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> {
                    if (!PlayerHelper.hasFullSet((EntityPlayer) player, ItemSoulforgeArmor.class)) {
                        player.addPotionEffect(new PotionEffect(MobEffects.POISON, 120, level));
                        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120, level));
                    }
                }
        ));
        BEACON_EFFECTS.put(Blocks.COAL_BLOCK.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> {
                    if (!PlayerHelper.hasPart(player, EntityEquipmentSlot.HEAD, ItemSoulforgeArmor.class)) {
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 120, level));
                    }
                }
        ));
        BEACON_EFFECTS.put(BlockAesthetic.getVariant(BlockAesthetic.EnumType.HELLFIRE), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 120, level))));
        BEACON_EFFECTS.put(Blocks.PRISMARINE.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 120, level))));
        BEACON_EFFECTS.put(Blocks.SPONGE.getDefaultState(), (world, pos, level) -> IBeaconEffect.forEachEntityAround(EntityLivingBase.class, world, pos, level, entity -> {
            if (!(entity instanceof EntityWaterMob) && !(entity.isEntityUndead() && !PlayerHelper.hasPart(entity, EntityEquipmentSlot.HEAD, ItemSoulforgeArmor.class))) {
                entity.setAir(entity.getAir() - 1);
            }
        }));
        BEACON_EFFECTS.put(BWMBlocks.STEEL_BLOCK.getDefaultState().withProperty(BlockSteel.HEIGHT, 0), new SpawnBeaconEffect());

        BEACON_EFFECTS.put(BlockAesthetic.getVariant(BlockAesthetic.EnumType.PADDING), (world, pos, level) -> IBeaconEffect.forEachPlayersAround(world, pos, level, player -> {
            player.addPotionEffect(new PotionEffect(BWRegistry.POTION_SLOWFALL, 120, level));
        }));
        if (enderchestBeacon) {
            BEACON_EFFECTS.put(BlockAesthetic.getVariant(BlockAesthetic.EnumType.ENDERBLOCK), new EnderBeaconEffect());
        }
        */

    }


    public static BeaconEffect getEffect(World world, BlockPos pos, IBlockState blockState) {
        for(BeaconEffect beaconEffect : BEACON_EFFECTS) {
            if(beaconEffect.isBlockStateValid(world, pos, blockState)) {
                return beaconEffect;
            }
        }

        return null;
    }

    public static boolean isValidBeaconBase(IBlockState blockState) {
        return getEffect(null, null, blockState) != null;
    }


    @Override
    public String getFeatureDescription() {
        return "Overhauls the function of Beacons. Beacons have extended range, no longer have a GUI, and require the same material throughout the pyramid. The pyramid material determines the beacon effect, and additional tiers increase the range and strength of the effects. Some beacon types may also cause side effects to occur while a beacon is active.";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    public static ResourceLocation WORLD1 = new ResourceLocation(BWMod.MODID, "world_enderchest");
    public static ResourceLocation WORLD2 = new ResourceLocation(BWMod.MODID, "world2_enderchest");
    public static ResourceLocation GLOBAL = new ResourceLocation(BWMod.MODID, "global_enderchest");


    @SubscribeEvent
    public void attachTileCapability(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof TileEnderchest && !event.getObject().hasCapability(ENDERCHEST_CAPABILITY, EnumFacing.UP)) {
            event.addCapability(new ResourceLocation(BWMod.MODID, "enderchest"), new EnderchestCap(EnumFacing.UP));
        }
    }

    @SubscribeEvent
    public void attachWorldCapability(AttachCapabilitiesEvent<World> event) {
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
}
