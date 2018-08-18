package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class IntArrayProperty extends ConfigProperty<int[]> {

    public IntArrayProperty(Configuration config, String property, String category, int[] defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public int[] get() {
        property.setDefaultValues(defaultValue);
        return property.getIntList();
    }
}
