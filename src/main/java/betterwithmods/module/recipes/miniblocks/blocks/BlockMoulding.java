package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.MouldingOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileMoulding;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMoulding extends BlockOrientation<MouldingOrientation, TileMoulding> {
    public static final PropertyOrientation<MouldingOrientation> ORIENTATION = new PropertyOrientation<>("orientation", MouldingOrientation.class, MouldingOrientation.PLACER, MouldingOrientation.VALUES);

    public BlockMoulding(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
        setDefaultState(getDefaultState().withProperty(ORIENTATION, MouldingOrientation.EAST_DOWN));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileMoulding();
    }

    @Override
    public PropertyOrientation<MouldingOrientation> getOrientationProperty() {
        return ORIENTATION;
    }
}
