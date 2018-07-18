package betterwithmods.common.tile;

import betterwithmods.common.BWRegistry;

public class TileCrucible extends TileCookingPot {
    public TileCrucible() {
        super(BWRegistry.CRUCIBLE);
    }

    @Override
    public String getName() {
        return "inv.crucible.name";
    }

}
