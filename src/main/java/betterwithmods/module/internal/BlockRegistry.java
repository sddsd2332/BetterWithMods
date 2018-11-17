package betterwithmods.module.internal;

import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.blocks.*;
import betterwithmods.common.blocks.blood_wood.BlockBloodLeaves;
import betterwithmods.common.blocks.blood_wood.BlockBloodLog;
import betterwithmods.common.blocks.blood_wood.BlockBloodSapling;
import betterwithmods.common.blocks.mechanical.*;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockCauldron;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockCrucible;
import betterwithmods.common.blocks.mechanical.cookingpot.BlockDragonVessel;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockFilteredHopper;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockMillstone;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockPulley;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockTurntable;
import betterwithmods.common.container.anvil.ContainerSteelAnvil;
import betterwithmods.common.container.bulk.ContainerCauldron;
import betterwithmods.common.container.bulk.ContainerCrucible;
import betterwithmods.common.container.bulk.ContainerFilteredHopper;
import betterwithmods.common.container.bulk.ContainerMill;
import betterwithmods.common.container.other.ContainerAdvancedDispenser;
import betterwithmods.common.container.other.ContainerInfernalEnchanter;
import betterwithmods.common.container.other.ContainerPulley;
import betterwithmods.common.items.itemblocks.*;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.tile.*;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.creation.BlockEntry;
import betterwithmods.library.common.block.creation.BlockEntryBuilderFactory;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import betterwithmods.module.hardcore.beacons.TileBeacon;
import betterwithmods.module.hardcore.beacons.TileEnderchest;
import betterwithmods.network.BWMNetwork;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class BlockRegistry extends RequiredFeature {

    private static List<Block> REGISTRY = Lists.newArrayList();

    public static void registerTileEntities() {
        //TODO
        GameRegistry.registerTileEntity(TileBeacon.class, new ResourceLocation(ModLib.MODID, "beacon"));
        GameRegistry.registerTileEntity(TileEnderchest.class, new ResourceLocation(ModLib.MODID, "enderchest"));
        GameRegistry.registerTileEntity(TileFurnace.class, new ResourceLocation(ModLib.MODID, "furnace"));
    }

    public static void registerBlock(BlockEntry entry) {
        registerBlock(entry.getBlock(), entry.getItemBlock());
    }

    public static void registerBlock(Block block, @Nullable Item item) {
        if (block.getRegistryName() != null) {
            //TODO remove this in 1.13, it is done automatically
            if (block.getTranslationKey().equals("tile.null"))
                block.setTranslationKey(block.getRegistryName().toString());
            REGISTRY.add(block);
            if (item != null) {
                ItemRegistry.addItem(item);
            }
        }
    }

    public static void registerBlocks(Collection<BlockEntry> set) {
        for (BlockEntry entry : set) {
            registerBlock(entry);
        }

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        REGISTRY.forEach(block -> event.getRegistry().register(block));
        registerTileEntities();
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BlockEntryBuilderFactory<Void> factory = BlockEntryBuilderFactory.create(getLogger());

        //noinspection unchecked
        registerBlocks(factory
                .tab(BWMCreativeTabs.BLOCKS)
                .builder().block(new BlockAnchor()).id("anchor").build()
                .builder().block(new BlockRope()).id("rope").build()
                .builder().block(new BlockBrokenGearbox(Material.WOOD)).id("wooden_broken_gearbox").build()
                .builder().block(new BlockWicker()).id("wicker").build()
                .builder().block(new BlockUrn(BlockUrn.EnumType.EMPTY)).id("urn").build()
                .builder().block(new BlockUrn(BlockUrn.EnumType.SOUL)).id("soul_urn").itemblock(ItemBlockUrn::new).build()
                .builder().block(new BlockFireStoked()).id("stoked_flame").noItem().build()
                .builder().block(new BlockHibachi()).id("hibachi").build()
                .builder().block(new BlockHemp()).id("hemp").itemblock(ItemHempSeed::new).build()
                .builder().block(new BlockDetector()).id("detector").build()
                .builder().block(new BlockLens()).id("lens").build()
                .builder().block(new BlockInvisibleLight()).id("invisible_light").noItem().build()
                .builder().block(new BlockWolf(new ResourceLocation("minecraft:wolf"))).id("companion_cube").build()
                .builder().block(new BlockBUD()).id("buddy_block").build()
                .builder().block(new BlockPlatform()).id("platform").build()
                .builder().block(new BlockMiningCharge()).id("mining_charge").build()
                .builder().block(new BlockFertileFarmland()).id("fertile_farmland").build()
                .builder().block(new BlockVineTrap()).id("vine_trap").build()
                .builder().block(new BlockTemporaryWater()).id("temporary_water").noItem().build()
                .builder().block(new BlockIronWall()).id("iron_wall").build()
                .builder().block(new BlockStake()).id("stake").build()
                .builder().block(new BlockStakeString()).id("stake_string").noItem().build()
                .builder().block(new BlockNetherGrowth()).id("nether_growth").itemblock(ItemBlockSpore::new).build()
                .builder().block(new BlockSteel()).id("steel_block").build()
                .builder().block(new BlockBloodLog()).id("blood_log").build()
                .builder().block(new BlockBloodLeaves()).id("blood_leaves").build()
                .builder().block(new BlockBloodSapling()).id("blood_sapling").build()
                .builder().block(new BlockNetherClay()).id("nether_clay").build()
                .builder().block(new BlockSteelPressurePlate()).id("steel_pressure_plate").build()
                .builder().block(new BlockShaft()).id("shaft").build()
                .builder().block(new BlockCandleHolder()).id("candle_holder").build()
                .builder().block(new BlockDirtSlab()).id("dirt_slab").itemblock(b -> new ItemSimpleSlab(b, Blocks.DIRT)).build()
                .builder().block(new BlockRailDetectorBase(cart -> !(cart instanceof EntityMinecartEmpty) || BlockRailDetectorBase.isRider(cart, Objects::nonNull))).id("detector_rail_stone").build()
                .builder().block(new BlockRailDetectorBase(cart -> BlockRailDetectorBase.isRider(cart, rider -> rider instanceof EntityPlayer))).id("detector_rail_steel").build()
                .complete());


        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerSteelAnvil.class)
                .tile(TileSteelAnvil.class).id("steel_anvil")
                .builder().block(new BlockSteelAnvil()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerCauldron.class)
                .tile(TileCauldron.class).id("cauldron")
                .builder().block(new BlockCauldron()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerCrucible.class)
                .tile(TileCrucible.class).id("crucible")
                .builder().block(new BlockCrucible()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerInfernalEnchanter.class)
                .tile(TileInfernalEnchanter.class).id("infernal_enchanter")
                .builder().block(new BlockInfernalEnchanter()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerMill.class)
                .tile(TileMill.class).id("millstone")
                .builder().block(new BlockMillstone()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger()).tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerFilteredHopper.class)
                .tile(TileFilteredHopper.class).id("filtered_hopper")
                .builder().block(new BlockFilteredHopper()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerPulley.class)
                .tile(TilePulley.class).id("pulley")
                .builder().block(new BlockPulley()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .guiHandler(BWMNetwork.GUI_HANDLER).container(ContainerAdvancedDispenser.class)
                .tile(TileAdvancedDispenser.class).id("block_dispenser")
                .builder().block(new BlockAdvancedDispenser()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileTurntable.class).id("turntable")
                .builder().block(new BlockTurntable()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileAxle.class).id("wooden_axle")
                .builder().block(new BlockAxle(Material.WOOD, 1, 1, 3)).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileGearbox.class).id("wooden_gearbox")
                .builder().block(new BlockGearbox(Material.WOOD, 1)).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileHandCrank.class).id("hand_crank")
                .builder().block(new BlockHandCrank()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileBellows.class).id("bellows")
                .builder().block(new BlockBellows()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileKiln.class).id("kiln")
                .builder().block(new BlockKiln(KilnStructureManager::getKilnBlocks)).noItem().build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileSaw.class).id("saw")
                .builder().block(new BlockSaw()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileWindmillHorizontal.class).id("horizontal_windmill")
                .builder().block(new BlockWindmill(EnumFacing.Axis.X)).itemblock(ItemHorizontalWindmill::new).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileWindmillVertical.class).id("vertical_windmill")
                .builder().block(new BlockWindmill(EnumFacing.Axis.Y)).itemblock(ItemVerticalWindmill::new).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileWaterwheel.class).id("waterwheel")
                .builder().block(new BlockWaterwheel()).itemblock(ItemWaterwheel::new).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileCreativeGenerator.class).id("creative_generator")
                .builder().block(new BlockCreativeGenerator()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileScrewPump.class).id("screw_pump")
                .builder().block(new BlockScrewPump()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileDragonVessel.class).id("dragon_vessel")
                .builder().block(new BlockDragonVessel()).itemblock(ItemBlockLimited::new).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileBucket.class).id("bucket")
                .builder().block(new BlockBucket()).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileBarrel.class).id("barrel")
                .builder().block(new BlockBarrel(Material.WOOD)).build()
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<EnumDyeColor>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(BlockCandle.GENERATOR)
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<EnumDyeColor>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .tile(TileVase.class).id("vase")
                .blockGenerator(BlockVase.GENERATOR)
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<EnumDyeColor>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .id("light")
                .blockGenerator(BlockLight.GENERATOR)
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<EnumDyeColor>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .id("light_inverted")
                .blockGenerator(BlockLight.INVERTED_GENERATOR)
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<BlockPlanks.EnumType>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(new BlockChime.Generator(Material.IRON))
                .blockGenerator(new BlockChime.Generator(Material.WOOD))
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<BlockCobble.Type>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(new BlockCobble.Generator())
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<BlockPlanter.Type>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(new BlockPlanter.Generator())
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<BlockAesthetic.Type>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(new BlockAesthetic.Generator())
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<BlockUnfiredPottery.Type>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(new BlockUnfiredPottery.Generator())
                .complete());
        registerBlocks(BlockEntryBuilderFactory.<BlockRawPastry.Type>create(getLogger())
                .tab(BWMCreativeTabs.BLOCKS)
                .blockGenerator(new BlockRawPastry.Generator())
                .complete());


    }

    @Override
    public int priority() {
        return 100;
    }
}
