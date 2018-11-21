package betterwithmods.module.recipes;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.recipes.builder.CrucibleRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.tweaks.CheaperAxes;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class MetalReclaming extends Feature {
    public static int reclaimCount;

    private static CrucibleRecipeBuilder builder = new CrucibleRecipeBuilder();

    @Deprecated //oreSuffix will not work once oredict is gone
    public static void addReclaimRecipe(Item item, String oreSuffix, int ingotCount) {
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
        RecipeRegistry.CRUCIBLE.register(builder.stoked().inputs(item).outputs(outputs).build());
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

            RecipeRegistry.CRUCIBLE.registerAll(
                    //STEEL
                    builder.stoked()
                            .inputs(BWMItems.STEEL_HOE)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 2)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_SWORD)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 3)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_PICKAXE)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 3)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_AXE)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, axe_amt)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_SHOVEL)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 1)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_MATTOCK)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 4)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_BATTLEAXE)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_HELMET)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 10)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_CHEST)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 14)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_PANTS)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 12)).build(),
                    builder.stoked()
                            .inputs(BWMItems.STEEL_BOOTS)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 8)).build(),
                    builder.stoked()
                            .inputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE))
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)).build(),
                    builder.stoked()
                            .inputs(BWMBlocks.STEEL_ANVIL)
                            .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 7)).build(),

                    //IRON
                    builder.stoked()
                            .inputs(Items.CHAINMAIL_HELMET)
                            .outputs(new ItemStack(Items.IRON_NUGGET, 20)).build(),
                    builder.stoked()
                            .inputs(Items.CHAINMAIL_LEGGINGS)
                            .outputs(new ItemStack(Items.IRON_NUGGET, 32)).build(),
                    builder.stoked()
                            .inputs(Items.CHAINMAIL_CHESTPLATE)
                            .outputs(new ItemStack(Items.IRON_NUGGET, 28)).build(),
                    builder.stoked()
                            .inputs(Items.CHAINMAIL_BOOTS)
                            .outputs(new ItemStack(Items.IRON_NUGGET, 16)).build(),
                    builder.stoked()
                            .inputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHAIN_MAIL))
                            .outputs(new ItemStack(Items.IRON_NUGGET, 4)).build(),
                    builder.stoked()
                            .inputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHAIN_MAIL))
                            .outputs(new ItemStack(Items.IRON_NUGGET, 4)).build(),
                    builder.stoked()
                            .inputs(Items.SHIELD)
                            .outputs(new ItemStack(Items.IRON_INGOT, 1)).build(),
                    builder.stoked()
                            .inputs(Items.IRON_DOOR)
                            .outputs(new ItemStack(Items.IRON_INGOT, 2)).build(),
                    builder.stoked()
                            .inputs(Items.IRON_HORSE_ARMOR)
                            .outputs(new ItemStack(Items.IRON_INGOT, 8)).build(),

                    builder.stoked()
                            .inputs(Items.MINECART)
                            .outputs(new ItemStack(Items.IRON_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(Items.CHEST_MINECART)
                            .outputs(new ItemStack(Items.IRON_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(Items.FURNACE_MINECART)
                            .outputs(new ItemStack(Items.IRON_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(Items.HOPPER_MINECART)
                            .outputs(new ItemStack(Items.IRON_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(Items.TNT_MINECART)
                            .outputs(new ItemStack(Items.IRON_INGOT, 5)).build(),
                    builder.stoked()
                            .inputs(Items.CAULDRON)
                            .outputs(new ItemStack(Items.IRON_INGOT, 7)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.RAIL, 8))
                            .outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.ACTIVATOR_RAIL, 6))
                            .outputs(new ItemStack(Items.IRON_INGOT, 6)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.DETECTOR_RAIL, 6))
                            .outputs(new ItemStack(Items.IRON_INGOT, 6)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(BWMBlocks.BOOSTER, 6))
                            .outputs(new ItemStack(Items.IRON_INGOT, 6)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 6))
                            .outputs(new ItemStack(Items.IRON_INGOT, 2)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.IRON_BARS, 8))
                            .outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),
                    builder.stoked()
                            .inputs(Blocks.ANVIL)
                            .outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),
                    builder.stoked()
                            .inputs(Blocks.HOPPER)
                            .outputs(new ItemStack(Items.IRON_INGOT, 3)).build(),

                    //GOLD
                    builder.stoked()
                            .inputs(Items.GOLDEN_HORSE_ARMOR)
                            .outputs(new ItemStack(Items.GOLD_INGOT, 8)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.RAIL, 8))
                            .outputs(new ItemStack(Items.GOLD_INGOT, 6)).build(),
                    builder.stoked()
                            .inputs(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE))
                            .outputs(new ItemStack(Items.GOLD_INGOT, 2)).build()
            );

            addReclaimRecipe(Items.IRON_CHESTPLATE, "Iron", 8);
            addReclaimRecipe(Items.IRON_AXE, "Iron", axe_amt);
            addReclaimRecipe(Items.IRON_BOOTS, "Iron", 4);
            addReclaimRecipe(Items.IRON_HELMET, "Iron", 5);
            addReclaimRecipe(Items.IRON_LEGGINGS, "Iron", 7);
            addReclaimRecipe(Items.IRON_HOE, "Iron", 2);
            addReclaimRecipe(Items.IRON_PICKAXE, "Iron", 3);
            addReclaimRecipe(Items.IRON_SHOVEL, "Iron", 1);
            addReclaimRecipe(Items.IRON_SWORD, "Iron", 2);

            addReclaimRecipe(Items.GOLDEN_CHESTPLATE, "Gold", 8);
            addReclaimRecipe(Items.GOLDEN_AXE, "Gold", axe_amt);
            addReclaimRecipe(Items.GOLDEN_BOOTS, "Gold", 4);
            addReclaimRecipe(Items.GOLDEN_HELMET, "Gold", 5);
            addReclaimRecipe(Items.GOLDEN_LEGGINGS, "Gold", 7);
            addReclaimRecipe(Items.GOLDEN_HOE, "Gold", 2);
            addReclaimRecipe(Items.GOLDEN_PICKAXE, "Gold", 3);
            addReclaimRecipe(Items.GOLDEN_SHOVEL, "Gold", 1);
            addReclaimRecipe(Items.GOLDEN_SWORD, "Gold", 2);
            addReclaimRecipe(Items.SHEARS, "Iron", 2);



        }
    }
}

