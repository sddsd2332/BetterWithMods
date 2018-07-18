package betterwithmods.module.gameplay.miniblocks.blocks;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.StairOrientation;
import betterwithmods.module.gameplay.miniblocks.tiles.TileMini;
import betterwithmods.module.gameplay.miniblocks.tiles.TileStair;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class BlockStair extends BlockMini {

    public BlockStair(Material material, Function<Material, Collection<IBlockState>> subtypes) {
        super(material, subtypes);
    }

    private List<AxisAlignedBB> getCollisionBoxes(World world, BlockPos pos) {
        BaseOrientation o = getTile(world, pos).map(TileMini::getOrientation).orElse(null);
        if (o instanceof StairOrientation) {
            return ((StairOrientation) o).getCollison();
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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        System.out.println(getTile(worldIn, pos).map(TileMini::getOrientation).map(IStringSerializable::getName).orElse("null"));

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
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
    public BaseOrientation getOrientationFromPlacement(EntityLivingBase placer, @Nullable EnumFacing face, ItemStack stack, BlockPos pos, float hitX, float hitY, float hitZ) {
        return StairOrientation.fromNeighbors(placer, pos, new Vec3d(hitX, hitY, hitZ), face);
    }
}
