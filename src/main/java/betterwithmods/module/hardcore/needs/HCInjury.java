package betterwithmods.module.hardcore.needs;

import betterwithmods.common.Registration;
import betterwithmods.common.penalties.HealthPenalities;
import betterwithmods.library.modularity.impl.Feature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by primetoxinz on 5/24/17.
 */
public class HCInjury extends Feature {
    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        Registration.PENALTY_HANDLERS.add(new HealthPenalities(this));
    }

    @Override
    public String getDescription() {
        return "Add Penalties to lower health levels.";
    }

}
