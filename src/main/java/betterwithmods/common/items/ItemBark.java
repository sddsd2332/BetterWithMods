package betterwithmods.common.items;

import betterwithmods.api.util.IBlockVariants;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWOreDictionary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

import java.util.List;
import java.util.stream.Collectors;

public class ItemBark extends Item {

    public ItemBark() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHasSubtypes(true);
    }

    public static ItemStack fromParentStack(Item bark, ItemStack log, int count) {
        ItemStack stack = new ItemStack(bark, count);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound texture = new NBTTagCompound();
        NBTUtil.writeBlockState(texture, BWMRecipes.getStateFromStack(log));
        tag.setTag("texture", texture);
        stack.setTagCompound(tag);
        return stack;
    }

    public static List<ItemStack> getBarks(int count) {
        return BWOreDictionary.blockVariants.stream().map(b -> b.getVariant(IBlockVariants.EnumBlock.BARK, count)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 25;
    }

    public List<ItemStack> getLogs() {
        return BWOreDictionary.blockVariants.stream().map(b -> b.getVariant(IBlockVariants.EnumBlock.LOG, 1)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        List<ItemStack> logs = getLogs();
        for (ItemStack log : logs) {
            items.add(fromParentStack(this, log, 1));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("texture");
        String type = I18n.translateToLocal("bwm.unknown_bark.name").trim();
        if (tag != null) {
            try {
                IBlockState state = NBTUtil.readBlockState(tag);
                ItemStack block = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
                if (block.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) block.getItem();
                    type = itemBlock.getItemStackDisplayName(block);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return String.format("%s %s", type, I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim());
    }
}
