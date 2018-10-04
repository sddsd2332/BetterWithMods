package betterwithmods.module.hardcore.world;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by primetoxinz on 5/6/17.
 */

public class HCInfo extends Feature {

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        event.getServer().getEntityWorld().getGameRules().setOrCreateGameRule("reducedDebugInfo", "true");
    }

    @Override
    public String getDescription() {
        return "Enables reducedDebugInfo by default for a more authentic BWM experience";
    }
}
