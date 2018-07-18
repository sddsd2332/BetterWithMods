package betterwithmods.common.registry.base;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class CraftingManagerBase<T> {
    protected final List<T> recipes = Lists.newArrayList();

    public List<T> getRecipes() {
        return recipes;
    }

    public abstract List<T> getDisplayRecipes();

    public abstract T addRecipe(@Nonnull T recipe);

    public boolean removeRecipe(@Nonnull T recipe) {
        return recipes.remove(recipe);
    }


}
