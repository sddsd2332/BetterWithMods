package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.CornerOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileCorner;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCorner extends BlockOrientation<CornerOrientation, TileCorner> {
    public static final PropertyOrientation<CornerOrientation> ORIENTATION = new PropertyOrientation<>("orientation", CornerOrientation.class, CornerOrientation.PLACER, CornerOrientation.VALUES);

    public BlockCorner(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
        setDefaultState(getDefaultState().withProperty(ORIENTATION, CornerOrientation.DOWN_EAST));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCorner();
    }

    @Override
    public PropertyOrientation<CornerOrientation> getOrientationProperty() {
        return ORIENTATION;
    }
}
