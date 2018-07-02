package betterwithmods.testing;

import betterwithmods.testing.base.BaseTest;
import com.google.common.collect.Lists;

public class BWMTests {

    public static void runTests() {
        Lists.newArrayList(new BulkRecipeTests(), new CookingPotTests(), new SawRecipesTest()).forEach(BaseTest::run);
    }

}
