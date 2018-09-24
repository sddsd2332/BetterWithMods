package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWMRegistry;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.StackIngredient;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
        return "Makes Ores only smelt into a single nugget, making it much harder to create large amounts of metal";
    }
    @Override
    public void onInit(FMLInitializationEvent event) {

        oreNuggetSmelting = loadProperty("Ore to Nugget Smelting", true).setComment("Make Ores (oredict ore.* )smelt into nuggets instead of ingots").get();

        oreExclude = Arrays.stream(loadProperty("Ore Exclude", new String[0]).setComment(  "Oredictionary entries to exclude from ore to nugget smelting. Remove the prefix of the oredictionary. example 'oreIron' would be just 'iron' ").get()).collect(Collectors.toSet());
        dustExclude = Arrays.stream(loadProperty("Dust Exclude",new String[0]).setComment("Oredictionary entries to exclude from dust to nugget smelting  Remove the prefix of the oredictionary. example 'dustIron' would be just 'iron'").get()).collect(Collectors.toSet());

        dustNuggetSmelting = loadProperty("Dust to Nugget Smelting", true).setComment( "Make Dusts ( oredict dust.* ) smelt into nuggets instead of ingots").get();
        boolean fixVanillaRecipes = loadProperty("Fix Vanilla Recipes", true).setComment("Make certain recipes cheaper to be more reasonable with nugget smelting, including Compass, Clock, and Bucket").get();

        oreProductionCount = loadProperty("Ore Production Count", 1).setComment("Number of Materials returned from Smelting an Ore").get();
        dustProductionCount = loadProperty("Dust Production Count", 1).setComment("Number of Materials returned from Smelting a Dust").get();


        if (fixVanillaRecipes) {

            BWMRecipes.removeRecipe(Items.COMPASS.getRegistryName());
            BWMRecipes.removeRecipe(Items.CLOCK.getRegistryName());
            BWMRecipes.removeRecipe(Items.BUCKET.getRegistryName());
            BWMRecipes.removeRecipe(Items.FLINT_AND_STEEL.getRegistryName());

            //TODO
//            addHardcoreRecipe(new ShapedOreRecipe(null, Items.COMPASS, " N ", "NRN", " N ", 'N', "nuggetIron", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "compass")));
//            addHardcoreRecipe(new ShapedOreRecipe(null, Items.CLOCK, " N ", "NQN", " N ", 'N', "nuggetGold", 'Q', "gemQuartz").setRegistryName(new ResourceLocation("minecraft", "clock")));
//            addHardcoreRecipe(new ShapedOreRecipe(null, Items.BUCKET, "N N", "N N", "NNN", 'N', "nuggetIron").setRegistryName(new ResourceLocation("minecraft", "bucket")));
//            addHardcoreRecipe(new ShapelessOreRecipe(null, Items.FLINT_AND_STEEL, Items.FLINT, "nuggetIron").setRegistryName(new ResourceLocation("minecraft", "flint_and_steel")));
        }

        addMeltingRecipeWithoutReturn(new ItemStack(Items.BUCKET), new ItemStack(Items.IRON_NUGGET, 7));
        addMeltingRecipeWithoutReturn(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.IRON_NUGGET, 7));
        addMeltingRecipeWithoutReturn(new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.IRON_NUGGET, 7));
        BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.MAP), new ItemStack(Items.IRON_NUGGET, 4));
        BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.COMPASS), new ItemStack(Items.IRON_NUGGET, 4));
        BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.TRIPWIRE_HOOK, 2), new ItemStack(Items.IRON_NUGGET));

        BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CLOCK), new ItemStack(Items.GOLD_NUGGET, 4));
    }

    private void addMeltingRecipeWithoutReturn(ItemStack input, ItemStack output) {
        BWMRegistry.CRUCIBLE.addRecipe(new CookingPotRecipe(Lists.newArrayList(StackIngredient.fromStacks(input)), Lists.newArrayList(output), BWMHeatRegistry.STOKED_HEAT) {
            //TODO
//            @Override
//            protected boolean consumeIngredients(ItemStackHandler inventory, NonNullList<ItemStack> containItems) {
//                boolean success = super.consumeIngredients(inventory, containItems);
//                containItems.clear();
//                return success;
//            }
        });
    }
//TODO
//    @Override
//    public void disabledInit(FMLInitializationEvent event) {
//        addMeltingRecipeWithoutReturn(new ItemStack(Items.BUCKET), new ItemStack(Items.IRON_INGOT, 3));
//        addMeltingRecipeWithoutReturn(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.IRON_INGOT, 3));
//        addMeltingRecipeWithoutReturn(new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.IRON_INGOT, 3));
//        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.MAP), new ItemStack(Items.IRON_INGOT, 4));
//        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.COMPASS), new ItemStack(Items.IRON_INGOT, 4));
//        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.TRIPWIRE_HOOK, 2), new ItemStack(Items.IRON_INGOT));
//        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CLOCK), new ItemStack(Items.GOLD_INGOT, 4));
//    }
//


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
                    if (BWMRecipes.removeFurnaceRecipe(oreStack)) {
                        ItemStack nugget = optionalNugget.get().copy();
                        nugget.setCount(oreProductionCount);
                        BWMRecipes.addFurnaceRecipe(oreStack, nugget);
                    }
                }
            }
        }
    }

}
