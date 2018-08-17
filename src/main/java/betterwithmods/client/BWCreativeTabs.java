package betterwithmods.client;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.gameplay.miniblocks.MiniBlocks;
import betterwithmods.module.gameplay.miniblocks.MiniType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BWCreativeTabs {
    public static final CreativeTabs ITEMS = new CreativeTabs(BWMod.MODID + ":items") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOOD_GEAR);
        }
    };

    public static final CreativeTabs BLOCKS = new CreativeTabs(BWMod.MODID + ":blocks") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(BWMBlocks.HAND_CRANK);
        }
    };

    public static final CreativeTabs FOODS = new CreativeTabs(BWMod.MODID + ":foods") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(BWMItems.HEARTY_STEW);
        }
    };

    public static final CreativeTabs MINI_BLOCKS = new CreativeTabs(BWMod.MODID + ":mini_blocks") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return MiniBlocks.fromParent(MiniBlocks.MINI_MATERIAL_BLOCKS.get(MiniType.SIDING).get(Material.WOOD), MiniBlocks.MATERIALS.get(Material.WOOD).stream().findAny().orElse(null));
        }
    };
}
