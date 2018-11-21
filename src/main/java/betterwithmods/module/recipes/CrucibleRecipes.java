package betterwithmods.module.recipes;

import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockCobble;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.recipes.builder.CrucibleRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class CrucibleRecipes extends Feature {

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        CrucibleRecipeBuilder builder = new CrucibleRecipeBuilder();

        RecipeRegistry.CRUCIBLE.registerAll(
                builder.stoked().inputs(new ItemStack(Blocks.COBBLESTONE)).outputs(new ItemStack(Blocks.STONE)).build(),
                builder.stoked().inputs(StackIngredient.fromOre(9, "nuggetDiamond")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT)).build(),
                builder.stoked().inputs(StackIngredient.fromOre(9, "nuggetSoulforgedSteel")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)).build(),
                builder.stoked().inputs(new OreIngredient("sand")).outputs(new ItemStack(Blocks.GLASS)).build(),
                builder.stoked().inputs(StackIngredient.fromStacks(new ItemStack(Blocks.GLASS_PANE, 8))).outputs(new ItemStack(Blocks.GLASS)).build(),
                builder.stoked().inputs(BlockAesthetic.getStack(BlockAesthetic.Type.WHITECOBBLE)).outputs(BlockAesthetic.getStack(BlockAesthetic.Type.WHITESTONE)).build()
        );

        for (BlockCobble.Type type : BlockCobble.Type.VALUES) {
            RecipeRegistry.CRUCIBLE.register(builder.inputs(BlockCobble.getStack(type)).outputs(type.getStack()).build());
        }
    }

    @Override
    public String getDescription() {
        return null;
    }
}
