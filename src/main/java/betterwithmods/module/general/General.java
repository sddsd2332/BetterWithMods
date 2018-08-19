package betterwithmods.module.general;

import betterwithmods.module.Module;
import betterwithmods.module.general.player.PlayerDataHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class General extends Module {

    private static boolean debug;

    public static boolean isDebug() {
        return debug;
    }

    @Override
    public void addFeatures() {
        addFeatures(
                new Client(),
                new Waterwheel(),
                new PlayerDataHandler(),
                new Documentation(),
                new MechanicalPower(),
                new Hemp(),
                new MoreCobble(),
                new UnitTesting()
        );
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        debug = config().load(getName(), "Debug Mode", false).setComment("Log extra debug information, warning can spam logs").get();
        super.onPreInit(event);
    }
}
