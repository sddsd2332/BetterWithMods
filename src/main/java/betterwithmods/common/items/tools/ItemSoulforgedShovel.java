package betterwithmods.common.items.tools;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import betterwithmods.module.internal.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class ItemSoulforgedShovel extends ItemSpade {
    public ItemSoulforgedShovel() {
        super(ItemRegistry.SOULFORGED_STEEL);

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
