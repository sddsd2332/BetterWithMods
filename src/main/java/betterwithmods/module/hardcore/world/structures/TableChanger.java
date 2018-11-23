package betterwithmods.module.hardcore.world.structures;

import betterwithmods.module.recipes.miniblocks.DynamicType;
import betterwithmods.module.recipes.miniblocks.MiniBlocks;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;

public class TableChanger implements IChanger {
    @Override
    public boolean canChangeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState state) {
        if (state.getBlock() == Blocks.WOODEN_PRESSURE_PLATE) {
            IBlockState below = world.getBlockState(pos.down());
            return below.getBlock() instanceof BlockFence;
        }
        return false;
    }

    @Override
    public IBlockState changeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState state) {
        MiniBlocks.placeMini(world, pos.down(), DynamicType.TABLE, Blocks.PLANKS.getDefaultState());
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean canChangeLoot(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        return false;
    }

    @Override
    public ResourceLocation changeLootTable(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        return null;
    }
}
