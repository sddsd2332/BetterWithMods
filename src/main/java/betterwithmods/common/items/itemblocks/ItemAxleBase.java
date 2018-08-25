package betterwithmods.common.items.itemblocks;

import betterwithmods.api.block.IRenderRotationPlacement;
import betterwithmods.common.blocks.mechanical.BlockAxle;
import betterwithmods.common.blocks.mechanical.BlockAxleGenerator;
import betterwithmods.util.DirUtils;
import betterwithmods.util.TooltipLib;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class ItemAxleBase extends ItemBlock implements IRenderRotationPlacement {
    protected int radius;

    public ItemAxleBase(Block block) {
        super(block);

        this.setMaxStackSize(16);
    }

    public abstract boolean isAxis(EnumFacing.Axis axis);

    private void showErrorMessage(EntityPlayer player, Error error) {
        String block = Objects.requireNonNull(getRegistryName()).getNamespace();
        player.sendMessage(TooltipLib.getMessageComponent(error.format(block)));
    }

    public AxisAlignedBB getBounds(EnumFacing.Axis axis, int radius) {
        switch (axis) {
            case X:
                return new AxisAlignedBB(0, -radius, -radius, 0, radius, radius);
            case Z:
                return new AxisAlignedBB(-radius, -radius, 0, radius, radius, 0);
            default:
                return Block.NULL_AABB;
        }
    }

    private boolean containsOtherGenerator(World world, BlockPos center, EnumFacing.Axis axis) {
        int d = radius * 2;
        AxisAlignedBB box = getBounds(DirUtils.rotateAroundY(axis), d);
        Iterable<BlockPos> positions = WorldUtils.getPosInBox(box.offset(center));
        for (BlockPos p : positions) {
            IBlockState state = world.getBlockState(p);
            if (state.getBlock() instanceof BlockAxleGenerator) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidArea(World world, EntityPlayer player, BlockPos pos, EnumFacing.Axis axis) {
        AxisAlignedBB box = getBounds(axis, radius);
        if (box == null)
            return false;
        Iterable<BlockPos> positions = WorldUtils.getPosInBox(box.offset(pos));
        for (BlockPos p : positions) {
            if (onAxis(pos, p, axis)) {
                IBlockState state = world.getBlockState(p);
                if (state.getBlock() instanceof BlockAxle) {
                    continue;
                } else {
                    showErrorMessage(player, Error.SPACE);
                    return false;
                }
            }
            IBlockState state = world.getBlockState(p);
            if (!state.getMaterial().isReplaceable()) {
                return false;
            }
        }
        return !containsOtherGenerator(world, pos, axis);
    }

    private boolean onAxis(BlockPos base, BlockPos test, EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                return base.getZ() == test.getZ() && base.getY() == test.getY();
            case Y:
                return base.getZ() == test.getZ() && base.getX() == test.getX();
            case Z:
                return base.getX() == test.getX() && base.getY() == test.getY();
            default:
                return false;
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return EnumActionResult.PASS;
        EnumFacing.Axis axis = getAxleAxis(worldIn, pos);
        if (axis != null) {
            if (isValidArea(worldIn, player, pos, axis)) {
                worldIn.setBlockState(pos, ((BlockAxleGenerator) block).getAxisState(axis));
                return EnumActionResult.SUCCESS;
            } else {
                showErrorMessage(player, Error.SPACE);
            }
        } else {
            showErrorMessage(player, Error.AXLE);
        }
        return EnumActionResult.PASS;
    }

    private EnumFacing.Axis getAxleAxis(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockAxle) {
            EnumFacing.Axis axis = state.getValue(DirUtils.AXIS);
            return isAxis(axis) ? axis : null;
        }
        return null;
    }

    @Override
    public AxisAlignedBB getBounds(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, ItemStack stack, EntityLivingBase placer) {
        return getBounds(facing.getAxis(), this.radius);
    }

    @SideOnly(Side.CLIENT)
    public enum Error {
        SPACE("bwm.message.%s.space"),
        AXLE("bwm.message.%s.axle");

        public final String key;

        Error(String key) {
            this.key = key;
        }

        public String format(String block) {
            return String.format(key, block);
        }
    }
}
