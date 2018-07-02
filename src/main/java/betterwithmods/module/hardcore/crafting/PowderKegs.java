package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class PowderKegs extends Feature {

    @Override
    public String getFeatureDescription() {
        return "";
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        overrideBlock("tnt_bottom");
        overrideBlock("tnt_top");
        overrideBlock("tnt_side");
        overrideItem("minecart_tnt");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:tnt"));
        Blocks.TNT.setUnlocalizedName("bwm:powder_keg");
        Items.TNT_MINECART.setUnlocalizedName("bwm:powder_keg_minecart");
    }
    
    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
