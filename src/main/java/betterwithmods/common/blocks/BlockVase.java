package betterwithmods.common.blocks;

import betterwithmods.common.tile.TileVase;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.ColoredGenerator;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.colors.ColorUtils;
import betterwithmods.library.utils.InventoryUtils;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Christian on 24.09.2016.
 */
public class BlockVase extends BlockColored {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0, 0.125D, 0.875D, 1.0D, 0.875D);

    public static final ResourceLocation NAME_BASE = new ResourceLocation(ModLib.MODID, "vase");

    public static ColoredGenerator GENERATOR = new ColoredGenerator(NAME_BASE) {
        @Override
        public Block createBlock(EnumDyeColor variant) {
            return new BlockVase(variant);
        }
    };

    public static Set<Block> getAll() {
        return Arrays.stream(ColorUtils.DYES).map(BlockVase::getBlock).collect(Collectors.toSet());
    }

    public static Block getBlock(EnumDyeColor color) {
        return getBlock(NAME_BASE, color);
    }

    public BlockVase(EnumDyeColor color) {
        super(Material.ROCK, color);
        this.setHardness(2.0F);
        this.setHarvestLevel("pickaxe", -1);
        this.setSoundType(SoundType.GLASS);
    }

    public static ItemStack getStack(EnumDyeColor type) {
        return new ItemStack(getBlock(type));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileVase();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        CapabilityUtils.getInventory(tile, null).ifPresent(inv -> InventoryUtils.readFromStack(inv, stack));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking())
            return false;
        InventoryUtils.getItemHandler(worldIn, pos, null).ifPresent(inv -> {
            if (InventoryUtils.insertSingle(inv, heldItem, false).isEmpty()) {
                if (!playerIn.isCreative())
                    heldItem.shrink(1);
                worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                        ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        });
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity != null && entity instanceof EntityArrow) {
            world.playEvent(2001, pos, Block.getStateId(state));
            world.updateComparatorOutputLevel(pos, this);
            world.setBlockToAir(pos);
        }
        super.onEntityCollision(world, pos, state, entity);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        ItemStack stack = new ItemStack(this);
        TileEntity tile = world.getTileEntity(pos);
        CapabilityUtils.getInventory(tile, null).ifPresent(inv -> InventoryUtils.writeToStack(inv, stack));
        return stack;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
        return true;
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.CENTER_BIG : (face == EnumFacing.UP ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED); //Top is center instead of bowl to facilitate placing candles on these.
    }
}
