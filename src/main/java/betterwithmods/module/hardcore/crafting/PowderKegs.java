package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.recipes.RecipeMatchers;
import betterwithmods.library.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class PowderKegs extends Feature {

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        //TODO
//        config().overrideBlock("tnt_bottom");
//        config().overrideBlock("tnt_top");
//        config().overrideBlock("tnt_side");
//        config().overrideItem("minecart_tnt");
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Blocks.TNT.getRegistryName()));

        Blocks.TNT.setTranslationKey("betterwithmods:powder_keg");
        Items.TNT_MINECART.setTranslationKey("betterwithmods:powder_keg_minecart");
    }
}
