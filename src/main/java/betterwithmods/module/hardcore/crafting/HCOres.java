package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCOres extends Feature {

    private static boolean oreNuggetSmelting;
    private static boolean dustNuggetSmelting;
    private static Set<String> oreExclude, dustExclude;
    private static int oreProductionCount, dustProductionCount;

    public HCOres() {
    }


    @Override
    public String getDescription() {
        return "Makes Ores only smelt into a single nugget, making it much harder to createBlock large amounts of metal";
    }

    @Override
    public void registerRecipes() {
        if (customRecipes) {
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.COMPASS.getRegistryName()));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.CLOCK.getRegistryName()));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.BUCKET.getRegistryName()));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.FLINT_AND_STEEL.getRegistryName()));
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        oreNuggetSmelting = loadProperty("Ore to Nugget Smelting", true).setComment("Make Ores (oredict ore.* )smelt into nuggets instead of ingots").get();

        oreExclude = Arrays.stream(loadProperty("Ore Exclude", new String[0]).setComment("Oredictionary entries to exclude from ore to nugget smelting. Remove the prefix of the oredictionary. example 'oreIron' would be just 'iron' ").get()).collect(Collectors.toSet());
        dustExclude = Arrays.stream(loadProperty("Dust Exclude", new String[0]).setComment("Oredictionary entries to exclude from dust to nugget smelting  Remove the prefix of the oredictionary. example 'dustIron' would be just 'iron'").get()).collect(Collectors.toSet());

        dustNuggetSmelting = loadProperty("Dust to Nugget Smelting", true).setComment("Make Dusts ( oredict dust.* ) smelt into nuggets instead of ingots").get();

        oreProductionCount = loadProperty("Ore Production Count", 1).setComment("Number of Materials returned from Smelting an Ore").get();
        dustProductionCount = loadProperty("Dust Production Count", 1).setComment("Number of Materials returned from Smelting a Dust").get();

        if (customRecipes) {
            addMeltingRecipeWithoutReturn(new ItemStack(Items.BUCKET), new ItemStack(Items.IRON_NUGGET, 7));
            addMeltingRecipeWithoutReturn(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.IRON_NUGGET, 7));
            addMeltingRecipeWithoutReturn(new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.IRON_NUGGET, 7));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.MAP), new ItemStack(Items.IRON_NUGGET, 4));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.COMPASS), new ItemStack(Items.IRON_NUGGET, 4));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.TRIPWIRE_HOOK, 2), new ItemStack(Items.IRON_NUGGET));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CLOCK), new ItemStack(Items.GOLD_NUGGET, 4));
        } else {
            addMeltingRecipeWithoutReturn(new ItemStack(Items.BUCKET), new ItemStack(Items.IRON_INGOT, 3));
            addMeltingRecipeWithoutReturn(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.IRON_INGOT, 3));
            addMeltingRecipeWithoutReturn(new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.IRON_INGOT, 3));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.MAP), new ItemStack(Items.IRON_INGOT, 4));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.COMPASS), new ItemStack(Items.IRON_INGOT, 4));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.TRIPWIRE_HOOK, 2), new ItemStack(Items.IRON_INGOT));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CLOCK), new ItemStack(Items.GOLD_INGOT, 4));
        }

    }

    private void addMeltingRecipeWithoutReturn(ItemStack input, ItemStack output) {
        RecipeRegistry.CRUCIBLE.register(new CrucibleRecipe(Lists.newArrayList(StackIngredient.fromStacks(input)), Lists.newArrayList(output), BWMHeatRegistry.STOKED_HEAT) {
            //TODO
//            @Override
//            protected boolean consumeIngredients(ItemStackHandler inventory, NonNullList<ItemStack> containItems) {
//                boolean success = super.consumeIngredients(inventory, containItems);
//                containItems.clear();
//                return success;
//            }
        });
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        Set<String> oreExcludes = Sets.union(oreExclude, Sets.newHashSet("oreDiamond"));
        if (oreNuggetSmelting) {
            for (BWMOreDictionary.Ore ore : BWMOreDictionary.oreNames) {
                replaceRecipe(oreExcludes, ore, oreProductionCount);
            }
        }
        Set<String> dustExcludes = Sets.union(dustExclude, Sets.newHashSet("dustDiamond"));
        if (dustNuggetSmelting) {
            for (BWMOreDictionary.Ore dust : BWMOreDictionary.dustNames) {
                replaceRecipe(dustExcludes, dust, dustProductionCount);
            }
        }
    }

    private void replaceRecipe(Set<String> oreExcludes, BWMOreDictionary.Ore ore, int oreProductionCount) {
        if (!oreExcludes.contains(ore.getOre())) {
            Optional<ItemStack> optionalNugget = BWMOreDictionary.nuggetNames.stream().filter(o -> o.getSuffix().equals(ore.getSuffix())).flatMap(o -> o.getOres().stream()).findFirst();
            if (optionalNugget.isPresent()) {
                for (ItemStack oreStack : ore.getOres()) {
                    if (RecipeRegistry.removeFurnaceRecipe(oreStack)) {
                        ItemStack nugget = optionalNugget.get().copy();
                        nugget.setCount(oreProductionCount);
                        //TODO likely changes in 1.13
                        FurnaceRecipes.instance().getSmeltingList().put(oreStack, nugget);

                    }
                }
            }
        }
    }

}
