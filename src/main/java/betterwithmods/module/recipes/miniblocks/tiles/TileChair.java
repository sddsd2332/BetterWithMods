package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.ChairOrientation;

public class TileChair extends TileMini {
    public TileChair() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return ChairOrientation.VALUES[ordinal];
    }
}
