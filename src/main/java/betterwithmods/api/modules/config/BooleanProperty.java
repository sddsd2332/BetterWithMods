package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class BooleanProperty extends ConfigProperty<Boolean> {

    public BooleanProperty(Configuration config, String property, String category, Boolean defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public Boolean get() {
        return property.getBoolean(defaultValue);
    }
}
