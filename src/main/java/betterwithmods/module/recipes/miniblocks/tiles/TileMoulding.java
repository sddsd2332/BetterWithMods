package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.blocks.BlockMoulding;
import betterwithmods.module.recipes.miniblocks.blocks.BlockSiding;
import betterwithmods.module.recipes.miniblocks.orientations.MouldingOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.SidingOrientation;

public class TileMoulding extends TileOrientation<MouldingOrientation> {
    public TileMoulding() {
        super(BlockMoulding.ORIENTATION);
    }
}
