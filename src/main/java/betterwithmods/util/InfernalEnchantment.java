package betterwithmods.util;

import betterwithmods.lib.ReflectionLib;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;

public class InfernalEnchantment extends Enchantment {

    private Enchantment enchantment;
    private int minLevel, maxLevel;
    private int id;

    public InfernalEnchantment(@Nonnull Enchantment enchantment) {
        super(enchantment.getRarity(), enchantment.type, getSlots(enchantment));
        this.enchantment = enchantment;
        this.maxLevel = enchantment.getMaxLevel();
        this.minLevel = enchantment.getMinLevel();
        this.id = Enchantment.getEnchantmentID(enchantment);
    }

    private static EntityEquipmentSlot[] getSlots(Enchantment enchantment) {
        return ReflectionHelper.getPrivateValue(Enchantment.class, enchantment, ReflectionLib.ENCHANTMENT_APPLICIBLE_EQUIPMENT_TYPES);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMinLevel() {
        return minLevel;
    }

    public InfernalEnchantment setMinLevel(int minLevel) {
        this.minLevel = minLevel;
        return this;
    }

    public InfernalEnchantment setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    public int getId() {
        return id;
    }
}
