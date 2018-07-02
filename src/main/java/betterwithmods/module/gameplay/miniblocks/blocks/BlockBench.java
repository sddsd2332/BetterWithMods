package betterwithmods.module.gameplay.miniblocks.blocks;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Function;

public class BlockBench extends BlockFurniture implements ISittable {

    private static final AxisAlignedBB BENCH_TOP = new AxisAlignedBB(0, 6d / 16d, 0, 1, 8 / 16d, 1);
    private static final ImmutableList<AxisAlignedBB> BENCH = ImmutableList.of(new AxisAlignedBB(0, 6/16d, 0, 1, 0.5, 1), new AxisAlignedBB(6 / 16d, 0, 6 / 16d, 10 / 16d, 8 / 16d, 10 / 16d));

    public BlockBench(Material material, Function<Material, Collection<IBlockState>> subtypes) {
        super(material, subtypes);
        setDefaultState(getDefaultState().withProperty(SUPPORTED, true));
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BENCH_TOP;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        return BENCH_TOP.offset(pos);
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockBench;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        removeEntities(state, worldIn, pos);
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        removeEntities(state, world, pos);
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return attemptToSit(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public double getOffset() {
        return 0;
    }

    @Override
    public ImmutableList<AxisAlignedBB> getBoxes() {
        return BENCH;
    }
}
