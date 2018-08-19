package betterwithmods.common.items.tools;

import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class ItemSoulforgedPickaxe extends ItemPickaxe {
    public ItemSoulforgedPickaxe() {
        super(BWMItems.SOULFORGED_STEEL);

    }

    @Override
    public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        int level = super.getHarvestLevel(stack, toolClass, player, blockState);
        if (level == -1 && toolClass != null && getToolClasses(stack).contains(toolClass))
            return this.toolMaterial.getHarvestLevel();
        else
            return level;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        return BWMOreDictionary.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }

    @Nonnull
    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe");
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return HCEnchanting.canEnchantSteel(enchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
