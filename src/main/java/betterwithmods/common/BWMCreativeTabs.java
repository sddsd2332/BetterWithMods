package betterwithmods.common;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.lib.ModLib;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import betterwithmods.module.recipes.miniblocks.DynblockUtils;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BWMCreativeTabs {

    public static final CreativeTabs ITEMS = new CreativeTabs(ModLib.MODID + ":items") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOODEN_GEAR);
        }
    };

    public static final CreativeTabs BLOCKS = new CreativeTabs(ModLib.MODID + ":blocks") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(BWMBlocks.HAND_CRANK);
        }
    };

    public static final CreativeTabs FOODS = new CreativeTabs(ModLib.MODID + ":foods") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(BWMItems.HEARTY_STEW);
        }
    };

    public static final CreativeTabs MINI_BLOCKS = new CreativeTabs(ModLib.MODID + ":mini_blocks") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.SIDING, Material.WOOD), DynblockUtils.MATERIAL_VARIANTS.get(Material.WOOD).stream().findAny().orElse(null));
        }
    };
}
