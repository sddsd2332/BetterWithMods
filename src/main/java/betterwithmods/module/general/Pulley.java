package betterwithmods.module.general;

import betterwithmods.library.modularity.impl.RequiredFeature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Pulley extends RequiredFeature {
    public static int MAX_BLOCKS;


    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        MAX_BLOCKS = loadProperty("Max Blocks", 128).setComment("Maximum blocks allowed to be contained in a moving platform").get();
    }

}
