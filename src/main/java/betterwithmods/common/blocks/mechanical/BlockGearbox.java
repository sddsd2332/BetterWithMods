package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.BWMAPI;
import betterwithmods.api.block.IAdvancedRotationPlacement;
import betterwithmods.api.block.IOverpower;
import betterwithmods.api.block.IRenderRotationPlacement;
import betterwithmods.client.ClientEventHandler;
import betterwithmods.common.BWSounds;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.common.blocks.mechanical.tile.TileGearbox;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BlockGearbox extends BWMBlock implements IBlockActive, IOverpower, IAdvancedRotationPlacement, IRenderRotationPlacement {
    private final int maxPower;

    public BlockGearbox(Material material, int maxPower) {
        super(material);
        this.maxPower = maxPower;
        this.setDefaultState(getDefaultState().withProperty(DirUtils.FACING, EnumFacing.UP).withProperty(ACTIVE, false));
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tooltip.gearbox.name"));
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float flX, float flY, float flZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return getStateForAdvancedRotationPlacement(getDefaultState(), placer.isSneaking() ? side.getOpposite() : side, flX, flY, flZ);
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.cycleProperty(DirUtils.FACING).withProperty(ACTIVE, false));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 5, 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        onChange(world, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        onChange(world, pos);
    }

    public void onChange(World world, BlockPos pos) {
        if (!world.isRemote) {
            withTile(world, pos).ifPresent(TileGearbox::onChanged);
        }
    }


    public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
        return getFacingFromState(world.getBlockState(pos));
    }

    public EnumFacing getFacingFromState(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }


    private void emitGearboxParticles(World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 5; i++) {
            float flX = pos.getX() + rand.nextFloat();
            float flY = pos.getY() + rand.nextFloat() * 0.5F + 1.0F;
            float flZ = pos.getZ() + rand.nextFloat();

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(ACTIVE)) {
            emitGearboxParticles(world, pos, rand);

            if (rand.nextInt(10) == 0) {
                TileGearbox tile = getTile(world, pos);
                if (tile != null && tile.isOverpowered())
                    world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.BLOCKS, 0.25F, world.rand.nextFloat() * 0.25F + 0.25F, true);
            }
            if (rand.nextInt(50) == 0) {
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.25F, world.rand.nextFloat() * 0.25F + 0.25F, false);
            }

        }
    }


    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean[] dirs = new boolean[6];
        for (int i = 0; i < 6; i++) {
            EnumFacing facing = EnumFacing.getFront(i);
            dirs[i] = BWMAPI.IMPLEMENTATION.isAxle(world, pos.offset(facing), facing.getOpposite()) && this.getFacing(world, pos) != facing;
        }
        return state.withProperty(DirUtils.DOWN, dirs[0]).withProperty(DirUtils.UP, dirs[1]).withProperty(DirUtils.NORTH, dirs[2]).withProperty(DirUtils.SOUTH, dirs[3]).withProperty(DirUtils.WEST, dirs[4]).withProperty(DirUtils.EAST, dirs[5]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(DirUtils.FACING).getIndex();
        int active = isActive(state) ? 1 : 0;
        return active | facing << 1;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, (meta & 1) == 1).withProperty(DirUtils.FACING, EnumFacing.getFront(meta >> 1));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, ACTIVE, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World worldIn, BlockPos pos) {
        return isActive(state) ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileGearbox(maxPower);
    }

    public Optional<TileGearbox> withTile(World world, BlockPos pos) {
        return Optional.of(getTile(world, pos));
    }

    public TileGearbox getTile(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileGearbox)
            return (TileGearbox) tile;
        return null;
    }

    @Override
    public void onChangeActive(World world, BlockPos pos, boolean newValue) {
        if (newValue) {
            world.playSound(null, pos, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.25F + 0.25F);
        }
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        overpowerSound(world, pos);
        EnumFacing facing = world.getBlockState(pos).getValue(DirUtils.FACING);
        world.setBlockState(pos, getDefaultState().withProperty(DirUtils.FACING, facing));
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        EnumFacing facing = getFacingFromState(state);
        if (facing.getAxis().isHorizontal())
            return state.withProperty(DirUtils.FACING, rot.rotate(facing));
        return state;
    }

    @Override
    public IBlockState getStateForAdvancedRotationPlacement(IBlockState defaultState, EnumFacing facing, float hitX, float hitY, float hitZ) {
        float hitXFromCenter = hitX - 0.5F;
        float hitYFromCenter = hitY - 0.5F;
        float hitZFromCenter = hitZ - 0.5F;
        EnumFacing newFacing = facing;
        switch (facing.getAxis()) {
            case Y:
                if (inCenter(hitXFromCenter, hitZFromCenter, 1 / 16f)) {
                    newFacing = facing.getOpposite();
                } else if (isMax(hitXFromCenter, hitZFromCenter)) {
                    newFacing = ((hitXFromCenter > 0) ? EnumFacing.EAST : EnumFacing.WEST);
                } else {
                    newFacing = ((hitZFromCenter > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH);
                }
                break;
            case X:
                if (inCenter(hitYFromCenter, hitZFromCenter, 1 / 16f)) {
                    newFacing = facing;
                } else if (isMax(hitYFromCenter, hitZFromCenter)) {
                    newFacing = ((hitYFromCenter > 0) ? EnumFacing.UP : EnumFacing.DOWN);
                } else {
                    newFacing = ((hitZFromCenter > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH);
                }
                break;
            case Z:
                if (inCenter(hitYFromCenter, hitXFromCenter, 1 / 16f)) {
                    newFacing = facing;
                } else if (isMax(hitYFromCenter, hitXFromCenter)) {
                    newFacing = ((hitYFromCenter > 0) ? EnumFacing.UP : EnumFacing.DOWN);
                } else {
                    newFacing = ((hitXFromCenter > 0) ? EnumFacing.EAST : EnumFacing.WEST);
                }
                break;
        }

        return defaultState.withProperty(DirUtils.FACING, newFacing);

    }

    @Override
    public IBlockState getRenderState(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, ItemStack stack, EntityLivingBase placer) {
        return getStateForAdvancedRotationPlacement(getDefaultState(), facing, flX, flY, flZ);
    }

    @Override
    public RenderFunction getRenderFunction() {
        return ClientEventHandler::renderBasicGrid;
    }

    @Override
    public boolean rotates() {
        return true;
    }
}
