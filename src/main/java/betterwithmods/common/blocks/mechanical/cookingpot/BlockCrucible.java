package betterwithmods.common.blocks.mechanical.cookingpot;

import betterwithmods.common.tile.TileCrucible;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrucible extends BlockCookingPot {

    public BlockCrucible() {
        setSoundType(SoundType.GLASS);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCrucible();
    }
}
