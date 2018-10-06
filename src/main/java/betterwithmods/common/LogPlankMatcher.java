package betterwithmods.common;

import betterwithmods.library.common.recipes.IRecipePrototype;
import betterwithmods.library.common.recipes.RecipeMatcher;
import net.minecraft.item.crafting.IRecipe;

import java.util.function.Function;

public class LogPlankMatcher extends RecipeMatcher<IRecipe> {


    public LogPlankMatcher(Function<IRecipe, IRecipe> provider, IRecipePrototype prototype) {
        super(provider, (a,b) -> prototype.matches(a));
    }


}
