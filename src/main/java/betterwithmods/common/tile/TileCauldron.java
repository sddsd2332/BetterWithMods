package betterwithmods.common.tile;

import betterwithmods.common.BWMRegistry;

public class TileCauldron extends TileCookingPot {
    public TileCauldron() {
        super(BWMRegistry.CAULDRON);
    }

    @Override
    public String getName() {
        return "inv.cauldron.name";
    }


}
