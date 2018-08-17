package betterwithmods.common.blocks;

import betterwithmods.common.tile.TileBeacon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 7/17/17.
 */
public class BlockBeacon extends net.minecraft.block.BlockBeacon {

    public BlockBeacon() {
        super();
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setTranslationKey("beacon");
        setLightLevel(1.0f);
    }

    public TileBeacon getTile(IBlockAccess world, BlockPos pos) {
        return ((TileBeacon) world.getTileEntity(pos));
    }

    @Override
    public boolean onBlockActivated(World worldIn, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        getTile(worldIn, pos).processInteraction(worldIn, playerIn, playerIn.getHeldItemMainhand());
        return true;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        getTile(worldIn, pos).onRemoved();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileBeacon();
    }
}
