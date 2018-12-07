package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.tile.TileCamo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class BlockFurniture extends BlockDynamic {
    protected static final PropertyBool SUPPORTED = PropertyBool.create("supported");

    public BlockFurniture(Material material, Function<Material, Collection<IBlockState>> subtypes) {
        super(material, subtypes);
    }

    public static RayTraceResult raytraceMultiAABB(List<AxisAlignedBB> aabbs, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = Lists.newArrayList();

        for (AxisAlignedBB axisalignedbb : aabbs) {
            list.add(rayTrace2(pos, start, end, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list) {
            if (raytraceresult != null) {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1) {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }

    // Block.raytrace
    private static RayTraceResult rayTrace2(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        Vec3d vec3d = start.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
        Vec3d vec3d1 = end.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.add((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), raytraceresult.sideHit, pos);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCamo();
    }

    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{SUPPORTED};
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED;
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean connected = isConnected(state, world, pos, EnumFacing.NORTH) || isConnected(state, world, pos, EnumFacing.WEST);
        return state.withProperty(SUPPORTED, !connected);
    }

    private boolean isConnected(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        EnumFacing opp = facing.getOpposite();
        IBlockState state1 = world.getBlockState(pos.offset(facing));
        IBlockState state2 = world.getBlockState(pos.offset(opp));
        return state1.getBlock().canBeConnectedTo(world, pos.offset(facing), opp) && state2.getBlock().canBeConnectedTo(world, pos.offset(opp), facing);
    }

    @Override
    public abstract boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing);

    public double getOffset() {
        return 0;
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
        return raytraceMultiAABB(getBoxes(), pos, start, end);
    }

    public abstract ImmutableList<AxisAlignedBB> getBoxes();

}
