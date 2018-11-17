package betterwithmods.common.blocks;

import betterwithmods.library.common.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockColored extends BlockBase {
    protected EnumDyeColor color;

    public BlockColored(Material material, EnumDyeColor color) {
        super(material);
        this.color = color;
    }

    public static Block getBlock(ResourceLocation base, EnumDyeColor color) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(base.getNamespace(), base.getPath() + "_" + color.getName()));
    }
}
