package betterwithmods.module.general;

import betterwithmods.common.tile.TileWaterwheel;
import betterwithmods.module.Feature;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Waterwheel extends Feature {
    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        Arrays.stream(
                loadProperty("Valid Fluids", new String[]{
                        "swamp_water"
                }).setComment("Additional Fluids which will allow the Waterwheel to turn, format fluid_name. (Vanilla water will always work)").get()
        ).map(FluidRegistry::getFluid).filter(Objects::nonNull).collect(Collectors.toList()).forEach(fluid -> TileWaterwheel.registerWater(fluid.getBlock()));
    }
}
