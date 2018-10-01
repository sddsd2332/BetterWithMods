package betterwithmods.module.recipes;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.miniblocks.MiniBlockIngredient;
import betterwithmods.library.utils.ingredient.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Map;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class CauldronRecipes extends Feature {

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        unstoked();
        stoked();
    }

    private void stoked() {


        StackIngredient meat = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre(1, "meatPork"),
                StackIngredient.fromOre(4, "meatBeef"),
                StackIngredient.fromOre(4, "meatMutton"),
                StackIngredient.fromOre(10, "meatRotten")
        ));
        RecipeRegistry.CAULDRON.addStokedRecipe(meat, ItemMaterial.getStack(ItemMaterial.EnumMaterial.TALLOW));

        StackIngredient leather = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromStacks(new ItemStack(Items.LEATHER)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_STRAP, 8)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT, 2)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT, 2)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2)),
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2)),
                StackIngredient.fromOre(2, "book")
        ));
        RecipeRegistry.CAULDRON.addStokedRecipe(leather, ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE));

        StackIngredient wood = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre("logWood"),
                StackIngredient.fromOre(6, "plankWood"),
                StackIngredient.fromIngredient(12, new MiniBlockIngredient("siding", new OreIngredient("plankWood"))),
                StackIngredient.fromIngredient(24, new MiniBlockIngredient("moulding", new OreIngredient("plankWood"))),
                StackIngredient.fromIngredient(48, new MiniBlockIngredient("corner", new OreIngredient("plankWood"))),
                StackIngredient.fromOre(16, "dustWood")
        ));
        RecipeRegistry.CAULDRON.addStokedRecipe(wood, ItemMaterial.getStack(ItemMaterial.EnumMaterial.POTASH));

        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE), Lists.newArrayList(new ItemStack(Items.STRING), new ItemStack(Items.STICK)));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.COMPOSITE_BOW, 1, OreDictionary.WILDCARD_VALUE), Lists.newArrayList(new ItemStack(Items.STRING), new ItemStack(Items.BONE)));

        RecipeRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.TALLOW), ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.POTASH)), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOAP)));

        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 4));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 3));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2));

        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_CHEST, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 4));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_PANTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 3));
        RecipeRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2));

        RecipeRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(
                StackIngredient.fromItem(Items.SUGAR),
                StackIngredient.fromOre(4, "meatRotten"),
                StackIngredient.fromStacks(new ItemStack(Items.DYE, 4, EnumDyeColor.WHITE.getDyeDamage()))
        ), Lists.newArrayList(new ItemStack(BWMItems.KIBBLE, 2)));
    }

    private void unstoked() {
        StackIngredient cord = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre(1, "string"),
                StackIngredient.fromOre(1, "fiberHemp")
        ));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustPotash"), StackIngredient.fromOre(4, "dustHellfire")),
                Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE, 8)));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustHellfire"), new OreIngredient("dustCarbon")), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHERCOAL, 4)));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("foodFlour"), StackIngredient.fromItem(Items.SUGAR)), Lists.newArrayList(new ItemStack(BWMItems.DONUT, 4)));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustHellfire"), StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TALLOW))), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BLASTING_OIL, 2)));

        RecipeRegistry.CAULDRON.addHeatlessRecipe(Lists.newArrayList(StackIngredient.fromOre(8, "dustHellfire")), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE)), BWMHeatRegistry.UNSTOKED_HEAT);

        RecipeRegistry.CAULDRON.addUnstokedRecipe(new OreIngredient("blockCactus"), new ItemStack(Items.DYE, 1, 2));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(cord, new OreIngredient("dustGlowstone"), new OreIngredient("dustRedstone")), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.FILAMENT)));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(cord, new OreIngredient("dustBlaze"), new OreIngredient("dustRedstone")), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ELEMENT)));

        //TODO 1.13 tags?
        StackIngredient bark = StackIngredient.fromStacks(ItemBark.getBarks(8));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER)),
                bark
        ), ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2)),
                bark
        ), ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2));


        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                new OreIngredient("dustSulfur"),
                new OreIngredient("dustSaltpeter"),
                new OreIngredient("dustCarbon")),
                new ItemStack(Items.GUNPOWDER, 2));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                new OreIngredient("gunpowder"),
                cord),
                ItemMaterial.getStack(ItemMaterial.EnumMaterial.FUSE));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromStacks(BlockAesthetic.getStack(BlockAesthetic.Type.CHOPBLOCKBLOOD, 4)),
                new OreIngredient("soap")),
                BlockAesthetic.getStack(BlockAesthetic.Type.CHOPBLOCK, 4));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromStacks(new ItemStack(Blocks.STICKY_PISTON, 4)),
                new OreIngredient("soap")),
                new ItemStack(Blocks.PISTON, 4));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                new OreIngredient("meatFish"),
                StackIngredient.fromItem(Items.MILK_BUCKET),
                StackIngredient.fromStacks(new ItemStack(Items.BOWL, 2))),
                new ItemStack(BWMItems.CHOWDER, 2));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                new OreIngredient("meatChicken"),
                new OreIngredient("cookedCarrot"),
                new OreIngredient("cookedPotato"),
                StackIngredient.fromStacks(new ItemStack(Items.BOWL, 3))),
                new ItemStack(BWMItems.CHICKEN_SOUP, 3));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                new OreIngredient("foodCocoapowder"),
                StackIngredient.fromItem(Items.SUGAR),
                StackIngredient.fromItem(Items.MILK_BUCKET)),
                new ItemStack(BWMItems.CHOCOLATE, 2)
        );

        Ingredient stewMeats = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre("meatPork"),
                StackIngredient.fromOre("meatBeef"),
                StackIngredient.fromOre("meatMutton")
        ));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                stewMeats,
                new OreIngredient("foodFlour"),
                new OreIngredient("cookedCarrot"),
                new OreIngredient("cookedPotato"),
                StackIngredient.fromStacks(new ItemStack(Items.BOWL, 5)),
                StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM, 3))
        ), new ItemStack(BWMItems.HEARTY_STEW, 5));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromItem(Items.MILK_BUCKET),
                StackIngredient.fromItem(Items.BOWL),
                StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM, 3))
        ), new ItemStack(Items.MUSHROOM_STEW));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromItem(Items.BOWL),
                StackIngredient.fromStacks(new ItemStack(Items.BEETROOT, 6))
        ), new ItemStack(Items.BEETROOT_SOUP));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromItem(Items.COOKED_RABBIT),
                new OreIngredient("cookedCarrot"),
                new OreIngredient("cookedPotato"),
                StackIngredient.fromOre("foodFlour"),
                StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM, 3)),
                StackIngredient.fromStacks(new ItemStack(Items.BOWL, 5))
        ), new ItemStack(Items.RABBIT_STEW, 5));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING)),
                StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 1)),
                StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 2)),
                StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 3)),
                StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 4)),
                StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 5)),
                StackIngredient.fromStacks(new ItemStack(Items.NETHER_WART)),
                StackIngredient.fromOre(8, "blockSoulUrn")
        ), new ItemStack(BWMBlocks.BLOOD_SAPLING));

        RecipeRegistry.CAULDRON.addUnstokedRecipe(StackIngredient.fromItem(Items.CHORUS_FRUIT), new ItemStack(Items.CHORUS_FRUIT_POPPED));
        RecipeRegistry.CAULDRON.addUnstokedRecipe(StackIngredient.fromItem(Items.EGG), new ItemStack(BWMItems.BOILED_EGG));

    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        //Add all food recipes
        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();
        for (ItemStack input : furnace.keySet()) {
            if (input != null) {
                if (input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD) {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if (!output.isEmpty()) {
                        RecipeRegistry.CAULDRON.addUnstokedRecipe(input, output);
                    }
                }
            }
        }
    }


    @Override
    public String getDescription() {
        return null;
    }
}

