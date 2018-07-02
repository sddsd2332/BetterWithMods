package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.ColumnOrientation;

public class TileColumn extends TileMini {
    public TileColumn() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return ColumnOrientation.VALUES[ordinal];
    }
}