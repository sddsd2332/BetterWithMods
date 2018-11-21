package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.block.recipe.KilnRecipe;
import betterwithmods.lib.ModLib;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

public class CrafingManagerKiln extends CraftingManagerBlock<KilnRecipe> {

    public CrafingManagerKiln() {
        super(new ResourceLocation(ModLib.MODID, "kiln"), KilnRecipe.class);
    }

    public List<KilnRecipe> getRecipesForHeat(int heat) {
        return getDisplayRecipes().stream().filter(r -> r.getHeat() == heat).collect(Collectors.toList());
    }
}
