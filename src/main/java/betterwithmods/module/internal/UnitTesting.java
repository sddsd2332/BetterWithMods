package betterwithmods.module.internal;

import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.testing.BWMTests;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public class UnitTesting extends Feature {
    @Override
    protected boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Run unit tests on BWM features, confirms that certain things work as intended.";
    }

    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        BWMTests.runTests();
    }
}
