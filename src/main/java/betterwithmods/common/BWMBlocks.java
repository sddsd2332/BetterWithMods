package betterwithmods.common;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.blocks.*;
import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.common.blocks.mechanical.*;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockCauldron;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockCrucible;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockDragonVessel;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockFilteredHopper;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockMillstone;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockPulley;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockTurntable;
import betterwithmods.common.items.itemblocks.*;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.tile.*;
import betterwithmods.lib.ModLib;
import betterwithmods.module.hardcore.beacons.TileBeacon;
import betterwithmods.module.hardcore.beacons.TileEnderchest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class BWMBlocks {
    public static final Material POTTERY = new Material(MapColor.STONE);
    public static final Block ANCHOR = new BlockAnchor().setRegistryName("anchor");
    public static final Block ROPE = new BlockRope().setRegistryName("rope");
    public static final Block FILTERED_HOPPER = new BlockFilteredHopper().setRegistryName("filtered_hopper");
    public static final Block MILLSTONE = new BlockMillstone().setRegistryName("millstone");
    public static final Block TURNTABLE = new BlockTurntable().setRegistryName("turntable");
    public static final Block PULLEY = new BlockPulley().setRegistryName("pulley");
    public static final Block WOODEN_AXLE = new BlockAxle(Material.WOOD, 1, 1, 3).setRegistryName("wooden_axle");
    public static final Block WOODEN_GEARBOX = new BlockGearbox(Material.WOOD, 1).setRegistryName("wooden_gearbox");
    public static final Block WOODEN_BROKEN_GEARBOX = new BlockBrokenGearbox(Material.WOOD).setRegistryName("wooden_broken_gearbox");
    public static final Block HAND_CRANK = new BlockCrank().setRegistryName("hand_crank");
    public static final Block WICKER = new BlockWicker().setRegistryName("wicker");
    public static final Block OAK_GRATE = new BlockPane(Material.WOOD).setRegistryName("oak_grate");
    public static final Block SPRUCE_GRATE = new BlockPane(Material.WOOD).setRegistryName("spruce_grate");
    public static final Block BIRCH_GRATE = new BlockPane(Material.WOOD).setRegistryName("birch_grate");
    public static final Block JUNGLE_GRATE = new BlockPane(Material.WOOD).setRegistryName("jungle_grate");
    public static final Block ACACIA_GRATE = new BlockPane(Material.WOOD).setRegistryName("acacia_grate");
    public static final Block DARK_OAK_GRATE = new BlockPane(Material.WOOD).setRegistryName("dark_oak_grate");
    public static final Block OAK_SLATS = new BlockPane(Material.WOOD).setRegistryName("oak_slats");
    public static final Block SPRUCE_SLATS = new BlockPane(Material.WOOD).setRegistryName("spruce_slats");
    public static final Block BIRCH_SLATS = new BlockPane(Material.WOOD).setRegistryName("birch_slats");
    public static final Block JUNGLE_SLATS = new BlockPane(Material.WOOD).setRegistryName("jungle_slats");
    public static final Block ACACIA_SLATS = new BlockPane(Material.WOOD).setRegistryName("acacia_slats");
    public static final Block DARK_OAK_SLATS = new BlockPane(Material.WOOD).setRegistryName("dark_oak_slats");
    public static final Block URN = new BlockUrn(BlockUrn.EnumType.EMPTY).setRegistryName("urn");
    public static final Block SOUL_URN = new BlockUrn(BlockUrn.EnumType.SOUL).setRegistryName("soul_urn");
    public static final Block STOKED_FLAME = new BlockFireStoked().setRegistryName("stoked_flame");
    public static final Block HIBACHI = new BlockHibachi().setRegistryName("hibachi");
    public static final Block BELLOWS = new BlockBellows().setRegistryName("bellows");
    public static final Block KILN = new BlockKiln(KilnStructureManager::getKilnBlocks).setRegistryName("kiln");
    public static final Block HEMP = new BlockHemp().setRegistryName("hemp");
    public static final Block DETECTOR = new BlockDetector().setRegistryName("detector");
    public static final Block LENS = new BlockLens().setRegistryName("lens");
    public static final Block LIGHT_SOURCE = new BlockInvisibleLight().setRegistryName("invisible_light");
    public static final Block SAW = new BlockSaw().setRegistryName("saw");
    public static final Block BOOSTER = new BlockGearBoostedRail().setRegistryName("booster");
    public static final Block HORIZONTAL_WINDMILL = new BlockWindmill(EnumFacing.Axis.X).setRegistryName("horizontal_windmill");
    public static final Block VERTICAL_WINDMILL = new BlockWindmill(EnumFacing.Axis.Y).setRegistryName("vertical_windmill");
    public static final Block WATERWHEEL = new BlockWaterwheel().setRegistryName("waterwheel");
    public static final Block WOLF = new BlockWolf(new ResourceLocation("minecraft:wolf")).setRegistryName("companion_cube");
    public static final Block BLOCK_DISPENSER = new BlockBDispenser().setRegistryName("block_dispenser");
    public static final Block BUDDY_BLOCK = new BlockBUD().setRegistryName("buddy_block");
    public static final Block CREATIVE_GENERATOR = new BlockCreativeGenerator().setRegistryName("creative_generator");
    public static final Block LIGHT = new BlockLight().setRegistryName("light");
    public static final Block PLATFORM = new BlockPlatform().setRegistryName("platform");
    public static final Block MINING_CHARGE = new BlockMiningCharge().setRegistryName("mining_charge");
    public static final Block FERTILE_FARMLAND = new BlockFertileFarmland().setRegistryName("fertile_farmland");
    public static final Block PUMP = new BlockPump().setRegistryName("screw_pump");
    public static final Block VINE_TRAP = new BlockVineTrap().setRegistryName("vine_trap");
    public static final BlockLiquid TEMP_LIQUID_SOURCE = (BlockLiquid) new BlockTemporaryWater().setRegistryName("temporary_water");
    public static final Block STEEL_ANVIL = new BlockSteelAnvil().setRegistryName("steel_anvil");
    public static final Block DIRT_SLAB = new BlockDirtSlab().setRegistryName("dirt_slab");
    public static final Block CRUCIBLE = new BlockCrucible().setRegistryName("crucible");
    public static final Block CAULDRON = new BlockCauldron().setRegistryName("cauldron");
    public static final Block DRAGON_VESSEL = new BlockDragonVessel().setRegistryName("dragon_vessel");
    public static final Block IRON_WALL = new BlockIronWall().setRegistryName("iron_wall");
    public static final Block STAKE = new BlockStake().setRegistryName("stake");
    public static final Block STAKE_STRING = new BlockStakeString().setRegistryName("stake_string");
    public static final Block NETHER_GROWTH = new BlockNetherGrowth().setRegistryName("nether_growth");
    public static final Block STEEL_BLOCK = new BlockSteel().setRegistryName("steel_block");
    public static final Block BLOOD_LOG = new BlockBloodLog().setRegistryName("blood_log");
    public static final Block BLOOD_LEAVES = new BlockBloodLeaves().setRegistryName("blood_leaves");
    public static final Block BLOOD_SAPLING = new BlockBloodSapling().setRegistryName("blood_sapling");
    public static final Block NETHER_CLAY = new BlockNetherClay().setRegistryName("nether_clay");
    public static final Block STEEL_PRESSURE_PLATE = new BlockSteelPressurePlate().setRegistryName("steel_pressure_plate");
    public static final Block INFERNAL_ENCHANTER = new BlockInfernalEnchanter().setRegistryName("infernal_enchanter");
    public static final Block CANDLE_HOLDER = new BlockCandleHolder().setRegistryName("candle_holder");
    public static final Block SHAFT = new BlockShaft().setRegistryName("shaft");
    public static final Block BUCKET = new BlockBucket().setRegistryName("bucket");
    public static final Block BARREL = new BlockBarrel(Material.WOOD).setRegistryName("barrel");

    private static final List<Block> BLOCKS = new ArrayList<>();

    static {
        BlockCandle.init();
        BlockVase.init();
        BlockPlanter.init();
        BlockAesthetic.init();
        BlockChime.init();
        BlockCobble.init();
        BlockUnfiredPottery.init();
        BlockRawPastry.init();
    }

    public static List<Block> getBlocks() {
        return BLOCKS;
    }

    public static void registerBlocks() {
        registerBlocks(BlockPlanter.BLOCKS.values());
        registerBlocks(BlockCandle.BLOCKS.values());
        registerBlocks(BlockVase.BLOCKS.values());
        registerBlocks(BlockAesthetic.BLOCKS.values());
        registerBlocks(BlockChime.BLOCKS);
        registerBlocks(BlockCobble.BLOCKS.values());
        registerBlocks(BlockUnfiredPottery.BLOCKS.values());
        registerBlocks(BlockRawPastry.BLOCKS.values());
        registerBlock(ANCHOR);
        registerBlock(ROPE);
        registerBlock(FILTERED_HOPPER);
        registerBlock(MILLSTONE);
        registerBlock(PULLEY);
        registerBlock(TURNTABLE);
        registerBlock(WOODEN_AXLE);
        registerBlock(WOODEN_GEARBOX);
        registerBlock(WOODEN_BROKEN_GEARBOX);
        registerBlock(HAND_CRANK);
        registerBlock(WICKER);
        registerBlock(OAK_GRATE);
        registerBlock(SPRUCE_GRATE);
        registerBlock(BIRCH_GRATE);
        registerBlock(JUNGLE_GRATE);
        registerBlock(ACACIA_GRATE);
        registerBlock(DARK_OAK_GRATE);
        registerBlock(OAK_SLATS);
        registerBlock(SPRUCE_SLATS);
        registerBlock(BIRCH_SLATS);
        registerBlock(JUNGLE_SLATS);
        registerBlock(ACACIA_SLATS);
        registerBlock(DARK_OAK_SLATS);
        registerBlock(URN);
        registerBlock(SOUL_URN, new ItemBlockUrn(SOUL_URN));
        registerBlock(STOKED_FLAME, null);
        registerBlock(HIBACHI);
        registerBlock(BELLOWS);
        registerBlock(KILN, null);
        registerBlock(HEMP, new ItemHempSeed(HEMP));
        registerBlock(DETECTOR);
        registerBlock(LENS);
        registerBlock(LIGHT_SOURCE, null);
        registerBlock(SAW);
        registerBlock(BOOSTER);
        registerBlock(HORIZONTAL_WINDMILL, new ItemHorizontalWindmill(HORIZONTAL_WINDMILL));
        registerBlock(VERTICAL_WINDMILL, new ItemVerticalWindmill(VERTICAL_WINDMILL));
        registerBlock(WATERWHEEL, new ItemWaterwheel(WATERWHEEL));
        registerBlock(WOLF);
        registerBlock(BLOCK_DISPENSER);
        registerBlock(BUDDY_BLOCK);
        registerBlock(CREATIVE_GENERATOR);
        registerBlock(LIGHT);
        registerBlock(PLATFORM);
        registerBlock(MINING_CHARGE);
        registerBlock(FERTILE_FARMLAND);
        registerBlock(PUMP);
        registerBlock(VINE_TRAP);
        registerBlock(STEEL_ANVIL);
        registerBlock(CAULDRON);
        registerBlock(CRUCIBLE);
        registerBlock(DRAGON_VESSEL, new ItemBlockLimited(DRAGON_VESSEL, 1));
        registerBlock(TEMP_LIQUID_SOURCE, null);
        registerBlock(IRON_WALL);
        registerBlock(STAKE);
        registerBlock(STAKE_STRING, null);
        registerBlock(NETHER_GROWTH, new ItemBlockSpore(NETHER_GROWTH));
        registerBlock(STEEL_BLOCK);
        registerBlock(BLOOD_LOG);
        registerBlock(BLOOD_LEAVES);
        registerBlock(BLOOD_SAPLING);
        registerBlock(NETHER_CLAY);
        registerBlock(STEEL_PRESSURE_PLATE);
        registerBlock(INFERNAL_ENCHANTER);
        registerBlock(CANDLE_HOLDER);
        registerBlock(SHAFT);
        registerBlock(BUCKET);
        registerBlock(DIRT_SLAB, new ItemSimpleSlab(DIRT_SLAB, Blocks.DIRT));
        registerBlock(BARREL);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileMill.class, new ResourceLocation(ModLib.MODID, "millstone"));
        GameRegistry.registerTileEntity(TilePulley.class, new ResourceLocation(ModLib.MODID, "pulley"));
        GameRegistry.registerTileEntity(TileFilteredHopper.class, new ResourceLocation(ModLib.MODID, "hopper"));
        GameRegistry.registerTileEntity(TileCauldron.class, new ResourceLocation(ModLib.MODID, "cauldron"));
        GameRegistry.registerTileEntity(TileCrucible.class, new ResourceLocation(ModLib.MODID, "crucible"));
        GameRegistry.registerTileEntity(TileDragonVessel.class, new ResourceLocation(ModLib.MODID, "vessel"));
        GameRegistry.registerTileEntity(TileTurntable.class, new ResourceLocation(ModLib.MODID, "turntable"));
        GameRegistry.registerTileEntity(TileSteelAnvil.class, new ResourceLocation(ModLib.MODID, "steel_anvil"));
        GameRegistry.registerTileEntity(TileVase.class, new ResourceLocation(ModLib.MODID, "vase"));
        GameRegistry.registerTileEntity(TileWindmillVertical.class, new ResourceLocation(ModLib.MODID, "vertical_windmill"));
        GameRegistry.registerTileEntity(TileWindmillHorizontal.class, new ResourceLocation(ModLib.MODID, "horizontal_windmill"));
        GameRegistry.registerTileEntity(TileWaterwheel.class, new ResourceLocation(ModLib.MODID, "waterwheel"));
        GameRegistry.registerTileEntity(TileBlockDispenser.class, new ResourceLocation(ModLib.MODID, "block_dispenser"));
        GameRegistry.registerTileEntity(TileCreativeGen.class, new ResourceLocation(ModLib.MODID, "creative_generator"));
        GameRegistry.registerTileEntity(TileGearbox.class, new ResourceLocation(ModLib.MODID, "gearbox"));
        GameRegistry.registerTileEntity(TileBellows.class, new ResourceLocation(ModLib.MODID, "bellows"));
        GameRegistry.registerTileEntity(TileBeacon.class, new ResourceLocation(ModLib.MODID, "beacon"));
        GameRegistry.registerTileEntity(TileEnderchest.class, new ResourceLocation(ModLib.MODID, "enderchest"));
        GameRegistry.registerTileEntity(TileAxle.class, new ResourceLocation(ModLib.MODID, "axle"));
        GameRegistry.registerTileEntity(TileSaw.class, new ResourceLocation(ModLib.MODID, "saw"));
        GameRegistry.registerTileEntity(TilePump.class, new ResourceLocation(ModLib.MODID, "pump"));
        GameRegistry.registerTileEntity(TileCrank.class, new ResourceLocation(ModLib.MODID, "crank"));
        GameRegistry.registerTileEntity(TileInfernalEnchanter.class, new ResourceLocation(ModLib.MODID, "infernal_enchanter"));
        GameRegistry.registerTileEntity(TileKiln.class, new ResourceLocation(ModLib.MODID, "kiln"));
        GameRegistry.registerTileEntity(TileFurnace.class, new ResourceLocation(ModLib.MODID, "furnace"));
        GameRegistry.registerTileEntity(TileBucket.class, new ResourceLocation(ModLib.MODID, "bucket"));
        GameRegistry.registerTileEntity(TileBarrel.class, new ResourceLocation(ModLib.MODID, "barrel"));
    }

    public static void registerBlocks(Collection<? extends Block> blocks) {
        blocks.forEach(BWMBlocks::registerBlock);
    }

    /**
     * Register a block with its specified linked item. Block's registry name
     * prevail and must be set before call.
     *
     * @param block Block instance to register.
     * @param item  Item instance to register. Will have the same registered name
     *              as the block. If null, then no item will be linked to the
     */
    public static void registerBlock(Block block, @Nullable Item item) {
        if (block.getRegistryName() != null) {

            if(block instanceof BlockCamo) {
                block.setCreativeTab(BWCreativeTabs.MINI_BLOCKS);
            } else {
                block.setCreativeTab(BWCreativeTabs.BLOCKS);
            }
            //TODO remove this in 1.13, it is done automatically
            if (block.getTranslationKey().equals("tile.null"))
                block.setTranslationKey(block.getRegistryName().toString());
            BLOCKS.add(block);
            if (item != null) {
                BWMItems.registerItem(item.setRegistryName(block.getRegistryName()));
            }
        }
    }

    /**
     * Register a Block and a new ItemBlock generated from it.
     *
     * @param block Block instance to register.
     * @return Registered block.
     */
    public static Block registerBlock(Block block) {
        registerBlock(block, new ItemBlock(block));
        return block;
    }
}
