package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.StairOrientation;

public class TileStair extends TileMini {
    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return StairOrientation.VALUES[ordinal];
    }
}
