package betterwithmods.module.general;

import betterwithmods.library.common.modularity.impl.RequiredModule;
import betterwithmods.module.internal.UnitTesting;
import betterwithmods.module.internal.player.PlayerDataHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class General extends RequiredModule {

    private static boolean debug;

    public static boolean isDebug() {
        return debug;
    }

    @Override
    public void setup() {
        addFeatures(
                new Client(),
                new Waterwheel(),
                new PlayerDataHandler(),
                new Documentation(),
                new MechanicalPower(),
                new Hemp(),
                new MoreCobble(),
                new FluidBottles(),
                new UnitTesting(),
                new Pulley()
        );
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        debug = config().load(getName(), "Debug Mode", false).setComment("Log extra debug information, warning can spam logs").get();
        super.onPreInit(event);
    }
}
