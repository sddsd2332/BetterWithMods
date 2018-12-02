package betterwithmods.module.compat.patchouli.bulk;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BulkProcessor<V extends BulkRecipe<V>> implements IComponentProcessor {

    protected V recipe;

    private CraftingManagerBulk<V> registry;

    private final Pattern inputPattern, outputPattern;

    public BulkProcessor(CraftingManagerBulk<V> registry, int inputCount, int outputCount) {
        this.registry = registry;
        this.inputPattern = Pattern.compile("input([0-" + (inputCount - 1) + "])");
        this.outputPattern = Pattern.compile("output([0-" + (outputCount - 1) + "])");
    }

    @Override
    public void setup(IVariableProvider<String> variables) {
        String recipeName = variables.get("recipe");
        this.recipe = registry.getValue(new ResourceLocation(recipeName));
    }


    @Override
    public String process(String key) {
        Matcher input = inputPattern.matcher(key);
        Matcher output = outputPattern.matcher(key);
        if (input.matches()) {
            int index = Integer.parseInt(input.group(1)) - 1;
            List<Ingredient> ingredients = recipe.getInputs();

            Ingredient ingredient = ingredients.size() > index ? ingredients.get(index) : Ingredient.EMPTY;
            ItemStack[] stacks = ingredient.getMatchingStacks();
            ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];
            return ItemStackUtil.serializeStack(stack);
        } else if (output.matches()) {
            int index = Integer.parseInt(output.group(1)) - 1;
            List<ItemStack> stacks = recipe.getOutputs();
            ItemStack stack = stacks.size() > index ? stacks.get(index) : ItemStack.EMPTY;
            return ItemStackUtil.serializeStack(stack);
        }
        return null;
    }



}
