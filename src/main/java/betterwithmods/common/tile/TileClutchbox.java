package betterwithmods.common.tile;

import betterwithmods.api.BWMAPI;

public class TileClutchbox extends TileGearbox {

    public TileClutchbox() {
    }

    public TileClutchbox(int maxPower) {
        super(maxPower);
    }

    @Override
    public void onChanged() {

        if (BWMAPI.IMPLEMENTATION.isRedstonePowered(world, pos)) {
            setPower(0);
            markDirty();
            return;
        }

        super.onChanged();
    }
}
