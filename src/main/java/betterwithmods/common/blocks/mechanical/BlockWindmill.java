package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.tile.IBannerInfo;
import betterwithmods.common.tile.TileWindmillHorizontal;
import betterwithmods.common.tile.TileWindmillVertical;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockWindmill extends BlockAxleGenerator {
    private final EnumFacing.Axis axis;

    public BlockWindmill(EnumFacing.Axis axis) {
        super(Material.WOOD);
        this.axis = axis;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBannerInfo) {
            ((IBannerInfo) tile).apply(stack);
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        if (axis == EnumFacing.Axis.Y)
            return new TileWindmillVertical();
        return new TileWindmillHorizontal();
    }
}
