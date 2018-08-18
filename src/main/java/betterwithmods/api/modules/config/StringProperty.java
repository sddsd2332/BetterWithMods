package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;

public class StringProperty extends ConfigProperty<String> {

    public StringProperty(Configuration config, String property, String category,  String defaultValue) {
        super(config, property, category, defaultValue);
    }

    @Override
    public String get() {
        property.setDefaultValue(this.defaultValue);
        return property.getString();
    }
}
