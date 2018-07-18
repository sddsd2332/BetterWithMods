package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.tile.IBulkTile;
import betterwithmods.api.tile.IHeated;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.common.tile.TileCookingPot;
import betterwithmods.util.InvUtils;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CraftingManagerPot extends CraftingManagerBulk<CookingPotRecipe> {

    public CookingPotRecipe addRecipe(List<Ingredient> inputs, IRecipeOutputs outputs, int heat) {
        return addRecipe(new CookingPotRecipe(inputs, outputs, heat));
    }

    public CookingPotRecipe addRecipe(List<Ingredient> inputs, List<ItemStack> outputs, int heat) {
        return addRecipe(new CookingPotRecipe(inputs, outputs, heat));
    }

    public CookingPotRecipe addStokedRecipe(ItemStack input, ItemStack... output) {
        return addStokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(input.copy())), Lists.newArrayList(output));
    }

    public CookingPotRecipe addStokedRecipe(ItemStack input, List<ItemStack> output) {
        return addStokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(input.copy())), output);
    }

    public CookingPotRecipe addStokedRecipe(ItemStack input, ItemStack output) {
        return addStokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(input)), Lists.newArrayList(output));
    }

    public CookingPotRecipe addStokedRecipe(Ingredient ingredient, ItemStack output) {
        return addStokedRecipe(Lists.newArrayList(ingredient), Lists.newArrayList(output));
    }

    public CookingPotRecipe addStokedRecipe(Ingredient ingredient, List<ItemStack> outputs) {
        return addStokedRecipe(Lists.newArrayList(ingredient), outputs);
    }

    public CookingPotRecipe addStokedRecipe(List<Ingredient> inputs, List<ItemStack> outputs) {
        return addRecipe(inputs, outputs, BWMHeatRegistry.STOKED_HEAT);
    }

    public CookingPotRecipe addStokedRecipe(List<Ingredient> inputs, IRecipeOutputs outputs) {
        return addRecipe(inputs, outputs, BWMHeatRegistry.STOKED_HEAT);
    }

    //Unstoked
    public CookingPotRecipe addUnstokedRecipe(List<Ingredient> inputs, ItemStack output) {
        return addUnstokedRecipe(inputs, Lists.newArrayList(output));
    }

    public CookingPotRecipe addUnstokedRecipe(ItemStack input, ItemStack... output) {
        return addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(input.copy())), Lists.newArrayList(output));
    }

    public CookingPotRecipe addUnstokedRecipe(ItemStack input, List<ItemStack> output) {
        return addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(input)), output);
    }

    public CookingPotRecipe addUnstokedRecipe(ItemStack input, ItemStack output) {
        return addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(input)), Lists.newArrayList(output));
    }

    public CookingPotRecipe addUnstokedRecipe(Ingredient ingredient, ItemStack output) {
        return addUnstokedRecipe(Lists.newArrayList(ingredient), Lists.newArrayList(output));
    }

    public CookingPotRecipe addUnstokedRecipe(Ingredient ingredient, List<ItemStack> outputs) {
        return addUnstokedRecipe(Lists.newArrayList(ingredient), outputs);
    }

    public CookingPotRecipe addUnstokedRecipe(List<Ingredient> inputs, List<ItemStack> outputs) {
        return addRecipe(inputs, outputs, BWMHeatRegistry.UNSTOKED_HEAT);
    }

    public CookingPotRecipe addUnstokedRecipe(List<Ingredient> inputs, IRecipeOutputs outputs) {
        return addRecipe(inputs, outputs, BWMHeatRegistry.UNSTOKED_HEAT);
    }

    public CookingPotRecipe addHeatlessRecipe(List<Ingredient> inputs, List<ItemStack> outputs, int heat) {
        return addRecipe(inputs, outputs, heat).setIgnoreHeat(true);
    }

    @Override
    public boolean canCraft(CookingPotRecipe recipe, IBulkTile tile) {
        return recipe != null && recipe.canCraft((IHeated) tile, tile.getWorld(), tile.getPos());
    }

    @Override
    public boolean craftRecipe(World world, IBulkTile tile) {
        if (tile instanceof TileCookingPot) {
            TileCookingPot pot = (TileCookingPot) tile;
            CookingPotRecipe r = findRecipe(tile);

            if (canCraft(r, tile)) {
                if (pot.cookProgress >= pot.getMax()) {
                    InvUtils.insert(tile.getInventory(), craftItem(r, world, tile), false);
                    pot.cookProgress = 0;
                    return true;
                }
                pot.cookProgress++;
            } else {
                pot.cookProgress = 0;
            }
        }
        return false;
    }

    @Override
    protected Optional<CookingPotRecipe> findRecipe(List<CookingPotRecipe> recipes, IBulkTile tile) {
        if (tile instanceof IHeated) {
            List<CookingPotRecipe> r1 = recipes.stream().filter(r -> r.canCraft((IHeated) tile, tile.getWorld(), tile.getPos())).collect(Collectors.toList());
            return super.findRecipe(r1, tile);
        }
        return Optional.empty();
    }


    public List<CookingPotRecipe> getRecipesForHeat(int heat) {
        return getRecipes().stream().filter(r -> r.getHeat() == heat).collect(Collectors.toList());
    }
}
