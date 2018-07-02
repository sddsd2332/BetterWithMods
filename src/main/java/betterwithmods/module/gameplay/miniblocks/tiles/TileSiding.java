package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.SidingOrientation;

public class TileSiding extends TileMini {
    public TileSiding() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return SidingOrientation.VALUES[ordinal];
    }
}
