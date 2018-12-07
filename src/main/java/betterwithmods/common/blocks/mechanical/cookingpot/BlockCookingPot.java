package betterwithmods.common.blocks.mechanical.cookingpot;

import betterwithmods.BetterWithMods;
import betterwithmods.common.tile.TileCookingPot;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.tile.TileVisibleInventory;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 3/3/17
 */
public class BlockCookingPot extends BlockBase {

    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 0.99d, 1),
            SELECTION_BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    public BlockCookingPot() {
        super(Material.ROCK);
        this.setHardness(3.5F);
        setSoundType(SoundType.STONE);
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }


    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{DirUtils.TILTING};
    }


    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return SELECTION_BOX.offset(pos);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOX;
    }


    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileCookingPot) {
                TileCookingPot pot = (TileCookingPot) tile;
                pot.insert(entityIn);
            }
        }
        if (entityIn instanceof EntityItem) {
            entityIn.setPosition(entityIn.posX, entityIn.posY + 0.1, entityIn.posZ); //Fix to stop items being caught on this
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking() && !world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && CapabilityUtils.hasInventory(tile, null)) {
                player.openGui(BetterWithMods.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
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

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    protected int getComparatorFromTile(TileEntity tile) {
        if (tile instanceof TileVisibleInventory) {
            return (int) (((TileVisibleInventory) tile).getPercentage() * 15);
        }
        return 0;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return getComparatorFromTile(tile);
    }
}
