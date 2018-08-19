package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.MouldingOrientation;

public class TileMoulding extends TileMini {
    public TileMoulding() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return MouldingOrientation.VALUES[ordinal];
    }
}
