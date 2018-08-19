package betterwithmods.module.general;

import betterwithmods.module.Feature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Pulley extends Feature {
    public static int MAX_BLOCKS;

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        MAX_BLOCKS = loadProperty("Max Blocks", 128).setComment("Maximum blocks allowed to be contained in a moving platform").get();
    }

    @Override
    public String getDescription() {
        return null;
    }
}
