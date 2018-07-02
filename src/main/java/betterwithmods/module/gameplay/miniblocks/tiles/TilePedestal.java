package betterwithmods.module.gameplay.miniblocks.tiles;

import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.orientations.PedestalOrientation;

public class TilePedestal extends TileMini {
    public TilePedestal() {
    }

    @Override
    public BaseOrientation deserializeOrientation(int ordinal) {
        return PedestalOrientation.VALUES[ordinal];
    }

}