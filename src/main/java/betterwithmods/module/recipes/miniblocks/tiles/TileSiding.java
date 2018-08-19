package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.SidingOrientation;

public class TileSiding extends TileMini {
    public TileSiding() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return SidingOrientation.VALUES[ordinal];
    }
}
