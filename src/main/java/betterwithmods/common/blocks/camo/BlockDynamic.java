package betterwithmods.common.blocks.camo;

import betterwithmods.client.baking.UnlistedPropertyGeneric;
import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.tile.TileDynamic;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import betterwithmods.module.recipes.miniblocks.DynblockUtils;
import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.ItemCamo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


public abstract class BlockDynamic<T extends TileDynamic> extends BlockBase {

    public static final IUnlistedProperty<CamoInfo> CAMO_INFO = new UnlistedPropertyGeneric<>("camo", CamoInfo.class);
    private final ISubtypeProvider subtypes;

    public BlockDynamic(Material material, ISubtypeProvider subtypes) {
        super(material);
        this.subtypes = subtypes;
        setCreativeTab(BWMCreativeTabs.MINI_BLOCKS);
    }

    public Optional<T> getTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDynamic)
            return Optional.of((T) tile);
        return Optional.empty();
    }

    public ItemBlock createItemBlock() {
        return new ItemCamo(this);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        IBlockState parent = getParent(world,pos);
        if(parent != null) {
            return parent.getBlock().getSoundType(parent, world,pos, entity);
        }
        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState parent = getParent(world,pos);
        if(parent != null) {
            return parent.getBlock().getLightValue(parent, world,pos);
        }
        return super.getLightValue(state, world, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return getTile(worldIn, pos).map(t -> t.getState().getBlockHardness(worldIn, pos)).orElse(super.getBlockHardness(blockState, worldIn, pos));
    }


    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return getTile(world, pos).map(t -> t.getState().getBlock().getExplosionResistance(world, pos, exploder, explosion)).orElse(super.getExplosionResistance(world, pos, exploder, explosion));
    }


    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (IBlockState state : subtypes.getSubtypes(material)) {
            items.add(DynblockUtils.fromParent(this, state));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, getProperties(), getUnlistedProperties());
    }

    @Override
    public IUnlistedProperty<?>[] getUnlistedProperties() {
        return new IUnlistedProperty[]{CAMO_INFO};
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileDynamic();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        final IExtendedBlockState extendedBS = (IExtendedBlockState) super.getExtendedState(state, world, pos);

        T tile = getTile(world, pos).orElse(null);
        if (tile != null) {
            return fromTile(extendedBS, tile);
        }
        return extendedBS;
    }

    public IExtendedBlockState fromTile(IExtendedBlockState state, T tile) {
        return state.withProperty(CAMO_INFO, new CamoInfo(tile.getState()));
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
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    public ItemStack getItem(IBlockAccess worldIn, BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileDynamic) {
            TileDynamic mini = (TileDynamic) tile;
            return mini.getPickBlock(null, null, state);
        }
        return new ItemStack(this);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        drops.add(getItem(world, pos, state));
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return getTile(world, pos).map(t -> t.getPickBlock(player, target, state)).orElse(new ItemStack(this));
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return withParent(world,pos).map(b ->b.getBlock().getFireSpreadSpeed(world, pos, face)).orElse(5);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return withParent(world,pos).map(b ->b.getBlock().getFlammability(world, pos, face)).orElse(10);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        Material material = this.getMaterial(state);
        if (material == Material.GLASS) {
            return layer == BlockRenderLayer.TRANSLUCENT;
        }
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
            CamoInfo info = extendedBlockState.getValue(CAMO_INFO);

            if (info != null) {
                IBlockState parent = info.getState();
                return parent.getBlock().canRenderInLayer(parent, layer);
            }
        }
        return super.canRenderInLayer(state, layer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {

        BlockPos offset = pos.offset(side);

        IBlockState neighorState = world.getBlockState(offset);


        if(neighorState.getBlock() != blockState.getBlock())
            return true;



        IBlockState parent = getParent(world, pos);
        if (parent != null) {
            Block block = parent.getBlock();
            if (block instanceof BlockBreakable) {
                IBlockState neighbor = getParent(world, offset);
                if (neighbor != null && neighbor.getBlock() == parent.getBlock()) {
                    return false;
                }
            }
            return parent.getBlock().shouldSideBeRendered(parent, world, pos, side);
        }
        return super.shouldSideBeRendered(blockState, world, pos, side);
    }

    public IBlockState getParent(IBlockAccess world, BlockPos pos) {
        return withParent(world, pos).orElse(null);
    }

    public Optional<IBlockState> withParent(IBlockAccess world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof BlockDynamic)
            return getTile(world, pos).map(TileDynamic::getState);
        return Optional.empty();
    }

}