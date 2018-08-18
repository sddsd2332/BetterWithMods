package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class DoubleArrayProperty extends ConfigProperty<double[]> {

    public DoubleArrayProperty(Configuration config, String property, String category, double[] defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public double[] get() {
        property.setDefaultValues(defaultValue);
        return property.getDoubleList();
    }
}
