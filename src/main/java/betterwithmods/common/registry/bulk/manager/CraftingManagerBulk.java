package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.tile.IBulkTile;
import betterwithmods.common.registry.base.CraftingManagerBase;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CraftingManagerBulk<T extends BulkRecipe> extends CraftingManagerBase<T> {

    public T addRecipe(@Nonnull T recipe) {
        if (!recipe.isInvalid())
            recipes.add(recipe);
        return recipe;
    }

    public abstract boolean craftRecipe(World world, IBulkTile tile, ItemStackHandler inv);

    @Nonnull
    public NonNullList<ItemStack> craftItem(T recipe, World world, IBulkTile tile, ItemStackHandler inv) {
        return canCraft(recipe, tile, inv) ? recipe.onCraft(world, tile, inv) : NonNullList.create();
    }

    protected Optional<T> findRecipe(List<T> recipes, IBulkTile tile, ItemStackHandler inv) {
        return recipes.stream().map(r -> {
            int i = r.matches(inv);
            return Pair.of(r, i);
        }).filter(p -> p.getValue() > -1).sorted(Comparator.comparingInt(Pair::getValue)).map(Pair::getKey).sorted().findFirst();
    }

    public T findRecipe(IBulkTile tile, ItemStackHandler inv) {
        return findRecipe(recipes, tile, inv).orElse(null);
    }

    protected List<T> findRecipe(List<ItemStack> outputs) {
        List<T> recipes = findRecipeExact(outputs);
        if (recipes.isEmpty())
            recipes = findRecipeFuzzy(outputs);
        return recipes;
    }

    protected List<T> findRecipeFuzzy(List<ItemStack> outputs) {
        return recipes.stream().filter(r -> r.getRecipeOutput().matchesFuzzy(outputs)).collect(Collectors.toList());
    }

    protected List<T> findRecipeExact(List<ItemStack> outputs) {
        return recipes.stream().filter(r -> r.getRecipeOutput().matches(outputs)).collect(Collectors.toList());
    }

    public boolean canCraft(T recipe, IBulkTile tile, ItemStackHandler inv) {
        return recipe != null;
    }

    public T getRecipe(IBulkTile tile, ItemStackHandler inv) {
        return findRecipe(recipes, tile, inv).orElse(null);
    }

    public boolean remove(List<ItemStack> outputs) {
        return recipes.removeAll(findRecipe(outputs));
    }

    public boolean removeFuzzy(List<ItemStack> outputs) {
        return recipes.removeAll(findRecipeFuzzy(outputs));
    }

    public boolean removeExact(List<ItemStack> outputs) {
        return recipes.removeAll(findRecipeExact(outputs));
    }

    @Override
    public List<T> getDisplayRecipes() {
        return getRecipes();
    }
}
