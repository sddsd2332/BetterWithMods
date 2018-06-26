package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.blocks.*;
import betterwithmods.common.blocks.mechanical.*;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockCauldron;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockCrucible;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockDragonVessel;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockFilteredHopper;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockMillstone;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockPulley;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockTurntable;
import betterwithmods.common.blocks.mechanical.tile.*;
import betterwithmods.common.blocks.tile.*;
import betterwithmods.common.items.*;
import betterwithmods.common.items.tools.ItemSteelSaw;
import betterwithmods.common.registry.KilnStructureManager;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class BWMBlocks {
    public static final Material POTTERY = new Material(MapColor.STONE);
    public static final Block ANCHOR = new BlockAnchor().setRegistryName("anchor");
    public static final Block ROPE = new BlockRope().setRegistryName("rope");
    public static final Block FILTERED_HOPPER = new BlockFilteredHopper().setRegistryName("filtered_hopper");
    public static final Block MILLSTONE = new BlockMillstone().setRegistryName("millstone");
    public static final Block TURNTABLE = new BlockTurntable().setRegistryName("turntable");
    public static final Block PULLEY = new BlockPulley().setRegistryName("pulley");
    public static final Block WOODEN_AXLE = new BlockAxle(EnumTier.WOOD, 1, 1, 3).setRegistryName("wooden_axle");
    public static final Block STEEL_AXLE = new BlockAxle(EnumTier.STEEL, 1, 50, 5).setRegistryName("steel_axle");
    public static final Block WOODEN_GEARBOX = new BlockGearbox(1, EnumTier.WOOD).setRegistryName("wooden_gearbox");
    public static final Block STEEL_GEARBOX = new BlockGearbox(50, EnumTier.STEEL).setRegistryName("steel_gearbox");
    public static final Block WOODEN_BROKEN_GEARBOX = new BlockBrokenGearbox(EnumTier.WOOD).setRegistryName("wooden_broken_gearbox");
    public static final Block STEEL_BROKEN_GEARBOX = new BlockBrokenGearbox(EnumTier.STEEL).setRegistryName("steel_broken_gearbox");
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
    public static final Block VOID_URN = new BlockUrn(BlockUrn.EnumType.VOID).setRegistryName("void_urn");
    public static final Block SOUL_URN = new BlockUrn(BlockUrn.EnumType.SOUL).setRegistryName("soul_urn");
    public static final Block STOKED_FLAME = new BlockFireStoked().setRegistryName("stoked_flame");
    public static final Block HIBACHI = new BlockHibachi().setRegistryName("hibachi");
    public static final Block BELLOWS = new BlockBellows().setTileClass(TileBellows.class).setRegistryName("bellows");
    public static final Block SPRING_ACTION_BELLOWS = new BlockBellows().setTileClass(TileSpringActionBellows.class).setRegistryName("spring_action_bellows");
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
    public static final Block STEEL_SAW = new BlockSteelSaw().setRegistryName("steel_saw");
    public static final Block BLOOD_LOG = new BlockBloodLog().setRegistryName("blood_log");
    public static final Block BLOOD_LEAVES = new BlockBloodLeaves().setRegistryName("blood_leaves");
    public static final Block BLOOD_SAPLING = new BlockBloodSapling().setRegistryName("blood_sapling");
    public static final Block NETHER_CLAY = new BlockNetherClay().setRegistryName("nether_clay");
    public static final Block STEEL_PRESSURE_PLATE = new BlockSteelPressurePlate().setRegistryName("steel_pressure_plate").setCreativeTab(BWCreativeTabs.BWTAB);
    public static final Block INFERNAL_ENCHANTER = new BlockInfernalEnchanter().setRegistryName("infernal_enchanter").setCreativeTab(BWCreativeTabs.BWTAB);
    public static final Block CANDLE_HOLDER = new BlockCandleHolder().setRegistryName("candle_holder").setCreativeTab(BWCreativeTabs.BWTAB);
    public static final Block MERGER = new BlockMerger().setRegistryName("steel_merger");
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
        registerBlocks(BlockCobble.BLOCKS);
        registerBlocks(BlockUnfiredPottery.BLOCKS.values());
        registerBlocks(BlockRawPastry.BLOCKS.values());
        registerBlock(ANCHOR);
        registerBlock(ROPE);
        registerBlock(FILTERED_HOPPER);
        registerBlock(MILLSTONE);
        registerBlock(PULLEY);
        registerBlock(TURNTABLE);
        registerBlock(WOODEN_AXLE);
        registerBlock(STEEL_AXLE);
        registerBlock(WOODEN_GEARBOX);
        registerBlock(STEEL_GEARBOX);
        registerBlock(STEEL_BROKEN_GEARBOX);
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
        registerBlock(VOID_URN);
        registerBlock(STOKED_FLAME, null);
        registerBlock(HIBACHI);
        registerBlock(BELLOWS);
        registerBlock(SPRING_ACTION_BELLOWS);
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
        registerBlock(STEEL_SAW, new ItemSteelSaw(STEEL_SAW));
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
        //TODO resourceloations
        GameRegistry.registerTileEntity(TileMill.class, "bwm.millstone");
        GameRegistry.registerTileEntity(TilePulley.class, "bwm.pulley");
        GameRegistry.registerTileEntity(TileFilteredHopper.class, "bwm.hopper");
        GameRegistry.registerTileEntity(TileCauldron.class, "bwm.cauldron");
        GameRegistry.registerTileEntity(TileCrucible.class, "bwm.crucible");
        GameRegistry.registerTileEntity(TileDragonVessel.class, "bwm.vessel");
        GameRegistry.registerTileEntity(TileTurntable.class, "bwm.turntable");
        GameRegistry.registerTileEntity(TileSteelAnvil.class, "bwm.steelAnvil");
        GameRegistry.registerTileEntity(TileVase.class, "bwm.vase");
        GameRegistry.registerTileEntity(TileWindmillVertical.class, "bwm.vert_windmill");
        GameRegistry.registerTileEntity(TileWindmillHorizontal.class, "bwm.horiz_windmill");
        GameRegistry.registerTileEntity(TileWaterwheel.class, "bwm.waterwheel");
        GameRegistry.registerTileEntity(TileBlockDispenser.class, "bwm.block_dispenser");
        GameRegistry.registerTileEntity(TileCreativeGen.class, "bwm.creative_generator");
        GameRegistry.registerTileEntity(TileGearbox.class, "bwm.gearbox");
        GameRegistry.registerTileEntity(TileBellows.class, "bwm.bellows");
        GameRegistry.registerTileEntity(TileSpringActionBellows.class, "bwm.spring_action_bellows");
        GameRegistry.registerTileEntity(TileBeacon.class, "bwm.beacon");
        GameRegistry.registerTileEntity(TileEnderchest.class, "bwm.enderchest");
        GameRegistry.registerTileEntity(TileAxle.class, "bwm.axle");
        GameRegistry.registerTileEntity(TileSaw.class, "bwm.saw");
        GameRegistry.registerTileEntity(TilePump.class, "bwm.pump");
        GameRegistry.registerTileEntity(TileCrank.class, "bwm.crank");
        GameRegistry.registerTileEntity(TileSteelSaw.class, "bwm.steel_saw");
        GameRegistry.registerTileEntity(TileInfernalEnchanter.class, "bwm.infernal_enchanter");
        GameRegistry.registerTileEntity(TileKiln.class, "bwm.kiln");
        GameRegistry.registerTileEntity(TileMerger.class, "bwm.steel_merger");
        GameRegistry.registerTileEntity(TileFurnace.class, "bwm.furnace");
        GameRegistry.registerTileEntity(TileBucket.class, "bwm.bucket");
        GameRegistry.registerTileEntity(TileBarrel.class, "bwm.barrel");
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
    public static void registerBlock(Block block,
                                     @Nullable
                                             Item item) {
        if (Objects.equals(block.getUnlocalizedName(), "tile.null")) {
            //betterwithmods:name => bwm:name
            block.setUnlocalizedName("bwm" + block.getRegistryName().toString().substring(BWMod.MODID.length()));
        }
        BLOCKS.add(block);
        if (item != null)
            BWMItems.registerItem(item.setRegistryName(block.getRegistryName()));
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

	@SideOnly(Side.CLIENT)
	public static void setInventoryModel(Block block) {
		BWMItems.setInventoryModel(Item.getItemFromBlock(block));
	}



	///CLIENT END
}
