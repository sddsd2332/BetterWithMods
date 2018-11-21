package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.block.recipe.SawRecipe;
import betterwithmods.lib.ModLib;
import net.minecraft.util.ResourceLocation;

public class CraftingManagerSaw extends CraftingManagerBlock<SawRecipe> {

    public CraftingManagerSaw() {
        super(new ResourceLocation(ModLib.MODID, "saw"), SawRecipe.class);
    }

}
