package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class BooleanArrayProperty extends ConfigProperty<boolean[]> {

    public BooleanArrayProperty(Configuration config, String property, String category, boolean[] defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public boolean[] get() {
        property.setDefaultValues(defaultValue);
        return property.getBooleanList();
    }
}
