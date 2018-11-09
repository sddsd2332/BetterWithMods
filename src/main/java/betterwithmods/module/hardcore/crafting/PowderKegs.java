package betterwithmods.module.hardcore.crafting;


import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class PowderKegs extends Feature {

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        config().overrideBlockResource("tnt_bottom");
        config().overrideBlockResource("tnt_top");
        config().overrideBlockResource("tnt_side");
        config().overrideItemResource("minecart_tnt");
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Blocks.TNT.getRegistryName()));

        Blocks.TNT.setTranslationKey("betterwithmods:powder_keg");
        Items.TNT_MINECART.setTranslationKey("betterwithmods:powder_keg_minecart");
    }
}
