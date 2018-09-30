package betterwithmods.testing;

import betterwithmods.library.testing.BaseTest;
import com.google.common.collect.Lists;

public class BWMTests {

    public static void runTests() {
        Lists.newArrayList(new BulkRecipeTests(), new CookingPotTests(), new SawRecipesTest()).forEach(BaseTest::run);
    }

}
