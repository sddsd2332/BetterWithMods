package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class DoubleProperty extends ConfigProperty<Double> {

    public DoubleProperty(Configuration config, String property, String category, Double defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public Double get() {
        return property.getDouble(defaultValue);
    }

}
