package betterwithmods.module.compat;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.registry.variants.WoodVariant;
import betterwithmods.module.CompatFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static betterwithmods.api.util.IBlockVariants.EnumBlock.BLOCK;
import static betterwithmods.api.util.IBlockVariants.EnumBlock.LOG;

@SuppressWarnings("unused")
public class Harvestcraft extends CompatFeature {
    @GameRegistry.ObjectHolder("harvestcraft:pamcinnamon")
    public static final Block logCinnamon = null;
    @GameRegistry.ObjectHolder("harvestcraft:pammaple")
    public static final Block logMaple = null;
    @GameRegistry.ObjectHolder("harvestcraft:pampaperbark")
    public static final Block logPaperbark = null;

    public Harvestcraft() {
        super("harvestcraft");
    }

    @Override
    public void init(FMLInitializationEvent event) {

        Item corn = getItem(new ResourceLocation(modid, "cornitem"));
        Item cornmeal = getItem(new ResourceLocation(modid, "cornmealitem"));
        Item pepper = getItem(new ResourceLocation(modid, "blackpepperitem"));
        Item peppercorn = getItem(new ResourceLocation(modid, "peppercornitem"));
        Item cocoa = getItem(new ResourceLocation(modid, "cocoapowderitem"));
        Item cinnamon = getItem(new ResourceLocation(modid, "cinnamonitem"));
        Item cinnamonPowder = getItem(new ResourceLocation(modid, "groundcinnamonitem"));
        Item nutmeg = getItem(new ResourceLocation(modid, "nutmegitem"));
        Item nutmegPowder = getItem(new ResourceLocation(modid, "groundnutmegitem"));
        Item curry = getItem(new ResourceLocation(modid, "curryleafitem"));
        Item curryPowder = getItem(new ResourceLocation(modid, "currypowderitem"));
        Item boiledEgg = getItem(new ResourceLocation(modid, "boiledeggitem"));
        Item dough = getItem(new ResourceLocation(modid, "doughitem"));

        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(corn), new ItemStack(cornmeal));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(peppercorn), new ItemStack(pepper));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.DYE, 1, 3), new ItemStack(cocoa));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(cinnamon), new ItemStack(cinnamonPowder));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(nutmeg), new ItemStack(nutmegPowder));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(curry), new ItemStack(curryPowder));

        BWRegistry.CAULDRON.addUnstokedRecipe(new ItemStack(Items.EGG), new ItemStack(boiledEgg));


        BWOreDictionary.blockVariants.add(WoodVariant.builder()
                .addVariant(LOG, new ItemStack(logMaple))
                .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.SPRUCE.getMetadata())));

        BWOreDictionary.blockVariants.add(WoodVariant.builder()
                .addVariant(LOG, new ItemStack(logCinnamon))
                .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.JUNGLE.getMetadata())));

        BWOreDictionary.blockVariants.add(WoodVariant.builder()
                .addVariant(LOG, new ItemStack(logPaperbark))
                .addVariant(BLOCK, new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.JUNGLE.getMetadata())));

    }

}
