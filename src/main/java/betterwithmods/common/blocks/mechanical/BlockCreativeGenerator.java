package betterwithmods.common.blocks.mechanical;

import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.common.tile.TileCreativeGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by primetoxinz on 8/5/16.
 */
public class BlockCreativeGenerator extends BWMBlock {
    public BlockCreativeGenerator() {
        super(Material.WOOD);

    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCreativeGenerator();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

}
