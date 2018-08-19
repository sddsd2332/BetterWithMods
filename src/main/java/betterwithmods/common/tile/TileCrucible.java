package betterwithmods.common.tile;

import betterwithmods.common.BWMRegistry;

public class TileCrucible extends TileCookingPot {
    public TileCrucible() {
        super(BWMRegistry.CRUCIBLE);
    }

    @Override
    public String getName() {
        return "inv.crucible.name";
    }

}
