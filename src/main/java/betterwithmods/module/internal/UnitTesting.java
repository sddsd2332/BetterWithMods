package betterwithmods.module.internal;

import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.testing.BaseTest;
//import betterwithmods.testing.BulkRecipeTests;
//import betterwithmods.testing.CookingPotTests;
//import betterwithmods.testing.SawRecipesTest;
import com.google.common.collect.Lists;
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
//        Lists.newArrayList(new BulkRecipeTests(), new CookingPotTests(), new SawRecipesTest()).forEach(BaseTest::run);
    }

    @Override
    public int priority() {
        return -1;
    }
}
