package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.BWMAPI;
import betterwithmods.common.tile.TileClutchbox;
import betterwithmods.library.utils.DirUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockClutchBox extends BlockGearbox {

    private static final PropertyBool REDSTONE = PropertyBool.create("redstone");

    public BlockClutchBox(Material material, int maxPower) {
        super(material, maxPower);
        setDefaultState(getDefaultState().withProperty(REDSTONE, false));
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileClutchbox(maxPower);
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean redstone = BWMAPI.IMPLEMENTATION.isRedstonePowered(world, pos);
        return super.getActualState(state, world, pos).withProperty(REDSTONE, redstone);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        super.randomDisplayTick(state, world, pos, rand);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, ACTIVE, REDSTONE, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
    }

}
