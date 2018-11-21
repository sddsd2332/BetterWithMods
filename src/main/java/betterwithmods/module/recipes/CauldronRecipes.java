package betterwithmods.module.recipes;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.builder.CauldronRecipeBuilder;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import betterwithmods.library.utils.colors.ColorUtils;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.miniblocks.MiniBlockIngredient;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Map;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class CauldronRecipes extends RequiredFeature {

    @Override
    public void onInit(FMLInitializationEvent event) {
        unstoked();
        stoked();
    }

    @SubscribeEvent
    public void onCauldronRecipes(RegistryEvent.Register<CauldronRecipe> event) {
        System.out.println("WOAH!");
    }

    private void stoked() {

        CauldronRecipeBuilder builder = new CauldronRecipeBuilder();
        StackIngredient meat = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre(1, "meatPork"),
                StackIngredient.fromOre(4, "meatBeef"),
                StackIngredient.fromOre(4, "meatMutton"),
                StackIngredient.fromOre(10, "meatRotten")
        ));

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


        StackIngredient wood = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre("logWood"),
                StackIngredient.fromOre(6, "plankWood"),
                StackIngredient.fromIngredient(12, new MiniBlockIngredient("siding", new OreIngredient("plankWood"))),
                StackIngredient.fromIngredient(24, new MiniBlockIngredient("moulding", new OreIngredient("plankWood"))),
                StackIngredient.fromIngredient(48, new MiniBlockIngredient("corner", new OreIngredient("plankWood"))),
                StackIngredient.fromOre(16, "dustWood")
        ));


        RecipeRegistry.CAULDRON.registerAll(
                builder.stoked().inputs(meat).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TALLOW)).build(),
                builder.stoked().inputs(leather).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE)).build(),
                builder.stoked().inputs(wood).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.POTASH)).build(),
                builder.stoked().inputs(Ingredient.fromItem(Items.BOW)).outputs(new ItemStack(Items.STRING), new ItemStack(Items.STICK)).build(),
                builder.stoked().inputs(Ingredient.fromItem(BWMItems.COMPOSITE_BOW)).outputs(new ItemStack(Items.STRING), new ItemStack(Items.BONE)).build(),
                builder.stoked().inputs(ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.TALLOW), ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.POTASH)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOAP)).build(),

                builder.stoked().inputs(Ingredient.fromItem(Items.LEATHER_HELMET)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2)).build(),
                builder.stoked().inputs(Ingredient.fromItem(Items.LEATHER_CHESTPLATE)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 4)).build(),
                builder.stoked().inputs(Ingredient.fromItem(Items.LEATHER_LEGGINGS)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 3)).build(),
                builder.stoked().inputs(Ingredient.fromItem(Items.LEATHER_BOOTS)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2)).build(),

                builder.stoked().inputs(Ingredient.fromItem(BWMItems.LEATHER_TANNED_HELMET)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2)).build(),
                builder.stoked().inputs(Ingredient.fromItem(BWMItems.LEATHER_TANNED_CHEST)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 4)).build(),
                builder.stoked().inputs(Ingredient.fromItem(BWMItems.LEATHER_TANNED_PANTS)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 3)).build(),
                builder.stoked().inputs(Ingredient.fromItem(BWMItems.LEATHER_TANNED_BOOTS)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2)).build(),

                builder.stoked().inputs(
                        StackIngredient.fromItem(Items.SUGAR),
                        StackIngredient.fromOre(4, "meatRotten"),
                        StackIngredient.fromStacks(new ItemStack(Items.DYE, 4, EnumDyeColor.WHITE.getDyeDamage()))
                ).outputs(new ItemStack(BWMItems.KIBBLE, 2)).build()
        );
    }

    private void unstoked() {
        StackIngredient cord = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre(1, "string"),
                StackIngredient.fromOre(1, "fiberHemp")
        ));

        Ingredient stewMeats = StackIngredient.mergeStacked(Lists.newArrayList(
                StackIngredient.fromOre("meatPork"),
                StackIngredient.fromOre("meatBeef"),
                StackIngredient.fromOre("meatMutton")
        ));

        StackIngredient bark = StackIngredient.fromStacks(ItemBark.getBarks(8));

        CauldronRecipeBuilder builder = new CauldronRecipeBuilder();


        builder.unstoked().inputs().outputs().build();

        RecipeRegistry.CAULDRON.registerAll(
                builder.unstoked().ignoreHeat().inputs(new OreIngredient("dustPotash"), StackIngredient.fromOre(4, "dustHellfire")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE, 8)).build(),
                builder.unstoked().ignoreHeat().inputs(new OreIngredient("dustHellfire"), new OreIngredient("dustCarbon")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHERCOAL, 4)).build(),
                builder.unstoked().ignoreHeat().inputs(new OreIngredient("dustHellfire"), StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TALLOW))).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BLASTING_OIL, 2)).build(),
                builder.unstoked().inputs(new OreIngredient("foodFlour"), StackIngredient.fromItem(Items.SUGAR)).outputs(new ItemStack(BWMItems.DONUT, 4)).build(),
                builder.unstoked().ignoreHeat().inputs(StackIngredient.fromOre(8, "dustHellfire")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE)).build(),
                builder.unstoked().inputs(new OreIngredient("blockCactus")).outputs(ColorUtils.getDye(EnumDyeColor.GREEN, 1)).build(),
                builder.unstoked().inputs(
                        cord,
                        new OreIngredient("dustGlowstone"),
                        new OreIngredient("dustRedstone")
                ).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.FILAMENT)).build(),
                builder.unstoked().inputs(
                        cord,
                        new OreIngredient("dustBlaze"),
                        new OreIngredient("dustRedstone"))
                        .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ELEMENT)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER)),
                        bark
                ).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2)),
                        bark
                ).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2)).build(),
                builder.unstoked().inputs(
                        new OreIngredient("dustSulfur"),
                        new OreIngredient("dustSaltpeter"),
                        new OreIngredient("dustCarbon")
                ).outputs(new ItemStack(Items.GUNPOWDER, 2)).build(),
                builder.unstoked().inputs(
                        new OreIngredient("gunpowder"),
                        cord
                ).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.FUSE)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromStacks(BlockAesthetic.getStack(BlockAesthetic.Type.CHOPBLOCKBLOOD, 4)),
                        new OreIngredient("soap")
                ).outputs(BlockAesthetic.getStack(BlockAesthetic.Type.CHOPBLOCK, 4)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromStacks(new ItemStack(Blocks.STICKY_PISTON, 4)),
                        new OreIngredient("soap")
                ).outputs(new ItemStack(Blocks.PISTON, 4)).build(),
                builder.unstoked().inputs(
                        new OreIngredient("meatFish"),
                        StackIngredient.fromItem(Items.MILK_BUCKET),
                        StackIngredient.fromStacks(new ItemStack(Items.BOWL, 2))
                ).outputs(new ItemStack(BWMItems.CHOWDER, 2)).build(),
                builder.unstoked().inputs(
                        new OreIngredient("meatChicken"),
                        new OreIngredient("cookedCarrot"),
                        new OreIngredient("cookedPotato"),
                        StackIngredient.fromStacks(new ItemStack(Items.BOWL, 3))
                ).outputs(new ItemStack(BWMItems.CHICKEN_SOUP, 3)).build(),
                builder.unstoked().inputs(
                        new OreIngredient("foodCocoapowder"),
                        StackIngredient.fromItem(Items.SUGAR),
                        StackIngredient.fromItem(Items.MILK_BUCKET)
                ).outputs(new ItemStack(BWMItems.CHOCOLATE, 2)).build(),
                builder.unstoked().inputs(
                        stewMeats,
                        new OreIngredient("foodFlour"),
                        new OreIngredient("cookedCarrot"),
                        new OreIngredient("cookedPotato"),
                        StackIngredient.fromStacks(new ItemStack(Items.BOWL, 5)),
                        StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM, 3))
                ).outputs(new ItemStack(BWMItems.HEARTY_STEW, 5)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromItem(Items.MILK_BUCKET),
                        StackIngredient.fromItem(Items.BOWL),
                        StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM, 3))

                ).outputs(new ItemStack(Items.MUSHROOM_STEW)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromItem(Items.BOWL),
                        StackIngredient.fromStacks(new ItemStack(Items.BEETROOT, 6))
                ).outputs(new ItemStack(Items.BEETROOT_SOUP)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromItem(Items.COOKED_RABBIT),
                        new OreIngredient("cookedCarrot"),
                        new OreIngredient("cookedPotato"),
                        StackIngredient.fromOre("foodFlour"),
                        StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM, 3)),
                        StackIngredient.fromStacks(new ItemStack(Items.BOWL, 5))
                ).outputs(new ItemStack(Items.RABBIT_STEW, 5)).build(),
                builder.unstoked().inputs(
                        StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING)),
                        StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 1)),
                        StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 2)),
                        StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 3)),
                        StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 4)),
                        StackIngredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 5)),
                        StackIngredient.fromStacks(new ItemStack(Items.NETHER_WART)),
                        StackIngredient.fromOre(8, "blockSoulUrn")
                ).outputs(new ItemStack(BWMBlocks.BLOOD_SAPLING)).build(),
                builder.unstoked().inputs(StackIngredient.fromItem(Items.CHORUS_FRUIT)).outputs(new ItemStack(Items.CHORUS_FRUIT_POPPED)).build(),
                builder.unstoked().inputs(StackIngredient.fromItem(Items.EGG)).outputs(new ItemStack(BWMItems.BOILED_EGG)).build()

        );


    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        //Add all food recipes
        CauldronRecipeBuilder builder = new CauldronRecipeBuilder();

        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();
        for (ItemStack input : furnace.keySet()) {
            if (input != null) {
                if (input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD) {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if (!output.isEmpty()) {
                        RecipeRegistry.CAULDRON.register(builder.unstoked().inputs(input).outputs(output).build());
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

