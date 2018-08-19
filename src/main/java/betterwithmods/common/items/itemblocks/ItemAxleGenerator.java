package betterwithmods.common.items.itemblocks;

import betterwithmods.api.block.IRenderRotationPlacement;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockAxle;
import betterwithmods.common.blocks.mechanical.BlockAxleGenerator;
import betterwithmods.util.DirUtils;
import betterwithmods.util.TooltipLib;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;

import static betterwithmods.client.ClientEventHandler.getPlayerPositionDelta;

public abstract class ItemAxleGenerator extends ItemBlock implements IRenderRotationPlacement {
    protected int radius;

    public ItemAxleGenerator(Block block) {
        super(block);

        this.setMaxStackSize(1);
    }

    public abstract boolean isAxis(EnumFacing.Axis axis);

    private void showErrorMessage(EntityPlayer player, Error error) {
        String block = Objects.requireNonNull(getRegistryName()).getNamespace();
        player.sendMessage(TooltipLib.getMessageComponent(error.format(block)));
    }

    public AxisAlignedBB getBounds(EnumFacing.Axis axis, int radius) {
        if (axis != null) {
            switch (axis) {
                case X:
                    return new AxisAlignedBB(1 / 16f, -radius, -radius, 15 / 16f, radius + 1, radius + 1);
                case Z:
                    return new AxisAlignedBB(-radius, -radius, 1 / 16f, radius + 1, radius + 1, 15 / 16f);
            }
        }
        return Block.NULL_AABB;
    }

    private boolean containsOtherGenerator(World world, BlockPos center, EnumFacing.Axis axis, EntityPlayer player) {
        int d = radius * 2;
        AxisAlignedBB box = getBounds(axis, d);
        Iterable<BlockPos> positions = WorldUtils.getPosInBox(box.offset(center));
        for (BlockPos p : positions) {
            IBlockState state = world.getBlockState(p);
            if (state.getBlock() instanceof BlockAxleGenerator) {
                showErrorMessage(player, Error.CONFLICT);
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
        return !containsOtherGenerator(world, pos, axis, player);
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

    protected EnumFacing.Axis getAxleAxis(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockAxle) {
            EnumFacing.Axis axis = state.getValue(DirUtils.AXIS);
            return isAxis(axis) ? axis : null;
        }
        return null;
    }

    @Override
    public AxisAlignedBB getBounds(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, ItemStack stack, EntityLivingBase placer) {
        return getBounds(getAxleAxis(world, pos), this.radius);
    }

    @Override
    public void render(World world, Block block, BlockPos pos, ItemStack stack, EntityPlayer player, EnumFacing side, RayTraceResult target, double partial) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == BWMBlocks.WOODEN_AXLE) {
            Vec3d vec = target.hitVec.addVector(-target.getBlockPos().getX(), -target.getBlockPos().getY(), -target.getBlockPos().getZ());
            float x = (float) vec.x, y = (float) vec.y, z = (float) vec.z;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(4.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);

            double[] deltas = getPlayerPositionDelta(player, partial);

            double dx = deltas[0];
            double dy = deltas[1];
            double dz = deltas[2];

            AxisAlignedBB box = this.getBounds(world, pos, side, x, y, z, stack, player);

            if (box != null) {
                renderModel(world, pos, pos.getX() - dx, pos.getY() - dy, pos.getZ() - dz, partial);
                RenderGlobal.drawSelectionBoundingBox(box.grow(0.002D).offset(pos).offset(-dx, -dy, -dz), 0.0F, 0.0F, 0.0F, 0.4F);
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    @SideOnly(Side.CLIENT)
    public abstract void renderModel(World world, BlockPos pos, double x, double y, double z, double partial);


    @SideOnly(Side.CLIENT)
    public enum Error {
        CONFLICT("generator.conflict"),
        SPACE("%s.space"),
        AXLE("%s.axle");

        public final String key;

        Error(String key) {
            this.key = key;
        }

        public String format(String block) {
            return String.format(key, block);
        }
    }
}
