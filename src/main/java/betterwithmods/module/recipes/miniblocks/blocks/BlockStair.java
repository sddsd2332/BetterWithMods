package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.StairOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileStair;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockStair extends BlockOrientation<StairOrientation, TileStair> {

    public static final PropertyOrientation<StairOrientation> ORIENTATION = new PropertyOrientation<>("orientation", StairOrientation.class, StairOrientation.PLACER, StairOrientation.VALUES);

    public BlockStair(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
    }

    private List<AxisAlignedBB> getCollisionBoxes(World world, BlockPos pos) {
        StairOrientation o = getOrientation(world,pos);
        if (o != null) {
            return o.getCollison();
        }
        return Lists.newArrayList();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        for (AxisAlignedBB axisalignedbb : getCollisionBoxes(worldIn, pos)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
        List<RayTraceResult> results = Lists.newArrayList();

        for (AxisAlignedBB axisalignedbb : getCollisionBoxes(worldIn, pos)) {
            results.add(this.rayTrace(pos, start, end, axisalignedbb));
        }

        RayTraceResult finalResult = null;
        double d1 = 0.0D;
        for (RayTraceResult raytraceresult : results) {
            if (raytraceresult != null) {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1) {
                    finalResult = raytraceresult;
                    d1 = d0;
                }
            }
        }
        return finalResult;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileStair();
    }

    @Override
    public PropertyOrientation<StairOrientation> getOrientationProperty() {
        return ORIENTATION;
    }
}
