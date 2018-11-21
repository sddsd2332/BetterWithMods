package betterwithmods.module.recipes;

import betterwithmods.api.recipe.output.impl.RandomCountOutputs;
import betterwithmods.api.recipe.output.impl.RandomOutput;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.registry.block.recipe.builder.SawRecipeBuilder;
import betterwithmods.common.registry.crafting.ChoppingRecipe;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.variants.IBlockVariants;
import betterwithmods.library.utils.VariantUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

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
    public void registerRecipes() {
        for (IBlockVariants variant : VariantUtils.BLOCK_VARIANTS) {
            ItemStack log = variant.getStack(IBlockVariants.EnumBlock.LOG, 1);
            if (!log.isEmpty()) {
                ResourceLocation location = new ResourceLocation(ModLib.MODID, log.getItem().getRegistryName().getPath() + "_" + log.getMetadata());
                RecipeRegistry.addRecipe(new ChoppingRecipe(variant).setRegistryName(location));
            }
        }
    }

    private static final SawRecipeBuilder builder = new SawRecipeBuilder();

    @Override
    public void onInit(FMLInitializationEvent event) {

        RecipeRegistry.WOOD_SAW.registerAll(
                builder.input(new BlockIngredient(Blocks.PUMPKIN)).outputs(new ItemStack(Blocks.PUMPKIN)).build(),
                builder.selfdrop(new ItemStack(Blocks.VINE)).build(),
                builder.selfdrop(new ItemStack(Blocks.YELLOW_FLOWER)).build(),
                builder.selfdrop(new ItemStack(Blocks.BROWN_MUSHROOM)).build(),
                builder.selfdrop(new ItemStack(Blocks.RED_MUSHROOM)).build(),
                builder.selfdrop(new ItemStack(BWMBlocks.ROPE)).build(),
                builder.input(new ItemStack(Blocks.MELON_BLOCK)).outputs(new RandomCountOutputs(new RandomOutput(new ItemStack(Items.MELON), 3, 8))).build()
        );
        //TODO meta in use
        for (int i = 0; i < 9; i++)
            RecipeRegistry.WOOD_SAW.register(builder.selfdrop(new ItemStack(Blocks.RED_FLOWER, 1, i)).build());
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        int plankCount = loadProperty("Saw Plank Output", 4).setComment("Plank count that is outputs when a log is chopped by a Saw.").get();
        int barkCount = loadProperty("Saw Bark Output", 1).setComment("Bark count that is outputs when a log is chopped by a Saw.").get();
        int sawDustCount = loadProperty("Saw sawdust Output", 2).setComment("Sawdust count that is outputs when a log is chopped by a Saw.").get();
        for (IBlockVariants wood : VariantUtils.BLOCK_VARIANTS) {
            RecipeRegistry.WOOD_SAW.register(builder.input(wood.getStack(IBlockVariants.EnumBlock.LOG, 1)).outputs(wood.getStack(IBlockVariants.EnumBlock.BLOCK, plankCount), wood.getStack(IBlockVariants.EnumBlock.BARK, barkCount), wood.getStack(IBlockVariants.EnumBlock.SAWDUST, sawDustCount)).build());
        }
    }

}
