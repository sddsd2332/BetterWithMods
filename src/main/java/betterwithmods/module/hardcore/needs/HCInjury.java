package betterwithmods.module.hardcore.needs;

import betterwithmods.common.penalties.HealthPenalities;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.internal.MiscRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by primetoxinz on 5/24/17.
 */
public class HCInjury extends Feature {
    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        MiscRegistry.PENALTY_HANDLERS.add(new HealthPenalities(this));
    }

    @Override
    public String getDescription() {
        return "Add Penalties to lower health levels.";
    }

}
