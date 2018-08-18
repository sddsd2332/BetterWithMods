package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;

/**
 * Created by primetoxinz on 5/14/17.
 */
public class RSBlockGlow extends Feature {
    @Override
    public void onInit(FMlInitializationEvent event) {
        Blocks.REDSTONE_BLOCK.setLightLevel(0.7F);
    }

    @Override
    public String getDescription() {
        return "Make Redstone blocks emit a little light";
    }
}
