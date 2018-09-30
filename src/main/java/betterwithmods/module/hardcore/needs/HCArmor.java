package betterwithmods.module.hardcore.needs;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMRegistry;
import betterwithmods.common.penalties.ArmorPenalties;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.collections.IngredientMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by primetoxinz on 5/10/17.
 */
public class HCArmor extends Feature {
    public static final IngredientMap<Integer> weights = new IngredientMap<>(0);

    public static ArmorPenalties penalties;

    public static boolean shieldRebalance;

    public static float getWeight(ItemStack stack) {
        if (!BetterWithMods.MODULE_LOADER.isFeatureEnabled(HCArmor.class))
            return 0;
        return weights.findValue(stack);
    }

    public static void initWeights() {
        weights.put(Items.CHAINMAIL_HELMET, 3);
        weights.put(Items.CHAINMAIL_CHESTPLATE, 4);
        weights.put(Items.CHAINMAIL_LEGGINGS, 4);
        weights.put(Items.CHAINMAIL_BOOTS, 2);

        weights.put(Items.IRON_HELMET, 5);
        weights.put(Items.IRON_CHESTPLATE, 8);
        weights.put(Items.IRON_LEGGINGS, 7);
        weights.put(Items.IRON_BOOTS, 4);

        weights.put(Items.DIAMOND_HELMET, 5);
        weights.put(Items.DIAMOND_CHESTPLATE, 8);
        weights.put(Items.DIAMOND_LEGGINGS, 7);
        weights.put(Items.DIAMOND_BOOTS, 4);

        weights.put(Items.GOLDEN_HELMET, 5);
        weights.put(Items.GOLDEN_CHESTPLATE, 8);
        weights.put(Items.GOLDEN_LEGGINGS, 7);
        weights.put(Items.GOLDEN_BOOTS, 4);
    }


    @Override
    public void onInit(FMLInitializationEvent event) {
        shieldRebalance = loadProperty("Shield Rebalance", false).setComment("Experimental recipes for rebalacing shields").get();
        BWMRegistry.PENALTY_HANDLERS.add(penalties = new ArmorPenalties(this));
        initWeights();
    }

    @Override
    public String getDescription() {
        return "Gives Armor weight values that effect movement. Changes Entity armor spawning: Zombies only spawn with Iron armor, Skeletons never wear armor.";
    }
}
