package betterwithmods.module.recipes;

import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockCobble;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.modularity.impl.Feature;
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
        RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONE));
        RecipeRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromOre(9, "nuggetDiamond"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        RecipeRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromOre(9, "nuggetSoulforgedSteel"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT));
        RecipeRegistry.CRUCIBLE.addStokedRecipe(new OreIngredient("sand"), new ItemStack(Blocks.GLASS));
        RecipeRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.GLASS_PANE, 8)), new ItemStack(Blocks.GLASS));

        RecipeRegistry.CRUCIBLE.addStokedRecipe(BlockAesthetic.getStack(BlockAesthetic.Type.WHITECOBBLE), BlockAesthetic.getStack(BlockAesthetic.Type.WHITESTONE));

        for(BlockCobble.Type type: BlockCobble.Type.VALUES) {
            RecipeRegistry.CRUCIBLE.addStokedRecipe(BlockCobble.getStack(type), type.getStack());
        }
    }

    @Override
    public String getDescription() {
        return null;
    }
}
