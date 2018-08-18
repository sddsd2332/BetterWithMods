package betterwithmods.api.modules.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class ConfigProperty<T> {
    protected T defaultValue;
    protected boolean isArray;
    protected Property property;
    private Class<T> classType;

    private Configuration config;
    private String category;

    public ConfigProperty(Configuration config, String property, String category, T defaultValue) {
        this.config = config;
        this.classType = (Class<T>) defaultValue.getClass();
        this.isArray = classType.isArray();
        this.property = createProperty(property, category, fromClass(), classType.isArray());
        this.defaultValue = defaultValue;
        this.category = category;
    }

    public ConfigProperty<T> setComment(String comment) {
        this.property.setComment(comment);
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public ConfigProperty<T> setCategory(String category) {
        config.getCategory(category).put(property.getName(), property);
        return this;
    }

    public ConfigProperty<T> subCategory(String child) {
        return setCategory(String.format("%s.%s", getCategory(), child));
    }

    public ConfigProperty<T> setCategoryComment(String comment) {
        config.setCategoryComment(getCategory(), comment);
        return this;
    }

    private Property createProperty(String category, String property, Property.Type type, boolean isList) {
        Property prop = config.getCategory(category).get(property);
        if (prop == null) {
            if (isList)
                prop = new Property(property, new String[0], type);
            else
                prop = new Property(property, (String) null, type);
            config.getCategory(category).put(property, prop);
        }
        return prop;
    }

    public abstract T get();

    private Property.Type fromClass() {
        if (classType.isAssignableFrom(boolean.class) || classType.isAssignableFrom(Boolean.class)) {
            return Property.Type.BOOLEAN;
        } else if (classType.isAssignableFrom(int.class) || classType.isAssignableFrom(Integer.class)) {
            return Property.Type.INTEGER;
        } else if (classType.isAssignableFrom(double.class) || classType.isAssignableFrom(Double.class)) {
            return Property.Type.DOUBLE;
        } else if (classType.isAssignableFrom(String.class)) {
            return Property.Type.STRING;
        }
        return null;
    }


}
