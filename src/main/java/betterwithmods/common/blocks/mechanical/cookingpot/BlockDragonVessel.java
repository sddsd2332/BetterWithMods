package betterwithmods.common.blocks.mechanical.cookingpot;

import betterwithmods.common.tile.TileDragonVessel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDragonVessel extends BlockCookingPot {

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileDragonVessel();
    }

    @Override
    protected int getComparatorFromTile(TileEntity tile) {
        if (tile instanceof TileDragonVessel) {
            return (int) (((TileDragonVessel) tile).getPercent() * 15);
        }
        return 0;
    }
}
