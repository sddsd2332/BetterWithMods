package betterwithmods.common.items.tools;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import betterwithmods.module.internal.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class ItemSoulforgedAxe extends ItemAxe {
    public ItemSoulforgedAxe() {
        super(ItemRegistry.SOULFORGED_STEEL, 8.0F, -3.0F);

    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        return BWMOreDictionary.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return HCEnchanting.canEnchantSteel(enchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
