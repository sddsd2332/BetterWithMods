package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class StringArrayProperty extends ConfigProperty<String[]> {

    public StringArrayProperty(Configuration config, String property, String category,  String[] defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public String[] get() {
        property.setDefaultValues(this.defaultValue);
        return property.getStringList();
    }
}
