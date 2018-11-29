package betterwithmods.module.internal;

import betterwithmods.api.BWMAPI;
import betterwithmods.api.capabilities.CapabilityAxle;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockDetector;
import betterwithmods.common.blocks.BlockHemp;
import betterwithmods.common.penalties.PenaltyHandlerRegistry;
import betterwithmods.common.registry.BellowsManager;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.advanceddispenser.DispenserBehaviorDynamite;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import betterwithmods.library.utils.WeatherUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.blockstate.PredicateBlockStateIngredient;
import betterwithmods.library.utils.ingredient.collections.BlockStateIngredientSet;
import betterwithmods.util.MechanicalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MiscRegistry extends RequiredFeature {

    public static final Fluid MILK = new Fluid("milk", new ResourceLocation(ModLib.MODID, "blocks/milk_still"), new ResourceLocation(ModLib.MODID, "blocks/milk_flowing"));
    public static final PenaltyHandlerRegistry PENALTY_HANDLERS = new PenaltyHandlerRegistry();

    public static void registerVanillaDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(BWMItems.DYNAMITE, new DispenserBehaviorDynamite());
    }

    private static void registerBUDBlacklist() {
        BlockBUD.BLACKLIST = new BlockStateIngredientSet(
                new BlockIngredient(Blocks.REDSTONE_WIRE, Items.REDSTONE),
                new BlockIngredient(Blocks.POWERED_REPEATER, Items.REPEATER),
                new BlockIngredient(Blocks.UNPOWERED_REPEATER, Items.REPEATER),
                new BlockIngredient(Blocks.UNLIT_REDSTONE_TORCH),
                new BlockIngredient(Blocks.REDSTONE_TORCH),
                new BlockDropIngredient(new ItemStack(BWMBlocks.LAMP)),
                new BlockDropIngredient(new ItemStack(BWMBlocks.BUDDY_BLOCK))
        );
    }

    private static void registerDetectorHandlers() {
        BlockDetector.DETECTION_HANDLERS = Sets.newHashSet(
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(WeatherUtils::isPrecipitationAt), facing -> facing == EnumFacing.UP),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> world.getBlockState(pos).getMaterial().isSolid()))),
                new BlockDetector.IngredientDetection(new BlockDropIngredient(new ItemStack(Items.REEDS))),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> world.getBlockState(pos).getBlock() instanceof BlockVine))),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> world.getBlockState(pos).getBlock().equals(BWMBlocks.LIGHT_SOURCE)))),
                new BlockDetector.IngredientDetection(new BlockIngredient(Lists.newArrayList(BWMBlocks.HEMP.getDefaultState().withProperty(BlockHemp.TOP, true)), Lists.newArrayList(new ItemStack(BWMBlocks.HEMP)))),
                new BlockDetector.EntityDetection(),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> {
                    BlockPos downOffset = pos.down();
                    IBlockState downState = world.getBlockState(downOffset);
                    Block downBlock = downState.getBlock();
                    if (!(downBlock instanceof BlockHemp) && downBlock instanceof BlockCrops) {
                        return ((BlockCrops) downBlock).isMaxAge(downState);
                    } else if (downBlock == Blocks.NETHER_WART) {
                        return downState.getValue(BlockNetherWart.AGE) >= 3;
                    }
                    return false;
                })))
        );
    }

    public static void registerHeatSources() {
        BWMHeatRegistry.addHeatSource(new BlockIngredient(Blocks.FIRE, Items.AIR), 1);
        BWMHeatRegistry.addHeatSource(new BlockIngredient(BWMBlocks.STOKED_FLAME, Items.AIR), 2);
        BWMHeatRegistry.addHeatSource(new BlockIngredient(Blocks.BARRIER, Items.AIR), 1);
    }

    private static void registerFireInfo() {

        Blocks.FIRE.setFireInfo(BWMBlocks.WOODEN_AXLE, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.WOODEN_BROKEN_GEARBOX, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.WOODEN_GEARBOX, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.HORIZONTAL_WINDMILL, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.VERTICAL_WINDMILL, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.WATERWHEEL, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.VINE_TRAP, 5, 20);
        //TODO 1.13 block of nethercoal

        registerFireInfo(new BlockStateIngredient("blockCandle"), 5, 20);
        registerFireInfo(new BlockStateIngredient("slats"), 5, 20);
        registerFireInfo(new BlockStateIngredient("grates"), 5, 20);
    }

    public static void registerFireInfo(BlockStateIngredient ingredient, int encouragement, int flammability) {
        for (IBlockState state : ingredient.getStates()) {
            Blocks.FIRE.setFireInfo(state.getBlock(), encouragement, flammability);
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        //FIXME Initialize api
        BWMAPI.IMPLEMENTATION = new MechanicalUtil();

        //FIXME Registry capabilities
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new CapabilityMechanicalPower.Impl(), CapabilityMechanicalPower.Default::new);
        CapabilityManager.INSTANCE.register(IAxle.class, new CapabilityAxle.Impl(), CapabilityAxle.Default::new);

        //FIXME Registry kiln blocks
        KilnStructureManager.registerKilnBlock(Blocks.BRICK_BLOCK.getDefaultState());
        KilnStructureManager.registerKilnBlock(Blocks.NETHER_BRICK.getDefaultState());

        FluidRegistry.registerFluid(MILK);
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        MiscRegistry.registerVanillaDispenserBehavior();
        registerHeatSources();
        registerBUDBlacklist();
        registerDetectorHandlers();
        registerFireInfo();
        BellowsManager.init();
    }

}
