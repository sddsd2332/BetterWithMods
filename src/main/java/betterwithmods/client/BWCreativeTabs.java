package betterwithmods.client;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BWCreativeTabs {
    public static final CreativeTabs BWTAB = new CreativeTabs(BWMod.MODID + ":creative_tab") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(BWMBlocks.HORIZONTAL_WINDMILL);
        }
    };

    public static final CreativeTabs MINI_BLOCKS = new CreativeTabs(BWMod.MODID + ":mini_block") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.WOODEN_SLAB);
        }
    };
}
