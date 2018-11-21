package betterwithmods.common.registry.bulk.recipes.builder;

import betterwithmods.api.recipe.input.IRecipeInputs;
import betterwithmods.api.recipe.input.impl.IngredientInputs;
import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.recipe.output.impl.ListOutputs;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class BulkRecipeBuilder<V extends BulkRecipe<V>> {

    private ResourceLocation name;
    protected IRecipeInputs inputs;
    protected IRecipeOutputs outputs;
    protected boolean handleContainers;
    protected int priority;

    protected abstract V create();

    public BulkRecipeBuilder<V> inputs(Block block) {
        return inputs(Item.getItemFromBlock(block));
    }

    public BulkRecipeBuilder<V> inputs(Item item) {
        return inputs(Ingredient.fromItem(item));
    }

    public BulkRecipeBuilder<V> inputs(ItemStack stack) {
        return inputs(Ingredient.fromStacks(stack));
    }

    public BulkRecipeBuilder<V> inputs(Ingredient... ingredients) {
        return inputs(new IngredientInputs(ingredients));
    }

    public BulkRecipeBuilder<V> inputs(IRecipeInputs inputs) {
        this.inputs = inputs;
        return this;
    }

    public BulkRecipeBuilder<V> disableContainers() {
        this.handleContainers = false;
        return this;
    }

    public BulkRecipeBuilder<V> outputs(ItemStack... stacks) {
        return outputs(new ListOutputs(stacks));
    }

    public BulkRecipeBuilder<V> outputs(List<ItemStack> stacks) {
        return outputs(new ListOutputs(stacks));
    }


    public BulkRecipeBuilder<V> outputs(IRecipeOutputs outputs) {
        this.outputs = outputs;
        return this;
    }

    public BulkRecipeBuilder<V> name(ResourceLocation name) {
        this.name = name;
        return this;
    }

    public BulkRecipeBuilder<V> priority(int priority) {
        this.priority = priority;
        return this;
    }

    public V build() {
        Preconditions.checkNotNull(inputs, "inputs");
        Preconditions.checkNotNull(outputs, "outputs");

        if (handleContainers) {
            this.inputs.disableContainers();
        }

        V recipe = create();
        if (name != null) {
            recipe.setRegistryName(name);
        }
        recipe.setPriority(priority);
        reset();
        return recipe;
    }

    public void reset() {
        name = null;
        inputs = null;
        outputs = null;
        priority = 0;
    }


}
