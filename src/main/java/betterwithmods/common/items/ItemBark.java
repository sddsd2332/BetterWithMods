package betterwithmods.common.items;

import betterwithmods.library.common.variants.IBlockVariants;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.VariantUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBark extends Item {

    public ItemBark() {
        super();
        this.setHasSubtypes(true);
    }

    public static ItemStack fromParentStack(Item bark, ItemStack log, int count) {
        ItemStack stack = new ItemStack(bark, count);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound texture = new NBTTagCompound();
        NBTUtil.writeBlockState(texture, GlobalUtils.getStateFromStack(log));
        tag.setTag("texture", texture);
        stack.setTagCompound(tag);
        return stack;
    }

    public static List<ItemStack> getBarks(int count) {
        return VariantUtils.BLOCK_VARIANTS.stream().map(b -> b.getStack(IBlockVariants.EnumBlock.BARK, count)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 25;
    }

    public List<ItemStack> getLogs() {
        return VariantUtils.BLOCK_VARIANTS.stream().map(b -> b.getStack(IBlockVariants.EnumBlock.LOG, 1)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            List<ItemStack> logs = getLogs();
            for (ItemStack log : logs) {
                items.add(fromParentStack(this, log, 1));
            }
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("texture");
        ITextComponent type = new TextComponentTranslation("betterwithmods.unknown_bark.name");
        //TODO .name is not longer in 1.13
        ITextComponent bark = new TextComponentTranslation(this.getTranslationKey(stack) + ".name");
        if (tag != null) {
            try {
                IBlockState state = NBTUtil.readBlockState(tag);
                ItemStack block = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
                if (block.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) block.getItem();
                    type = new TextComponentString(itemBlock.getItemStackDisplayName(block));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return type.appendText(" ").appendSibling(bark).getFormattedText();
    }
}
