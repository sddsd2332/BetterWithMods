package betterwithmods.module.recipes;

import betterwithmods.common.BWMRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockCobble;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.util.StackIngredient;
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

        BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONE));
        BWMRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromOre(9, "nuggetDiamond"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        BWMRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromOre(9, "nuggetSoulforgedSteel"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT));
        BWMRegistry.CRUCIBLE.addStokedRecipe(new OreIngredient("sand"), new ItemStack(Blocks.GLASS));
        BWMRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.GLASS_PANE, 8)), new ItemStack(Blocks.GLASS));

        BWMRegistry.CRUCIBLE.addStokedRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITECOBBLE), BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITESTONE));

        for (BlockCobble block : BlockCobble.BLOCKS.values()) {
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(block), block.type.getStone());
        }
    }

    @Override
    public String getDescription() {
        return null;
    }
}
