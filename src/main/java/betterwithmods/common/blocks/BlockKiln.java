package betterwithmods.common.blocks;

import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.common.blocks.camo.CamoInfo;
import betterwithmods.common.blocks.camo.TileCamo;
import betterwithmods.common.blocks.tile.TileKiln;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Supplier;

public class BlockKiln extends BlockCamo {

    public BlockKiln(Supplier<Set<IBlockState>> states) {
        super(Material.ROCK, material -> states.get());
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState fromTile(IExtendedBlockState state, TileCamo tile) {
        return state.withProperty(BlockCamo.CAMO_INFO, new CamoInfo(tile.getState()));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileKiln();
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }
}
