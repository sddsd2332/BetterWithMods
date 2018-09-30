package betterwithmods.module.recipes;

import betterwithmods.api.recipe.output.impl.RandomCountOutputs;
import betterwithmods.api.recipe.output.impl.RandomOutput;
import betterwithmods.api.util.IBlockVariants;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.BWMRecipes;
import betterwithmods.library.recipes.RecipeMatchers;
import betterwithmods.library.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import betterwithmods.common.registry.crafting.ChoppingRecipe;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class SawRecipes extends Feature {

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {


        RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(Blocks.PUMPKIN, 0, OreDictionary.WILDCARD_VALUE));
        RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(Blocks.VINE));
        RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(Blocks.YELLOW_FLOWER));
        RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(Blocks.BROWN_MUSHROOM));
        RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(Blocks.RED_MUSHROOM));
        RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(BWMBlocks.ROPE));
        for (int i = 0; i < 9; i++)
            RecipeRegistry.WOOD_SAW.addSelfdropRecipe(new ItemStack(Blocks.RED_FLOWER, 1, i));

        RecipeRegistry.WOOD_SAW.addRecipe(new SawRecipe(new BlockStateIngredient(new ItemStack(Blocks.MELON_BLOCK)), new RandomCountOutputs(new RandomOutput(new ItemStack(Items.MELON), 3, 8))));

        BWMOreDictionary.findLogRecipes();
        //TODO configure this




        if (!Loader.isModLoaded("primal")) {
            for (IBlockVariants variant : BWMOreDictionary.blockVariants) {
                ItemStack log = variant.getVariant(IBlockVariants.EnumBlock.LOG, 1);
                if (!log.isEmpty()) {
                    ResourceLocation location = new ResourceLocation(ModLib.MODID, log.getItem().getRegistryName().getPath() + "_" + log.getMetadata());
                    RecipeRegistry.addRecipe(new ChoppingRecipe(variant).setRegistryName(location));
                }
            }
        }
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        int plankCount = loadProperty("Saw Plank Output", 4).setComment("Plank count that is output when a log is chopped by a Saw.").get();
        int barkCount = loadProperty("Saw Bark Output", 1).setComment("Bark count that is output when a log is chopped by a Saw.").get();
        int sawDustCount = loadProperty("Saw sawdust Output", 2).setComment("Sawdust count that is output when a log is chopped by a Saw.").get();
        for (IBlockVariants wood : BWMOreDictionary.blockVariants) {
            RecipeRegistry.WOOD_SAW.addRecipe(new BlockDropIngredient(wood.getVariant(IBlockVariants.EnumBlock.LOG, 1)), Lists.newArrayList(wood.getVariant(IBlockVariants.EnumBlock.BLOCK, plankCount), wood.getVariant(IBlockVariants.EnumBlock.BARK, barkCount), wood.getVariant(IBlockVariants.EnumBlock.SAWDUST, sawDustCount)));
        }
    }

}
