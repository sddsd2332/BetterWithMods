package betterwithmods.common.tile;

import betterwithmods.module.internal.RecipeRegistry;

public class TileCrucible extends TileCookingPot {
    public TileCrucible() {
        super(RecipeRegistry.CRUCIBLE);
    }

    @Override
    public String getName() {
        return "inv.crucible.entityName";
    }

}
