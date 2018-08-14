package betterwithmods.util;

import com.google.common.collect.Lists;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class BannerUtils {

    public static class BannerData implements INBTSerializable<NBTTagCompound> {
        List<PatternColor> patternColors = Lists.newArrayList();

        public BannerData(ItemStack stack) {
            if (stack.getItem() instanceof ItemBanner) {
                NBTTagCompound tag = stack.getSubCompound("BlockEntityTag");
                deserializeNBT(tag);
            }
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();

            NBTTagList list = new NBTTagList();

            for(PatternColor pc: patternColors) {
                list.appendTag(pc.serializeNBT());
            }

            tag.setTag("Patterns,", list);
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound tag) {
            if (tag != null && tag.hasKey("Patterns")) {
                NBTTagList list = tag.getTagList("Patterns", 10);
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound entry = list.getCompoundTagAt(i);
                    EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(entry.getInteger("Color"));
                    BannerPattern bannerpattern = BannerPattern.byHash(entry.getString("Pattern"));
                    patternColors.add(new PatternColor(enumdyecolor, bannerpattern));
                }

            }
        }

        public List<PatternColor> getPatternColors() {
            return patternColors;
        }
    }

    private static class PatternColor implements INBTSerializable<NBTTagCompound> {
        private EnumDyeColor color;
        private BannerPattern pattern;

        public PatternColor(EnumDyeColor color, BannerPattern pattern) {
            this.color = color;
            this.pattern = pattern;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound entry = new NBTTagCompound();

            entry.setInteger("Color", color.getDyeDamage());
            entry.setString("Pattern", pattern.getHashname());
            return null;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            pattern = BannerPattern.byHash(nbt.getString("Pattern"));
            color = EnumDyeColor.byDyeDamage(nbt.getInteger("Color"));

        }
    }

}
