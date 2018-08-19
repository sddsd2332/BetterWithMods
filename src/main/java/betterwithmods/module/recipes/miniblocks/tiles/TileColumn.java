package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.ColumnOrientation;

public class TileColumn extends TileMini {
    public TileColumn() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return ColumnOrientation.VALUES[ordinal];
    }
}