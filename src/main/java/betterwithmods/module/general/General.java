package betterwithmods.module.general;

import betterwithmods.module.Module;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class General extends Module {

    private static boolean debug;

    public static boolean isDebug() {
        return debug;
    }

    @Override
    public void addFeatures() {
        addFeature(new Client());
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        debug = config().load(getName(), "Debug Mode", false).setComment("Log extra debug information, warning can spam logs").get();
    }
}
