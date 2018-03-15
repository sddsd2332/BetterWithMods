package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.util.StackIngredient;
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
    public CauldronRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        unstoked();
        stoked();
    }

    private void stoked() {
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE));

        //Stoked
        Lists.newArrayList(
                StackIngredient.fromOre(1, "meatPork"),
                StackIngredient.fromOre(4, "meatBeef"),
                StackIngredient.fromOre(4, "meatMutton"),
                StackIngredient.fromOre(10, "meatRotten")
        ).forEach(meat -> BWRegistry.CAULDRON.addStokedRecipe(meat, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW)));

        Lists.newArrayList(
                StackIngredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)),
                StackIngredient.fromStacks(8, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP)),
                StackIngredient.fromStacks(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT)),
                StackIngredient.fromStacks(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT)),
                StackIngredient.fromStacks(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT)),
                StackIngredient.fromStacks(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT)),
                StackIngredient.fromStacks(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT)),
                StackIngredient.fromOre(2, "book")
        ).forEach(i -> BWRegistry.CAULDRON.addStokedRecipe(i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE)));

        Lists.newArrayList(
                StackIngredient.fromOre("logWood"),
                StackIngredient.fromOre(6, "plankWood"),
                StackIngredient.fromOre(16, "dustWood")
        ).forEach(i -> BWRegistry.CAULDRON.addStokedRecipe(i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH)));

        BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.TALLOW), ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.POTASH)), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP)));

        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 2));
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 4));
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 3));
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(Items.LEATHER_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 2));

        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 2));
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_CHEST, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 4));
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_PANTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 3));
        BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(BWMItems.LEATHER_TANNED_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 2));


    }

    private void unstoked() {
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustPotash"), StackIngredient.fromOre(4, "dustHellfire")),
                Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE, 8)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustHellfire"), new OreIngredient("dustCarbon")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL, 4)));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("foodFlour"), Ingredient.fromItem(Items.SUGAR)), Lists.newArrayList(new ItemStack(BWMItems.DONUT, 4)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustHellfire"), Ingredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW))), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL, 2)));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromOre(8, "dustHellfire")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE)));

        BWRegistry.CAULDRON.addUnstokedRecipe(new OreIngredient("blockCactus"), new ItemStack(Items.DYE, 1, 2));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("string"), new OreIngredient("dustGlowstone"), new OreIngredient("dustRedstone")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("fiberHemp"), new OreIngredient("dustGlowstone"), new OreIngredient("dustRedstone")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT)));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("string"), new OreIngredient("dustBlaze"), new OreIngredient("dustRedstone")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("fiberHemp"), new OreIngredient("dustBlaze"), new OreIngredient("dustRedstone")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT)));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(Ingredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)), StackIngredient.fromOre(8, "barkBlood")), Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER)));


        Lists.newArrayList(
                StackIngredient.fromOre(5, "barkOak"),
                StackIngredient.fromOre(3, "barkSpruce"),
                StackIngredient.fromOre(2, "barkBirch"),
                StackIngredient.fromOre(4, "barkJungle"),
                StackIngredient.fromOre(8, "barkAcacia"),
                StackIngredient.fromOre(8, "barkDarkOak"),
                StackIngredient.fromOre(8, "barkBlood")
        ).forEach(
                bark -> {
                    BWRegistry.CAULDRON.addUnstokedRecipe(
                            Lists.newArrayList(Ingredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)),
                                    bark), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER));

                    BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                            StackIngredient.fromStacks(2, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)),
                            bark
                    ), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2));
                }
        );


        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("dustSulfur"), new OreIngredient("dustSaltpeter"), new OreIngredient("dustCarbon")), new ItemStack(Items.GUNPOWDER, 2));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("gunpowder"), new OreIngredient("string")), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FUSE));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(Ingredient.fromStacks(BlockAesthetic.getStack(BlockAesthetic.EnumType.CHOPBLOCKBLOOD)), new OreIngredient("soap")), BlockAesthetic.getStack(BlockAesthetic.EnumType.CHOPBLOCKBLOOD));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(4, new ItemStack(Blocks.STICKY_PISTON)), new OreIngredient("soap")), new ItemStack(Blocks.PISTON, 4));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("meatFish"), Ingredient.fromItem(Items.MILK_BUCKET), StackIngredient.fromStacks(2, new ItemStack(Items.BOWL))), new ItemStack(BWMItems.CHOWDER, 2));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(Ingredient.fromItem(Items.CARROT), Ingredient.fromItem(Items.COOKED_CHICKEN), Ingredient.fromItem(Items.BAKED_POTATO), StackIngredient.fromStacks(3, new ItemStack(Items.BOWL))), new ItemStack(BWMItems.CHICKEN_SOUP, 3));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(new OreIngredient("foodCocoapowder"), Ingredient.fromItem(Items.SUGAR), Ingredient.fromItem(Items.MILK_BUCKET)), new ItemStack(BWMItems.CHOCOLATE, 2));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                new OreIngredient("listAllmeatcooked"),
                new OreIngredient("foodFlour"),
                Ingredient.fromItem(Items.CARROT),
                Ingredient.fromItem(Items.BAKED_POTATO),
                StackIngredient.fromStacks(5, new ItemStack(Items.BOWL)),
                StackIngredient.fromStacks(3, new ItemStack(Blocks.BROWN_MUSHROOM))
        ), new ItemStack(BWMItems.HEARTY_STEW, 5));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                Ingredient.fromItem(Items.MILK_BUCKET),
                Ingredient.fromItem(Items.BOWL),
                StackIngredient.fromStacks(3, new ItemStack(Blocks.BROWN_MUSHROOM))
        ), new ItemStack(Items.MUSHROOM_STEW));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                Ingredient.fromItem(Items.SUGAR),
                StackIngredient.fromOre(4, "meatRotten"),
                StackIngredient.fromStacks(4, new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()))
        ), new ItemStack(BWMItems.KIBBLE, 2));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                Ingredient.fromItem(Items.BOWL),
                StackIngredient.fromStacks(6, new ItemStack(Items.BEETROOT))
        ), new ItemStack(Items.BEETROOT_SOUP));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                Ingredient.fromItem(Items.COOKED_RABBIT),
                Ingredient.fromItem(Items.CARROT),
                Ingredient.fromItem(Items.BAKED_POTATO),
                StackIngredient.fromOre("foodFlour"),
                StackIngredient.fromStacks(3, new ItemStack(Blocks.RED_MUSHROOM)),
                StackIngredient.fromStacks(5, new ItemStack(Items.BOWL))
        ), new ItemStack(Items.RABBIT_STEW, 5));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(
                Ingredient.fromStacks(new ItemStack(Blocks.SAPLING)),
                Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 1)),
                Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 2)),
                Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 3)),
                Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 4)),
                Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 5)),
                Ingredient.fromStacks(new ItemStack(Items.NETHER_WART)),
                StackIngredient.fromOre(8, "blockSoulUrn")
        ), new ItemStack(BWMBlocks.BLOOD_SAPLING));

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        //Add all food recipes
        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();
        for (ItemStack input : furnace.keySet()) {
            if (input != null) {
                if (input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD) {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if (!output.isEmpty()) {
//                        CauldronRecipes.addCauldronRecipe(new CauldronFoodRecipe(output.copy(), new Object[]{input.copy()}));
                    }
                }
            }
        }
    }

}

