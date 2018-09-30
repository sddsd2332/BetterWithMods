package betterwithmods.module.recipes;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.tweaks.CheaperAxes;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class MetalReclaming extends Feature {
    public static int reclaimCount;

    public static void addReclaimRecipe(ItemStack input, String oreSuffix, int ingotCount) {
        int totalNuggets = ingotCount * reclaimCount;
        int ingots = totalNuggets / 9;
        int nuggets = totalNuggets % 9;
        ItemStack ingotStack = ItemStack.EMPTY;
        ItemStack nuggetStack = ItemStack.EMPTY;
        if (ingots > 0 && !OreDictionary.getOres("ingot" + oreSuffix).isEmpty())
            ingotStack = OreDictionary.getOres("ingot" + oreSuffix).get(0);
        if (nuggets > 0 && !OreDictionary.getOres("nugget" + oreSuffix).isEmpty())
            nuggetStack = OreDictionary.getOres("nugget" + oreSuffix).get(0);
        List<ItemStack> outputs = Lists.newArrayList();
        if (ingotStack.isEmpty()) {
            if (!nuggetStack.isEmpty()) {
                outputs.add(new ItemStack(nuggetStack.getItem(), totalNuggets > nuggets ? totalNuggets : nuggets, nuggetStack.getMetadata()));
            }
        } else {
            outputs.add(new ItemStack(ingotStack.getItem(), ingots, ingotStack.getMetadata()));
            if (!nuggetStack.isEmpty())
                outputs.add(new ItemStack(nuggetStack.getItem(), nuggets, nuggetStack.getMetadata()));
        }
        RecipeRegistry.CRUCIBLE.addStokedRecipe(input, outputs);
    }

    @Override
    public String getDescription() {
        return "Adds recipes to the Crucible to melt metal items back into their component metals";
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        int axe_amt = BetterWithMods.MODULE_LOADER.isFeatureEnabled(CheaperAxes.class) ? 2 : 3;
        reclaimCount = loadProperty("Reclaming Count", 6).setMin(0).setMax(9).setComment("Amount (in nuggets per ingot) tools and armor in the crucible reclaim. Does not affect diamond or soulforged steel ingot reclamation. (Set to 0 to disable reclamation entirely.)").get();

        if (reclaimCount > 0) {
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_HOE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 2));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_SWORD, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 3));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 3));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_AXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, axe_amt));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_MATTOCK, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 4));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_BATTLEAXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 5));
            addReclaimRecipe(new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 8);
            addReclaimRecipe(new ItemStack(Items.IRON_AXE, 1, OreDictionary.WILDCARD_VALUE), "Iron", axe_amt);
            addReclaimRecipe(new ItemStack(Items.IRON_BOOTS, 1, OreDictionary.WILDCARD_VALUE), "Iron", 4);
            addReclaimRecipe(new ItemStack(Items.IRON_HELMET, 1, OreDictionary.WILDCARD_VALUE), "Iron", 5);
            addReclaimRecipe(new ItemStack(Items.IRON_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), "Iron", 7);
            addReclaimRecipe(new ItemStack(Items.IRON_HOE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 2);
            addReclaimRecipe(new ItemStack(Items.IRON_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), "Iron", 3);
            addReclaimRecipe(new ItemStack(Items.IRON_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), "Iron", 1);
            addReclaimRecipe(new ItemStack(Items.IRON_SWORD, 1, OreDictionary.WILDCARD_VALUE), "Iron", 2);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 8);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_AXE, 1, OreDictionary.WILDCARD_VALUE), "Gold", axe_amt);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_BOOTS, 1, OreDictionary.WILDCARD_VALUE), "Gold", 4);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_HELMET, 1, OreDictionary.WILDCARD_VALUE), "Gold", 5);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), "Gold", 7);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_HOE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 2);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), "Gold", 3);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), "Gold", 1);
            addReclaimRecipe(new ItemStack(Items.GOLDEN_SWORD, 1, OreDictionary.WILDCARD_VALUE), "Gold", 2);
            addReclaimRecipe(new ItemStack(Items.SHEARS, 1, OreDictionary.WILDCARD_VALUE), "Iron", 2);

            RecipeRegistry.CRUCIBLE.addStokedRecipe(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 10));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_CHEST, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 14));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_PANTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 12));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.STEEL_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 8));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHAIN_MAIL), new ItemStack(Items.IRON_NUGGET, 4));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CHAINMAIL_HELMET, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_NUGGET, 20));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_NUGGET, 32));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_NUGGET, 28));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CHAINMAIL_BOOTS, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_NUGGET, 16));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.SHIELD, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_INGOT, 1));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.IRON_DOOR), new ItemStack(Items.IRON_INGOT, 2));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.IRON_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_INGOT, 8));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.GOLDEN_HORSE_ARMOR, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.GOLD_INGOT, 8));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.MINECART), new ItemStack(Items.IRON_INGOT, 5));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CHEST_MINECART), new ItemStack(Items.IRON_INGOT, 5));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.FURNACE_MINECART), new ItemStack(Items.IRON_INGOT, 5));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.HOPPER_MINECART), new ItemStack(Items.IRON_INGOT, 5));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.TNT_MINECART), new ItemStack(Items.IRON_INGOT, 5));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.CAULDRON), new ItemStack(Items.IRON_INGOT, 7));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.RAIL, 8), new ItemStack(Items.IRON_INGOT, 3));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.GOLDEN_RAIL, 6), new ItemStack(Items.GOLD_INGOT, 6));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.ACTIVATOR_RAIL, 6), new ItemStack(Items.IRON_INGOT, 6));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.DETECTOR_RAIL, 6), new ItemStack(Items.IRON_INGOT, 6));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMBlocks.BOOSTER, 6), new ItemStack(Items.IRON_INGOT, 6));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.IRON_BARS, 8), new ItemStack(Items.IRON_INGOT, 3));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_INGOT, 31));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.HOPPER, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.IRON_INGOT, 5));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 1), new ItemStack(Items.IRON_INGOT, 2));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 1), new ItemStack(Items.GOLD_INGOT, 2));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMBlocks.STEEL_ANVIL), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 7));
        }
    }
}

