package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class IntProperty extends ConfigProperty<Integer> {

    public IntProperty(Configuration config, String property, String category, Integer defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public Integer get() {
        return property.getInt(defaultValue);
    }
}
