package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.PedestalOrientation;

public class TilePedestal extends TileMini {
    public TilePedestal() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return PedestalOrientation.VALUES[ordinal];
    }

}