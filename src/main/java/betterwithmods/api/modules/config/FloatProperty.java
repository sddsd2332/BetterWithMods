package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class FloatProperty extends ConfigProperty<Float> {
    public FloatProperty(Configuration config, String property, String category, Float defaultValue) {
        super(config, property, category, (float) defaultValue.doubleValue());
    }

    @Override
    public Float get() {
        return (float) property.getDouble((double) defaultValue);
    }
}
