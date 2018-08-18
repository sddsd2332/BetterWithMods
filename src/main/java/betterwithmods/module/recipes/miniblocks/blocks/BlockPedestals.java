package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.PedestalOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TilePedestal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

public class BlockPedestals extends BlockMini {


    public BlockPedestals(Material material, Function<Material, Collection<IBlockState>> subtypes) {
        super(material, subtypes);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TilePedestal();
    }

    @Override
    public BaseOrientation getOrientationFromPlacement(EntityLivingBase placer, @Nullable EnumFacing face, ItemStack stack, BlockPos pos, float hitX, float hitY, float hitZ) {
        if (face != null)
            return PedestalOrientation.getFromVec(new Vec3d(hitX, hitY, hitZ), placer.isSneaking() ? face.getOpposite() : face);
        return BaseOrientation.DEFAULT;
    }


}
