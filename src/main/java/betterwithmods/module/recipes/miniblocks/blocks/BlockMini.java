package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.api.block.IRenderRotationPlacement;
import betterwithmods.client.ClientEventHandler;
import betterwithmods.client.baking.UnlistedPropertyGeneric;
import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.tile.TileCamo;
import betterwithmods.module.recipes.miniblocks.DynblockUtils;
import betterwithmods.module.recipes.miniblocks.ItemMini;
import betterwithmods.module.recipes.miniblocks.client.MiniInfo;
import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileMini;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BlockMini extends BlockDynamic implements IRenderRotationPlacement {

    public static final IUnlistedProperty<MiniInfo> MINI_INFO = new UnlistedPropertyGeneric<>("mini", MiniInfo.class);

    public BlockMini(Material material, Function<Material, Collection<IBlockState>> subtypes) {
        super(material, subtypes);
    }

    public BaseOrientation getDefaultOrientation(ItemStack stack) {
        return BaseOrientation.DEFAULT;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.addAll(DynblockUtils.MATERIAL_VARIANTS.get(material).stream().sorted(this::compareBlockStates).map(state -> DynblockUtils.fromParent(this, state)).collect(Collectors.toList()));
    }

    @Override
    public ItemBlock createItemBlock(Block block) {
        return new ItemMini(block);
    }

    private int compareBlockStates(IBlockState a, IBlockState b) {
        Block blockA = a.getBlock();
        Block blockB = b.getBlock();
        int compare = Integer.compare(Block.getIdFromBlock(blockA), Block.getIdFromBlock(blockB));
        if (compare == 0)
            return Integer.compare(blockA.getMetaFromState(a), blockB.getMetaFromState(b));
        else
            return compare;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{MINI_INFO});
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state);

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState fromTile(IExtendedBlockState state, TileCamo tile) {
        return state.withProperty(MINI_INFO, new MiniInfo((TileMini) tile));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Optional<TileMini> getTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileMini)
            return Optional.of((TileMini) tile);
        return Optional.empty();
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getTile(source, pos).map(t -> t.getOrientation().getBounds()).orElse(Block.FULL_BLOCK_AABB);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    }

    @Override
    public boolean rotateBlock(World world, @Nonnull BlockPos pos, @Nullable EnumFacing axis) {
        return getTile(world, pos).map(t -> t.changeOrientation(t.getOrientation().next(), false)).orElse(false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(World world, Block block, BlockPos pos, ItemStack stack, EntityPlayer player, EnumFacing side, RayTraceResult target, double partial) {
        ClientEventHandler.renderMiniBlock(world, block, pos, stack, player, side, target, partial);
    }

    public abstract BaseOrientation getOrientationFromPlacement(EntityLivingBase placer, @Nullable EnumFacing face, ItemStack stack, BlockPos pos, float hitX, float hitY, float hitZ);

    @Override
    public AxisAlignedBB getBounds(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, ItemStack stack, EntityLivingBase placer) {
        return getOrientationFromPlacement(placer, facing, stack, pos, flX, flY, flZ).getBounds();
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        rotateBlock(world, pos, null);
    }

    @Override
    public boolean rotates() {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return getTile(worldIn, pos).map(TileMini::getOrientation).map(o -> o.getFaceShape(face)).orElse(BlockFaceShape.UNDEFINED);
    }
}
