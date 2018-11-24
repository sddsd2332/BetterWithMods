package betterwithmods.module.compat.patchouli.block;

import betterwithmods.common.registry.block.managers.CraftingManagerBlock;
import betterwithmods.common.registry.block.recipe.BlockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockProcessor<V extends BlockRecipe<V>> implements IComponentProcessor {

    protected V recipe;

    private CraftingManagerBlock<V> registry;

    private final Pattern inputPattern, outputPattern;

    public BlockProcessor(CraftingManagerBlock<V> registry, int outputCount) {
        this.registry = registry;
        this.inputPattern = Pattern.compile("input");
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
            Ingredient ingredient = recipe.getInput();
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
