package betterwithmods.module.hardcore.needs;

import betterwithmods.BWMod;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.penalties.ArmorPenalties;
import betterwithmods.module.Feature;
import betterwithmods.util.IngredientMap;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 5/10/17.
 */
public class HCArmor extends Feature {
    public static final IngredientMap<Integer> weights = new IngredientMap<>(0);

    public static ArmorPenalties penalties;

    public static boolean shieldRebalance;

    public static float getWeight(ItemStack stack) {
        if (!BWMod.MODULE_LOADER.isFeatureEnabled(HCArmor.class))
            return 0;
        return weights.get(stack);
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
    public void init(FMLInitializationEvent event) {
        initWeights();
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public void setupConfig() {
        BWRegistry.PENALTY_HANDLERS.add(penalties = new ArmorPenalties());
        shieldRebalance = loadPropBool("Shield Rebalance", "Experimental recipes for rebalacing shields", false);

    }

    @Override
    public String getFeatureDescription() {
        return "Gives Armor weight values that effect movement. Changes Entity armor spawning: Zombies only spawn with Iron armor, Skeletons never wear armor.";
    }
}
