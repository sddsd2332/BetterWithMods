package betterwithmods.module.industry.pollution;

import betterwithmods.api.tile.IPollutant;
import net.minecraft.tileentity.TileEntityFurnace;

public class CapabilityFurnacePollution implements IPollutant {
    private final TileEntityFurnace furnace;

    public CapabilityFurnacePollution(TileEntityFurnace furnace) {
        this.furnace = furnace;
    }

    @Override
    public boolean isPolluting() {
        return furnace.isBurning();
    }

    @Override
    public float getPollutionRate() {
        return 0.01F;
    }
}
