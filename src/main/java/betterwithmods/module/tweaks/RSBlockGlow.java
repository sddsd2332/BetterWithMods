package betterwithmods.module.tweaks;

import betterwithmods.library.modularity.impl.Feature;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by primetoxinz on 5/14/17.
 */

public class RSBlockGlow extends Feature {
    @Override
    public void onInit(FMLInitializationEvent event) {
        Blocks.REDSTONE_BLOCK.setLightLevel(0.7F);
    }

    @Override
    public String getDescription() {
        return "Make Redstone blocks emit a little light";
    }
}
