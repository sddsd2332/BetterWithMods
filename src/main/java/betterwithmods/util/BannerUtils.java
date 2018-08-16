package betterwithmods.util;

import betterwithmods.BWMod;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.stream.Collectors;

public class BannerUtils {

    public static final BannerTextures.Cache HORIZONTAL_WINDMILL = new BannerTextures.Cache("betterwithmods:H", new ResourceLocation(BWMod.MODID, "textures/blocks/horizontal_windmill_banner.png"), "betterwithmods:textures/blocks/horizontal_windmill/");
    public static final BannerTextures.Cache VERTICAL_WINDMILL = new BannerTextures.Cache("betterwithmods:V", new ResourceLocation(BWMod.MODID, "textures/blocks/vertical_windmill_banner.png"), "betterwithmods:textures/blocks/vertical_windmill/");

    public static void readArray(BannerData[] array, NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("Banners", 10);
        for (int i = 0; i < array.length; i++) {
            if (list.tagCount() > i) {
                BannerData data = new BannerData();
                data.deserializeNBT(list.getCompoundTagAt(i));
                array[i] = data;
            }
        }
    }

    public static void writeArray(BannerData[] banners, NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for (BannerData data : banners) {
            if (data != null)
                list.appendTag(data.serializeNBT());
        }
        tag.setTag("Banners", list);
    }

    public static BannerData fromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemBanner) {
            return new BannerData(stack);
        }
        return null;
    }

    public static BannerData fromPatternColors(PatternColor... data) {
        return new BannerData(data);
    }

    public static class BannerData implements INBTSerializable<NBTTagCompound> {
        //initialize list with base
        private PatternColor base = new PatternColor(BannerPattern.BASE, EnumDyeColor.WHITE);

        List<PatternColor> patternColors = Lists.newArrayList();

        private BannerData(PatternColor... patternColors) {
            this.patternColors.addAll(Lists.newArrayList(patternColors));
        }

        private BannerData(ItemStack stack) {
            if (stack.getItem() instanceof ItemBanner) {
                base = new PatternColor(BannerPattern.BASE, EnumDyeColor.byDyeDamage(stack.getMetadata()));
                NBTTagCompound tag = stack.getSubCompound("BlockEntityTag");
                if(tag != null) {
                    deserializeNBT(tag);
                }
            }
        }

        private BannerData() { }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();

            NBTTagList list = new NBTTagList();

            for (PatternColor pc : patternColors) {
                list.appendTag(pc.serializeNBT());
            }
            tag.setTag("Base", base.serializeNBT());
            tag.setTag("Patterns", list);
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound tag) {
            if (tag != null && tag.hasKey("Patterns")) {
                if(tag.hasKey("Base")) {
                    base.deserializeNBT(tag.getCompoundTag("Base"));
                }
                NBTTagList list = tag.getTagList("Patterns", 10);
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound e = list.getCompoundTagAt(i);
                    PatternColor pc = new PatternColor();
                    pc.deserializeNBT(e);
                    patternColors.add(pc);
                }
            }
        }

        private List<PatternColor> getPatternColors() {
            List<PatternColor> l = Lists.newArrayList(base);
            l.addAll(patternColors);
            return l;
        }

        private List<BannerPattern> getPatternList() {
            return getPatternColors().stream().map(PatternColor::getPattern).collect(Collectors.toList());
        }

        private List<EnumDyeColor> getColorList() {
            return getPatternColors().stream().map(PatternColor::getColor).collect(Collectors.toList());
        }

        public ResourceLocation getTexture(BannerTextures.Cache cache) {
            return cache.getResourceLocation(getPatternID(), getPatternList(), getColorList());
        }

        private String getPatternID() {
            StringBuilder builder = new StringBuilder();
            for (PatternColor pc : getPatternColors()) {
                builder.append(pc.pattern.getHashname()).append(pc.color.getDyeDamage());
            }
            return builder.toString();
        }
    }

    public static class PatternColor implements INBTSerializable<NBTTagCompound> {
        private EnumDyeColor color;
        private BannerPattern pattern;

        private PatternColor(BannerPattern pattern, EnumDyeColor color) {
            this.pattern = pattern;
            this.color = color;
        }

        private PatternColor() {
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setInteger("Color", color.getDyeDamage());
            entry.setString("Pattern", pattern.getHashname());
            return entry;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            pattern = BannerPattern.byHash(nbt.getString("Pattern"));
            color = EnumDyeColor.byDyeDamage(nbt.getInteger("Color"));

        }

        public EnumDyeColor getColor() {
            return color;
        }

        public BannerPattern getPattern() {
            return pattern;
        }
    }

}
