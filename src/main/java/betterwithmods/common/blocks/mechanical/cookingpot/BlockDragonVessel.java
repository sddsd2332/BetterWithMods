package betterwithmods.common.blocks.mechanical.cookingpot;

import betterwithmods.common.blocks.tile.TileDragonVessel;
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
}
