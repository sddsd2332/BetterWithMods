package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class ItemSoulforgedBattleAxe extends ItemAxe {
    public ItemSoulforgedBattleAxe() {
        super(BWMItems.SOULFORGED_STEEL, 9F, -2.4f);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        return BWOreDictionary.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (HCEnchanting.canEnchantSteel(enchantment)) {
            EnumEnchantmentType type = enchantment.type;
            return type == EnumEnchantmentType.WEAPON || type == EnumEnchantmentType.DIGGER;
        }
        return false;

    }
}
