package betterwithmods.common.registry;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.utils.ingredient.collections.BaseIngredientMap;
import betterwithmods.library.utils.ingredient.collections.IngredientMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by primetoxinz on 6/29/17.
 */
public class BellowsManager {

    public static final IngredientMap<Float> bellowing = new IngredientMap<>(-1f);

    public static float getWeight(ItemStack stack) {
        return bellowing.findValue(stack);
    }


    public static void init() {
        bellowing.put(Items.GUNPOWDER, 3f);
        bellowing.put(Items.PAPER, 3f);
        bellowing.put(Items.MAP, 3f);
        bellowing.put(Items.FILLED_MAP, 3f);
        bellowing.put(Items.FEATHER, 3f);
        bellowing.put(Items.SUGAR, 3f);
        bellowing.put("foodFlour", 3f);
        bellowing.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GROUND_NETHERRACK), 2f);
        bellowing.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_LEAF), 2f);
        bellowing.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_FIBERS), 2f);
        bellowing.put(new ItemStack(BWMBlocks.HEMP), 2f);
        bellowing.put(Items.BEETROOT_SEEDS, 2f);
        bellowing.put(Items.MELON_SEEDS, 2f);
        bellowing.put(Items.PUMPKIN_SEEDS, 2f);
        bellowing.put(Items.WHEAT_SEEDS, 2f);
        bellowing.put(Items.STRING, 2f);
        bellowing.put(Items.DYE, 2f);
        bellowing.put(Items.WHEAT, 1f);
        bellowing.put(Items.NETHER_WART, 1f);
        bellowing.put(Items.ARROW, 1f);
        bellowing.put(Items.TIPPED_ARROW, 1f);
        bellowing.put(Items.SPECTRAL_ARROW, 1f);
        bellowing.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ELEMENT), 2f);
        bellowing.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.FILAMENT), 2f);
        bellowing.put(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_STRAP), 2f);

        BWMOreDictionary.dustNames.forEach(ore -> bellowing.put(ore, 3f));
    }


}
