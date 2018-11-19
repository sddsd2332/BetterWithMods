package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.recipe.input.IRecipeInputs;
import betterwithmods.api.recipe.input.impl.IngredientInputs;
import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.recipe.output.impl.ListOutputs;
import betterwithmods.api.tile.IBulkTile;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class BulkRecipe<V extends IForgeRegistryEntry<V>> extends IForgeRegistryEntry.Impl<V> implements Comparable<BulkRecipe<V>> {

    private final IRecipeInputs recipeInputs;
    private final IRecipeOutputs recipeOutput;
    protected int priority;

    public BulkRecipe(IRecipeInputs recipeInputs, IRecipeOutputs recipeOutput, int priority) {
        this.recipeInputs = recipeInputs;
        this.recipeOutput = recipeOutput;
        this.priority = priority;
    }

    public BulkRecipe(List<Ingredient> inputs, IRecipeOutputs outputs, int priority) {
        this(new IngredientInputs(inputs), outputs, priority);
    }

    public BulkRecipe(@Nonnull List<Ingredient> inputs, @Nonnull List<ItemStack> outputs) {
        this(inputs, outputs, 0);
    }

    public BulkRecipe(List<Ingredient> inputs, IRecipeOutputs outputs) {
        this(inputs, outputs, 0);
    }

    public BulkRecipe(List<Ingredient> inputs, @Nonnull List<ItemStack> outputs, int priority) {
        this(inputs, new ListOutputs(outputs), priority);
    }

    public NonNullList<ItemStack> onCraft(@Nullable World world, IBulkTile tile) {
        IRecipeOutputs containers = recipeInputs.consume(tile);
        if (containers != null) {
            NonNullList<ItemStack> items = NonNullList.create();
            items.addAll(containers.getOutputs());
            items.addAll(getOutputs());
            return BulkCraftEvent.fireOnCraft(tile, world, this, items);
        }
        return NonNullList.create();
    }

    public IRecipeOutputs getRecipeOutput() {
        return recipeOutput;
    }

    public IRecipeInputs getRecipeInputs() {
        return recipeInputs;
    }

    public List<ItemStack> getOutputs() {
        return recipeOutput.getOutputs();
    }

    public List<Ingredient> getInputs() {
        return recipeInputs.getInputs();
    }


    public boolean isInvalid() {
        return (getInputs().isEmpty() || getInputs().stream().anyMatch(InventoryUtils::isIngredientValid) || recipeOutput.isInvalid());
    }

    @Override
    public String toString() {
        return String.format("%s: %s -> %s", getClass().getSimpleName(), this.recipeInputs, this.recipeOutput);
    }

    /**
     * Recipes with higher priority will be crafted first.
     *
     * @return sorting priority for Comparable
     */
    public int getPriority() {
        return priority;
    }

    public BulkRecipe setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public int matches(IBulkTile tile) {
        return recipeInputs.orderedMatch(tile);
    }

    public boolean isHidden() {
        return false;
    }

    @Override
    public int compareTo(@Nonnull BulkRecipe<V> recipe) {
        return Comparator.comparingInt(BulkRecipe<V>::getPriority).reversed().compare(this, recipe);
    }
}

