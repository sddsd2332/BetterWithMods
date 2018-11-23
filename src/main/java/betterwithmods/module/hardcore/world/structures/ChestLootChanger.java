package betterwithmods.module.hardcore.world.structures;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;

public class ChestLootChanger implements IChanger {

    private ResourceLocation replacement;

    public ChestLootChanger(ResourceLocation replacement) {
        this.replacement = replacement;
    }

    @Override
    public boolean canChangeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
        return original instanceof BlockChest;
    }

    @Override
    public IBlockState changeState(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean canChangeLoot(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        return true;
    }

    @Override
    public ResourceLocation changeLootTable(StructureComponent structure, World world, BlockPos pos, ResourceLocation lootTable) {
        return replacement;
    }
}
