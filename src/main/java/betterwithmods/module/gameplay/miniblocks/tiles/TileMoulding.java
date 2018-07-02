package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.MouldingOrientation;

public class TileMoulding extends TileMini {
    public TileMoulding() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return MouldingOrientation.VALUES[ordinal];
    }
}
