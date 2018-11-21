package betterwithmods.module.recipes;

import betterwithmods.common.blocks.BlockUnfiredPottery;
import betterwithmods.common.registry.TurntableRotationManager;
import betterwithmods.common.registry.block.recipe.builder.TurntableRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class TurntableRecipes extends Feature {

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        TurntableRecipeBuilder builder = new TurntableRecipeBuilder();
        RecipeRegistry.TURNTABLE.registerAll(
                builder
                        .productState(BlockUnfiredPottery.getBlock(BlockUnfiredPottery.Type.CRUCIBLE))
                        .input(new ItemStack(Blocks.CLAY))
                        .outputs(new ItemStack(Items.CLAY_BALL))
                        .build(),

                builder
                        .productState(BlockUnfiredPottery.getBlock(BlockUnfiredPottery.Type.PLANTER))
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.CRUCIBLE))
                        .build(),

                builder
                        .productState(BlockUnfiredPottery.getBlock(BlockUnfiredPottery.Type.PLANTER))
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.VASE))
                        .outputs(new ItemStack(Items.CLAY_BALL))
                        .build(),

                builder
                        .productState(BlockUnfiredPottery.getBlock(BlockUnfiredPottery.Type.VASE))
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.URN))
                        .outputs(new ItemStack(Items.CLAY_BALL))
                        .build(),

                builder
                        .productState(Blocks.AIR)
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.URN))
                        .outputs(new ItemStack(Items.CLAY_BALL))
                        .build()
        );


        TurntableRotationManager.addAttachment(b -> b instanceof BlockTorch);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockLever);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockLadder);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockButton);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockWallSign);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockTripWireHook);

        TurntableRotationManager.addRotationHandler(block -> block instanceof BlockTorch, (world, pos) -> {
            IBlockState state = world.getBlockState(pos);
            return state.getValue(BlockTorch.FACING).getAxis().isVertical();
        });
        TurntableRotationManager.addRotationHandler(Blocks.LEVER, (world, pos) -> {
            IBlockState state = world.getBlockState(pos);
            return state.getValue(BlockLever.FACING).getFacing().getAxis().isVertical();
        });
        TurntableRotationManager.addRotationBlacklist(block -> block instanceof BlockPistonExtension);
        TurntableRotationManager.addRotationHandler(block -> block instanceof BlockUnfiredPottery, new TurntableRotationManager.IRotation() {
            @Override
            public boolean isValid(World world, BlockPos pos) {
                return true;
            }

            @Override
            public boolean canTransmitHorizontally(World world, BlockPos pos) {
                return false;
            }

            @Override
            public boolean canTransmitVertically(World world, BlockPos pos) {
                return false;
            }
        });
        TurntableRotationManager.addRotationHandler(Blocks.CLAY, new TurntableRotationManager.IRotation() {
            @Override
            public boolean isValid(World world, BlockPos pos) {
                return true;
            }

            @Override
            public boolean canTransmitHorizontally(World world, BlockPos pos) {
                return false;
            }

            @Override
            public boolean canTransmitVertically(World world, BlockPos pos) {
                return false;
            }
        });
        TurntableRotationManager.addRotationHandler(block -> block instanceof BlockPistonBase, (world, pos) -> !world.getBlockState(pos).getValue(BlockPistonBase.EXTENDED));
    }

    @Override
    public String getDescription() {
        return null;
    }
}
