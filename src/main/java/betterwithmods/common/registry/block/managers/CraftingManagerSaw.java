package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.block.recipe.SawRecipe;
import betterwithmods.lib.ModLib;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CraftingManagerSaw extends CraftingManagerBlock<SawRecipe> {

    public CraftingManagerSaw() {
        super(new ResourceLocation(ModLib.MODID, "saw"), SawRecipe.class);
    }

    public SawRecipe addRecipe(ItemStack input, ItemStack outputs) {
        return addRecipe(input, Lists.newArrayList(outputs));
    }

    public SawRecipe addRecipe(ItemStack input, List<ItemStack> outputs) {
        SawRecipe recipe = new SawRecipe(new BlockStateIngredient(input), outputs);
        register(recipe);
        return recipe;
    }

    public SawRecipe addRecipe(BlockStateIngredient input, ItemStack outputs) {
        return addRecipe(input, Lists.newArrayList(outputs));
    }

    public SawRecipe addRecipe(BlockStateIngredient input, List<ItemStack> outputs) {
        SawRecipe recipe = new SawRecipe(input, outputs);
        register(recipe);
        return recipe;
    }

    public SawRecipe addSelfdropRecipe(ItemStack stack) {
        return addRecipe(new BlockDropIngredient(stack), Lists.newArrayList(stack));
    }

}
