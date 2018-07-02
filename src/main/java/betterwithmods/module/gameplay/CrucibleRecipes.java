package betterwithmods.module.gameplay;

import betterwithmods.common.BWRegistry;
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
    public CrucibleRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {

        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONE));
        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromOre(9, "nuggetDiamond"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromOre(9, "nuggetSoulforgedSteel"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT));

        BWRegistry.CRUCIBLE.addStokedRecipe(new OreIngredient("sand"), new ItemStack(Blocks.GLASS));
        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.GLASS_PANE,8)), new ItemStack(Blocks.GLASS));

        BWRegistry.CRUCIBLE.addStokedRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITECOBBLE), BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITESTONE));

        for (BlockCobble block : BlockCobble.BLOCKS) {
            BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(block), block.type.getStone());
        }
    }

}
