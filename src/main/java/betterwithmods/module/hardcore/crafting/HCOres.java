package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.registry.bulk.recipes.builder.CrucibleRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
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

        CrucibleRecipeBuilder builder = new CrucibleRecipeBuilder();
        if (customRecipes) {
            RecipeRegistry.CRUCIBLE.registerAll(
                    builder.stoked().disableContainers().inputs(new ItemStack(Items.BUCKET)).outputs(new ItemStack(Items.IRON_NUGGET, 7)).build(),
                    builder.stoked().disableContainers().inputs(new ItemStack(Items.WATER_BUCKET)).outputs(new ItemStack(Items.IRON_NUGGET, 7)).build(),
                    builder.stoked().disableContainers().inputs(new ItemStack(Items.BUCKET)).outputs(new ItemStack(Items.IRON_NUGGET, 7)).build(),
                    builder.stoked().inputs(new ItemStack(Items.MAP)).outputs(new ItemStack(Items.IRON_NUGGET, 4)).build(),
                    builder.stoked().inputs(new ItemStack(Items.COMPASS)).outputs(new ItemStack(Items.IRON_NUGGET, 4)).build(),
                    builder.stoked().inputs(new ItemStack(Blocks.TRIPWIRE_HOOK, 2)).outputs(new ItemStack(Items.IRON_NUGGET)).build(),
                    builder.stoked().inputs(new ItemStack(Items.CLOCK)).outputs(new ItemStack(Items.GOLD_NUGGET, 4)).build()
            );
        } else {
            RecipeRegistry.CRUCIBLE.registerAll(
                    builder.stoked().disableContainers().inputs(new ItemStack(Items.BUCKET)).outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),
                    builder.stoked().disableContainers().inputs(new ItemStack(Items.WATER_BUCKET)).outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),
                    builder.stoked().disableContainers().inputs(new ItemStack(Items.BUCKET)).outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),
                    builder.stoked().inputs(new ItemStack(Items.MAP)).outputs(new ItemStack(Items.IRON_INGOT, 4)).build(),
                    builder.stoked().inputs(new ItemStack(Items.COMPASS)).outputs(new ItemStack(Items.IRON_INGOT, 4)).build(),
                    builder.stoked().inputs(new ItemStack(Blocks.TRIPWIRE_HOOK, 2)).outputs(new ItemStack(Items.IRON_INGOT)).build(),
                    builder.stoked().inputs(new ItemStack(Items.CLOCK)).outputs(new ItemStack(Items.GOLD_INGOT, 4)).build()
            );
        }
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

