package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.PedestalOrientation;
import betterwithmods.module.recipes.miniblocks.tiles.TilePedestal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPedestal extends BlockOrientation<PedestalOrientation, TilePedestal> {

    public static final PropertyOrientation<PedestalOrientation> ORIENTATION = new PropertyOrientation<>("orientation", PedestalOrientation.class, PedestalOrientation.PLACER, PedestalOrientation.VALUES);

    public BlockPedestal(Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
    }

    @Override
    public PropertyOrientation<PedestalOrientation> getOrientationProperty() {
        return ORIENTATION;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TilePedestal();
    }

}
