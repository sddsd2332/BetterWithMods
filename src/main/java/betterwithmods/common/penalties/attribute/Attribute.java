package betterwithmods.common.penalties.attribute;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.util.ResourceLocation;

public abstract class Attribute<V> implements IAttribute<V> {
    private final V value;
    private ResourceLocation registryName;
    private String description;

    public Attribute(ResourceLocation registryName, V value) {
        this.registryName = registryName;
        this.value = value;
    }

    @Override
    public V getDefaultValue() {
        return value;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public Attribute<V> setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }

    public AttributeInstance<V> fromConfig(Feature feature, String name, V defaultValue) {
        return BWMAttributes.getConfigAttribute(feature, this, name, getDescription(), defaultValue);
    }

    public String getDescription() {
        return description;
    }

    public Attribute<V> setDescription(String description) {
        this.description = description;
        return this;
    }
}
