package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class AxeLeaves extends Feature {
    @Override
    public String getDescription() {
        return "Axes are fast at breaking leaves";
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        Blocks.LEAVES.setHarvestLevel("axe", 0);
        Blocks.LEAVES2.setHarvestLevel("axe", 1);
    }
}
