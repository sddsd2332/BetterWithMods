package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class PowderKegs extends Feature {

    @Override
    public String getDescription() {
        return "";
    }

    //TODO
//
//    @Override
//    public void preInitClient(FMLPreInitializationEvent event) {
//        overrideBlock("tnt_bottom");
//        overrideBlock("tnt_top");
//        overrideBlock("tnt_side");
//        overrideItem("minecart_tnt");
//    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:tnt"));
        Blocks.TNT.setTranslationKey("betterwithmods:powder_keg");
        Items.TNT_MINECART.setTranslationKey("betterwithmods:powder_keg_minecart");
    }
}
