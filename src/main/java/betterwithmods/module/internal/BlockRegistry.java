package betterwithmods.module.internal;

import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.BWMItems;
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
import betterwithmods.library.common.block.BlockEntry;
import betterwithmods.library.common.block.BlockEntryBuilder;
import betterwithmods.library.modularity.impl.RequiredFeature;
import betterwithmods.module.hardcore.beacons.TileBeacon;
import betterwithmods.module.hardcore.beacons.TileEnderchest;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class BlockRegistry extends RequiredFeature {

    private static List<Block> REGISTRY = Lists.newArrayList();

    public static void registerTileEntities() {
        //TODO
        GameRegistry.registerTileEntity(TileVase.class, new ResourceLocation(ModLib.MODID, "vase"));
        GameRegistry.registerTileEntity(TileBeacon.class, new ResourceLocation(ModLib.MODID, "beacon"));
        GameRegistry.registerTileEntity(TileEnderchest.class, new ResourceLocation(ModLib.MODID, "enderchest"));
        GameRegistry.registerTileEntity(TileFurnace.class, new ResourceLocation(ModLib.MODID, "furnace"));
    }

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

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        registerBlocks(
                BlockEntryBuilder.create().block(new BlockMillstone()).id("millstone").tile(TileMill.class).build(),
                BlockEntryBuilder.create().block(new BlockAnchor()).id("anchor").build(),
                BlockEntryBuilder.create().block(new BlockRope()).id("rope").build(),
                BlockEntryBuilder.create().block(new BlockFilteredHopper()).id("filtered_hopper").tile(TileFilteredHopper.class).build(),
                BlockEntryBuilder.create().block(new BlockPulley()).id("pulley").tile(TilePulley.class).build(),
                BlockEntryBuilder.create().block(new BlockTurntable()).id("turntable").tile(TileTurntable.class).build(),

                BlockEntryBuilder.create().block(new BlockAxle(Material.WOOD, 1, 1, 3)).id("wooden_axle").tile(TileAxle.class).build(),
                BlockEntryBuilder.create().block(new BlockGearbox(Material.WOOD, 1)).id("wooden_gearbox").tile(TileGearbox.class).build(),
                BlockEntryBuilder.create().block(new BlockBrokenGearbox(Material.WOOD)).id("wooden_broken_gearbox").build(),
                BlockEntryBuilder.create().block(new BlockHandCrank()).id("hand_crank").tile(TileHandCrank.class).build(),
                BlockEntryBuilder.create().block(new BlockWicker()).id("wicker").build(),
                BlockEntryBuilder.create().block(new BlockUrn(BlockUrn.EnumType.EMPTY)).id("urn").build(),
                BlockEntryBuilder.create().block(new BlockUrn(BlockUrn.EnumType.SOUL)).id("soul_urn").itemblock(ItemBlockUrn::new).build(),

                BlockEntryBuilder.create().block(new BlockFireStoked()).id("stoked_flame").noItem().build(),
                BlockEntryBuilder.create().block(new BlockHibachi()).id("hibachi").build(),
                BlockEntryBuilder.create().block(new BlockBellows()).id("bellows").tile(TileBellows.class).build(),

                BlockEntryBuilder.create().block(new BlockKiln(KilnStructureManager::getKilnBlocks)).id("kiln").noItem().tile(TileKiln.class).build(),
                BlockEntryBuilder.create().block(new BlockHemp()).id("hemp").itemblock(ItemHempSeed::new).build(),
                BlockEntryBuilder.create().block(new BlockDetector()).id("detector").build(),
                BlockEntryBuilder.create().block(new BlockLens()).id("lens").build(),
                BlockEntryBuilder.create().block(new BlockInvisibleLight()).id("invisible_light").noItem().build(),
                BlockEntryBuilder.create().block(new BlockSaw()).id("saw").tile(TileSaw.class).build(),
//                BlockEntryBuilder.create().block(new BlockGearBoostedRail()).id("booster").build(),

                BlockEntryBuilder.create().block(new BlockWindmill(EnumFacing.Axis.X)).id("horizontal_windmill").itemblock(ItemHorizontalWindmill::new).tile(TileWindmillHorizontal.class).build(),
                BlockEntryBuilder.create().block(new BlockWindmill(EnumFacing.Axis.Y)).id("vertical_windmill").itemblock(ItemHorizontalWindmill::new).tile(TileWindmillVertical.class).build(),
                BlockEntryBuilder.create().block(new BlockWaterwheel()).id("waterwheel").itemblock(ItemWaterwheel::new).tile(TileWaterwheel.class).build(),
                BlockEntryBuilder.create().block(new BlockWolf(new ResourceLocation("minecraft:wolf"))).id("companion_cube").build(),
                BlockEntryBuilder.create().block(new BlockBDispenser()).id("block_dispenser").tile(TileBlockDispenser.class).build(),
                BlockEntryBuilder.create().block(new BlockBUD()).id("buddy_block").build(),

                BlockEntryBuilder.create().block(new BlockCreativeGenerator()).id("creative_generator").tile(TileCreativeGenerator.class).build(),
                BlockEntryBuilder.create().block(new BlockLight()).id("light").build(),
                BlockEntryBuilder.create().block(new BlockPlatform()).id("platform").build(),
                BlockEntryBuilder.create().block(new BlockMiningCharge()).id("mining_charge").build(),
                BlockEntryBuilder.create().block(new BlockFertileFarmland()).id("fertile_farmland").build(),
                BlockEntryBuilder.create().block(new BlockScrewPump()).id("screw_pump").tile(TileScrewPump.class).build(),
                BlockEntryBuilder.create().block(new BlockVineTrap()).id("vine_trap").build(),
                BlockEntryBuilder.create().block(new BlockSteelAnvil()).id("steel_anvil").tile(TileSteelAnvil.class).build(),
                BlockEntryBuilder.create().block(new BlockCauldron()).id("cauldron").tile(TileCauldron.class).build(),
                BlockEntryBuilder.create().block(new BlockCrucible()).id("crucible").tile(TileCrucible.class).build(),
                BlockEntryBuilder.create().block(new BlockDragonVessel()).id("dragon_vessel").itemblock(ItemBlockLimited::new).tile(TileDragonVessel.class).build(),

                BlockEntryBuilder.create().block(new BlockTemporaryWater()).id("temporary_water").noItem().build(),
                BlockEntryBuilder.create().block(new BlockIronWall()).id("iron_wall").build(),
                BlockEntryBuilder.create().block(new BlockStake()).id("stake").build(),
                BlockEntryBuilder.create().block(new BlockStakeString()).id("stake_string").build(),
                BlockEntryBuilder.create().block(new BlockNetherGrowth()).id("nether_growth").itemblock(ItemBlockSpore::new).build(),
                BlockEntryBuilder.create().block(new BlockSteel()).id("steel_block").build(),
                BlockEntryBuilder.create().block(new BlockBloodLog()).id("blood_log").build(),
                BlockEntryBuilder.create().block(new BlockBloodLeaves()).id("blood_leaves").build(),
                BlockEntryBuilder.create().block(new BlockBloodSapling()).id("blood_sapling").build(),
                BlockEntryBuilder.create().block(new BlockNetherClay()).id("nether_clay").build(),
                BlockEntryBuilder.create().block(new BlockSteelPressurePlate()).id("steel_pressure_plate").build(),
                BlockEntryBuilder.create().block(new BlockInfernalEnchanter()).id("infernal_enchanter").tile(TileInfernalEnchanter.class).build(),
                BlockEntryBuilder.create().block(new BlockShaft()).id("shaft").build(),
                BlockEntryBuilder.create().block(new BlockBucket()).id("bucket").tile(TileBucket.class).build(),
                BlockEntryBuilder.create().block(new BlockDirtSlab()).id("dirt_slab").itemblock(b -> new ItemSimpleSlab(b, Blocks.DIRT)).build(),
                BlockEntryBuilder.create().block(new BlockBarrel(Material.WOOD)).id("barrel").tile(TileBarrel.class).build()
        );

        registerBlocks(BlockPlanter.BLOCKS.values());
        registerBlocks(BlockCandle.BLOCKS.values());
        registerBlocks(BlockVase.BLOCKS.values());
        registerBlocks(BlockAesthetic.BLOCKS.values());
        registerBlocks(BlockChime.BLOCKS);
        registerBlocks(BlockCobble.BLOCKS.values());
        registerBlocks(BlockUnfiredPottery.BLOCKS.values());
        registerBlocks(BlockRawPastry.BLOCKS.values());


    }

    public static void registerBlock(BlockEntry entry) {
        registerBlock(entry.getBlock(), entry.getItemBlock());
    }

    public static void registerBlock(Block block, @Nullable Item item) {
        if (block.getRegistryName() != null) {

            if (block instanceof BlockCamo) {
                block.setCreativeTab(BWMCreativeTabs.MINI_BLOCKS);
            } else {
                block.setCreativeTab(BWMCreativeTabs.BLOCKS);
            }
            //TODO remove this in 1.13, it is done automatically
            if (block.getTranslationKey().equals("tile.null"))
                block.setTranslationKey(block.getRegistryName().toString());
            REGISTRY.add(block);
            if (item != null) {
                BWMItems.registerItem(item);
            }
        }
    }

    public static Block registerBlock(@Nonnull Block block) {
        registerBlock(block, new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    public static void registerBlocks(Collection<? extends Block> blocks) {
        blocks.forEach(BlockRegistry::registerBlock);
    }

    public static void registerBlocks(BlockEntry... entries) {
        for (BlockEntry block : entries) {
            registerBlock(block);
        }
    }


    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        REGISTRY.forEach(block -> event.getRegistry().register(block));
        registerTileEntities();
    }

    @Override
    public int priority() {
        return 100;
    }
}
