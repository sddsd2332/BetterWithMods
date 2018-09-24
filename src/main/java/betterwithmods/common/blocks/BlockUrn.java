package betterwithmods.common.blocks;

import betterwithmods.api.block.ISoulContainer;
import betterwithmods.api.block.IUrnConnector;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockFilteredHopper;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

import static net.minecraft.util.EnumFacing.UP;

public class BlockUrn extends BWMBlock implements ISoulContainer {
    public static final PropertyBool UNDERHOPPER = PropertyBool.create("underhopper");
    private static final double OFFSET = 0.375D;
    private static final AxisAlignedBB URN_AABB = new AxisAlignedBB(0.3125D, 0, 0.3125D, 0.6875D, 0.625D, 0.6875D);
    private static final AxisAlignedBB UNDER_HOPPER_AABB = URN_AABB.offset(0, OFFSET, 0);

    private final EnumType type;

    public BlockUrn(EnumType type) {
        super(Material.ROCK);
        this.type = type;
        this.setHardness(2.0F);
        this.setDefaultState(getDefaultState().withProperty(UNDERHOPPER, false));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        BlockPos down = pos.down();
        BlockFaceShape blockfaceshape = worldIn.getBlockState(down).getBlockFaceShape(worldIn, down, UP);
        boolean below = blockfaceshape != BlockFaceShape.BOWL && blockfaceshape != BlockFaceShape.UNDEFINED;

        BlockPos up = pos.up();
        IBlockState state = worldIn.getBlockState(up);
        boolean above = state.getBlock() instanceof IUrnConnector;
        return below || above;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getActualState(world, pos).getValue(UNDERHOPPER) ? UNDER_HOPPER_AABB : URN_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (type != EnumType.EMPTY && rand.nextInt(64) == 0) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            float flX = x + rand.nextFloat();
            float flY = y + rand.nextFloat() * 0.5F + 0.625F;
            float flZ = z + rand.nextFloat();
            world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() * 0.1F + 0.45F,
                    false);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPos up = pos.up();
        Block block = world.getBlockState(up).getBlock();
        if (block instanceof BlockFilteredHopper) {
            return state.withProperty(UNDERHOPPER, true);
        }
        return state;
    }


    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UNDERHOPPER);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(UNDERHOPPER, meta == 1);
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(UNDERHOPPER) ? 1 : 0;
    }

    @Override
    public int getMaxSouls() {
        return type.getSouls();
    }

    @Override
    public boolean onFull(World world, BlockPos pos) {
        switch (type) {
            case EMPTY:
                if (world.setBlockToAir(pos)) {
                    InventoryUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMBlocks.SOUL_URN));
                }
                break;
            default:
                break;
        }
        return true;
    }

    public enum EnumType {
        EMPTY(8),
        SOUL(0);
        
        final int souls;

        EnumType(int souls) {
            this.souls = souls;
        }

        public int getSouls() {
            return souls;
        }
    }

}
