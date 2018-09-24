package betterwithmods.common.penalties.attribute;

import betterwithmods.library.modularity.impl.Feature;
import net.minecraft.util.ResourceLocation;

public interface IAttribute<V> {
    V getDefaultValue();

    ResourceLocation getRegistryName();

    IAttributeInstance<V> fromConfig(Feature feature, String name, V defaultValue) ;
}
