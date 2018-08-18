package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWRegistry;
import betterwithmods.common.penalties.HealthPenalities;
import betterwithmods.module.Feature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by primetoxinz on 5/24/17.
 */
public class HCInjury extends Feature {
    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWRegistry.PENALTY_HANDLERS.add(new HealthPenalities());
    }

    @Override
    public String getDescription() {
        return "Add Penalties to lower health levels.";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
