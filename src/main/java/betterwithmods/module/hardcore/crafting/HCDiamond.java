package betterwithmods.module.hardcore.crafting;

import betterwithmods.BetterWithMods;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.recipes.builder.CrucibleRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.MetalReclaming;
import betterwithmods.module.tweaks.CheaperAxes;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCDiamond extends Feature {
    @Override
    public String getDescription() {
        return "Makes it so diamonds have to be made into an ingot alloy to be used in certain recipes";
    }

    @Override
    public void registerRecipes() {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_axe"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_hoe"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_pickaxe"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_sword"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_shovel"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_helmet"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_chestplate"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_leggings"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_boots"));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        if (BetterWithMods.MODULE_LOADER.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            CrucibleRecipeBuilder builder = new CrucibleRecipeBuilder();
            if (BetterWithMods.MODULE_LOADER.isFeatureEnabled(CheaperAxes.class)) {
                RecipeRegistry.CRUCIBLE.register(builder.stoked().inputs(Ingredient.fromItem(Items.DIAMOND_AXE)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2)).build());
            } else {
                RecipeRegistry.CRUCIBLE.register(builder.stoked().inputs(Ingredient.fromItem(Items.DIAMOND_AXE)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3)).build());
            }

            RecipeRegistry.CRUCIBLE.registerAll(
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_HOE))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_PICKAXE))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_SHOVEL))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 1)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_SWORD))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_HELMET))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_CHESTPLATE))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 8)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_LEGGINGS))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 7)).build(),
                    builder.stoked()
                            .inputs(Ingredient.fromItem(Items.DIAMOND_BOOTS))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 4)).build()
            );

        }
    }


}
