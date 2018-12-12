package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.SidingOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TileSiding;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSiding extends BlockOrientation<SidingOrientation, TileSiding> {
    public static final PropertyOrientation<SidingOrientation> ORIENTATION = new PropertyOrientation<>("orientation", SidingOrientation.class, SidingOrientation.PLACER, SidingOrientation.VALUES);

    public BlockSiding(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
        setDefaultState(getDefaultState().withProperty(ORIENTATION, SidingOrientation.DOWN));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileSiding();
    }

    @Override
    public PropertyOrientation<SidingOrientation> getOrientationProperty() {
        return ORIENTATION;
    }
}
