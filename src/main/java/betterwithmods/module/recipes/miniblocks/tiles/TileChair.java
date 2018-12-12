package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.module.recipes.miniblocks.blocks.BlockChair;
import betterwithmods.module.recipes.miniblocks.blocks.BlockSiding;
import betterwithmods.module.recipes.miniblocks.orientations.ChairOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.SidingOrientation;

public class TileChair extends TileOrientation<ChairOrientation> {
    public TileChair() {
        super(BlockChair.ORIENTATION);
    }
}
