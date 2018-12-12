package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.blocks.BlockColumn;
import betterwithmods.module.recipes.miniblocks.blocks.BlockStair;
import betterwithmods.module.recipes.miniblocks.orientations.ColumnOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.StairOrientation;

public class TileStair extends TileOrientation<StairOrientation> {
    public TileStair() {
        super(BlockStair.ORIENTATION);
    }
}
