package betterwithmods.module.hardcore.world.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;

public interface IChanger {

    boolean canChangeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original);

    IBlockState changeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original);

    default boolean canChangeLoot(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        return false;
    }

    default ResourceLocation changeLootTable(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        return lootTable;
    }
}
