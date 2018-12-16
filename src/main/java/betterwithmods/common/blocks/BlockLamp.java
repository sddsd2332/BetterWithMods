package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.tile.TileLamp;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.block.IBlockActive;
import betterwithmods.library.utils.colors.ColorUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockLamp extends BlockBase implements IBlockActive, IHempGrower, IBlockColor, IItemColor {
    private static final PropertyBool INVERTED = PropertyBool.create("inverted");

    public BlockLamp() {
        super(Material.REDSTONE_LIGHT);
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumDyeColor color : ColorUtils.DYES) {
            items.add(createLamp(ColorUtils.getDyeColor(color)));
        }
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return getTile(world, pos).map(TileLamp::getItemStack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack createLamp(int color) {
        ItemStack stack = new ItemStack(BWMBlocks.LAMP);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("color", color);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{ACTIVE, INVERTED};
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        int active = state.getValue(ACTIVE) ? 1 : 0;
        int inverted = state.getValue(INVERTED) ? 1 : 0;

        return active | inverted << 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean active = (meta & 1) == 1;
        boolean inverted = (meta >> 1 & 1) == 1;
        return getDefaultState().withProperty(ACTIVE, active).withProperty(INVERTED, inverted);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLamp();
    }

    @Override
    public boolean canGrowHemp(IBlockAccess world, BlockPos pos, IBlockState state) {
        return isActive(state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean active = isActive(state);
        return active ? 15 : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess access, @Nonnull BlockPos pos, EnumFacing side) {
        IBlockState offsetState = access.getBlockState(pos.offset(side));
        if (offsetState != state)
            return true;
        if (offsetState.getBlock() == this)
            return false;
        return super.shouldSideBeRendered(state, access, pos, side);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        onChanged(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        onChanged(worldIn, pos, state);
    }

    public void onChanged(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            boolean active = isActive(state);
            boolean redstone = WorldUtils.isRedstonePowered(world, pos);
            if (active != redstone) {
                setActive(world, pos, redstone);
            }
        }
    }

    @Override
    public boolean recolorBlock(World world, @Nonnull BlockPos pos, EnumFacing side, @Nonnull EnumDyeColor color) {
        if (!world.isRemote) {
            getTile(world, pos).ifPresent(t -> t.setColor(ColorUtils.getDyeColor(color)));
            return true;
        }
        return false;
    }


    @Override
    public int colorMultiplier(@Nonnull IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null && pos != null) {
            return getTile(world, pos).map(TileLamp::getColor).orElse(0);
        }
        return 0;
    }

    @Override
    public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
        return getStackColor(stack);
    }

    public static int getStackColor(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            return tag.getInteger("color");
        }
        return 0;
    }

    public Optional<TileLamp> getTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileLamp ? Optional.of((TileLamp) tile) : Optional.empty();
    }


    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        if (playerIn.isSneaking()) {
            if (hand == EnumHand.MAIN_HAND) {
                float f = state.getValue(INVERTED) ? 0.6F : 0.5F;
                worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
                setInverted(worldIn, pos, state, !isInverted(state));
            }

        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    private boolean isInverted(IBlockState state) {
        return state.getValue(INVERTED);
    }

    private void setInverted(World world, BlockPos pos, IBlockState state, boolean inverted) {
        world.setBlockState(pos, state.withProperty(INVERTED, inverted));
    }

    @Override
    public boolean isActive(IBlockState state) {
        return IBlockActive.super.isActive(state);
    }
}

