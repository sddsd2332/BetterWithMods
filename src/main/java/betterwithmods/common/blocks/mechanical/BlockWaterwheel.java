package betterwithmods.common.blocks.mechanical;

import betterwithmods.common.blocks.mechanical.tile.TileWaterwheel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockWaterwheel extends BlockAxleGenerator {

    public BlockWaterwheel() {
        super(Material.WOOD);
    }


    @Override
    public int damageDropped(IBlockState state) {
        return 1;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileWaterwheel();
    }

}
