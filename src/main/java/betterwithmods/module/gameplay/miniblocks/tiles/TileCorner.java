package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.CornerOrientation;

public class TileCorner extends TileMini {
    public TileCorner() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return CornerOrientation.VALUES[ordinal];
    }
}