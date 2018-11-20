package betterwithmods.common.tile;

import betterwithmods.library.common.inventory.SimpleStackHandler;
import betterwithmods.library.common.tile.TileBasicInventory;


public class TileSteelAnvil extends TileBasicInventory {
    private SimpleStackHandler result = new SimpleStackHandler(1, this);

    public TileSteelAnvil() {
        super();
    }

    @Override
    public int getInventorySize() {
        return 16;
    }

    public SimpleStackHandler getResult() {
        return result;
    }
}
