package betterwithmods.module.conversion;

import betterwithmods.library.common.modularity.impl.Module;
import betterwithmods.module.conversion.beacons.HCBeacons;
import betterwithmods.module.conversion.enchanting.HCEnchanting;
import betterwithmods.module.conversion.hunger.HCHunger;

public class Conversion extends Module {

    @Override
    public void setup() {

        addFeature(HCHunger.class, "applecore");

        addFeatures(
                new HCFurnace(),
                new HCEnchanting(),
                new HCHopper(),
                new HCBeacons(),
                new HCDeadweight()
        );
    }

    //Force Hardcore module to load last
    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

}
