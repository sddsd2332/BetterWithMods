package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.StairOrientation;

public class TileStair extends TileMini {
    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return StairOrientation.VALUES[ordinal];
    }
}
