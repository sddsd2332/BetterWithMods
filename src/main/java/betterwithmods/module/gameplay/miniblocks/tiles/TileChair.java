package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.ChairOrientation;

public class TileChair extends TileMini {
    public TileChair() {}
    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return ChairOrientation.VALUES[ordinal];
    }
}
