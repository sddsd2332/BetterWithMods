package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.CornerOrientation;

public class TileCorner extends TileMini {
    public TileCorner() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return CornerOrientation.VALUES[ordinal];
    }
}