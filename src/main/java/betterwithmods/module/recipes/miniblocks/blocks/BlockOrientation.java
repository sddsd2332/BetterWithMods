package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.library.common.block.IRotate;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.IOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileOrientation;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BlockOrientation<O extends IOrientation<O> & Comparable<O>, T extends TileOrientation<O>> extends BlockDynamic<T> {

    public abstract PropertyOrientation<O> getOrientationProperty();

    public BlockOrientation(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state);

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState actualState = super.getActualState(state, worldIn, pos);
        return actualState.withProperty(getOrientationProperty(), getOrientation(worldIn, pos));
    }

    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{getOrientationProperty()};
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IOrientation o = getOrientation(source, pos);
        if (o != null) {
            return o.getBounds();
        }

        return super.getBoundingBox(state, source, pos);
    }

    protected O getOrientation(IBlockAccess world, BlockPos pos) {
        if(world.getBlockState(pos).getBlock() instanceof BlockOrientation)
            return getTile(world, pos).map(TileOrientation::getOrientation).orElse(getOrientationProperty().getDefault());
        return null;
    }


    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

        O orientation = getOrientation(world,pos);
        if(orientation != null) {
            return true;
//            O neighbor = getOrientation(world,pos.offset(side));
//            if(neighbor != null && orientation != neighbor)
//                return true;
        }
        return super.shouldSideBeRendered(blockState, world, pos, side);
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        Optional<T> t = getTile(world,pos);
        if(t.isPresent()) {
            T tile = t.get();
            tile.setOrientation(tile.getOrientation().next());
            world.markBlockRangeForRenderUpdate(pos,pos);
        }
    }

    @Override
    public boolean rotates() {
        return true;
    }

//    @Override
//    public boolean rotateBlock(World world, @Nonnull BlockPos pos, @Nonnull EnumFacing axis) {
//        nextState(world,pos, world.getBlockState(pos));
//        return true;
//    }
}

