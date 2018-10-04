package betterwithmods.module.hardcore.world;

import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by primetoxinz on 5/10/17.
 */
public class HCTorches extends Feature {
    @Override
    public String getDescription() {
        return "Change Torch Recipe to only provide 1 from each coal. Makes Nethercoal more useful, as it converts 1 coal into 4 Nethercoal.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Blocks.TORCH.getRegistryName()));
    }
}
