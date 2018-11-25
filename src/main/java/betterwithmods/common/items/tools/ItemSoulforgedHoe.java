package betterwithmods.common.items.tools;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.module.general.InfernalEnchanting;
import betterwithmods.module.internal.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSoulforgedHoe extends ItemHoe {
    public ItemSoulforgedHoe() {
        super(ItemRegistry.SOULFORGED_STEEL);

    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return BWMOreDictionary.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return InfernalEnchanting.canEnchantSteel(enchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
