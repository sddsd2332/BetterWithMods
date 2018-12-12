package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.ChairOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileChair;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockChair extends BlockOrientation<ChairOrientation, TileChair> implements ISittable{


    public static final PropertyOrientation<ChairOrientation> ORIENTATION = new PropertyOrientation<>("orientation", ChairOrientation.class, ChairOrientation.PLACER, ChairOrientation.VALUES);

    public BlockChair(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
    }

    @Override
    public PropertyOrientation<ChairOrientation> getOrientationProperty() {
        return ORIENTATION;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileChair();
    }


    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        removeEntities(state, worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return attemptToSit(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public double getOffset() {
        return 0;
    }
}
