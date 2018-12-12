package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.ColumnOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileColumn;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockColumn extends BlockOrientation<ColumnOrientation, TileColumn> {

    public static final PropertyOrientation<ColumnOrientation> ORIENTATION = new PropertyOrientation<>("orientation", ColumnOrientation.class, ColumnOrientation.PLACER, ColumnOrientation.VALUES);

    public BlockColumn(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
    }

    @Override
    public PropertyOrientation<ColumnOrientation> getOrientationProperty() {
        return ORIENTATION;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileColumn();
    }

}
