package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.library.utils.DirUtils;
import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

import static betterwithmods.common.blocks.BlockPane.*;

public class BlockPane extends BlockDynamic {

    public BlockPane(Material material, ISubtypeProvider subtypes) {
        super(material,subtypes);
    }


    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{DirUtils.NORTH, DirUtils.SOUTH, DirUtils.EAST, DirUtils.WEST};
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, Entity entity, boolean pass) {
        collisionPane(state, world, pos, entityBox, collidingBoxes, entity, pass);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.getActualState(world, pos);
        AxisAlignedBB bound = new AxisAlignedBB(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
        for (PropertyBool dir : DirUtils.DIR_PROP_HORIZ)
            if (state.getValue(dir))
                bound = bound.union(PANE_BOUNDS.get(dir));
        return bound;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = canConnectTo(world, pos, EnumFacing.NORTH);
        boolean east = canConnectTo(world, pos, EnumFacing.EAST);
        boolean south = canConnectTo(world, pos, EnumFacing.SOUTH);
        boolean west = canConnectTo(world, pos, EnumFacing.WEST);
        return state.withProperty(DirUtils.NORTH, north).withProperty(DirUtils.EAST, east).withProperty(DirUtils.SOUTH, south).withProperty(DirUtils.WEST, west);
    }
}
