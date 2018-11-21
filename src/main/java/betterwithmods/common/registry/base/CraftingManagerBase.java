package betterwithmods.common.registry.base;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class CraftingManagerBase<V extends IForgeRegistryEntry<V>> implements IForgeRegistry<V> {

    private ForgeRegistry<V> registry;
    private Class<V> type;
    private ResourceLocation registryName;

    public CraftingManagerBase(ResourceLocation registryName, Class<V> type) {
        this.type = type;
        this.registryName = registryName;
        this.registry = (ForgeRegistry<V>) new RegistryBuilder<V>()
                .setName(registryName)
                .setType(type)
                .create();
    }


    @Override
    public Iterator<V> iterator() {
        return registry.iterator();
    }

    @Nonnull
    @Override
    public Set<ResourceLocation> getKeys() {
        return registry.getKeys();
    }

    @Deprecated
    @Nonnull
    @Override
    public List<V> getValues() {
        return registry.getValues();
    }

    @Nonnull
    @Override
    public Set<Map.Entry<ResourceLocation, V>> getEntries() {
        return registry.getEntries();
    }

    @Override
    public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type) {
        return registry.getSlaveMap(slaveMapName, type);
    }

    @Nullable
    @Override
    public V getValue(ResourceLocation key) {
        return registry.getValue(key);
    }

    @Nullable
    @Override
    public ResourceLocation getKey(V value) {
        return registry.getKey(value);
    }

    @Override
    public boolean containsKey(ResourceLocation key) {
        return registry.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return registry.containsValue(value);
    }

    @Override
    public void register(V value) {
        if(value.getRegistryName() == null) {
            //TODO CHEAT CODES. THIS CANNNNNNNNN NOTTT STAY
            value.setRegistryName(getNextName());
        }
        registry.register(value);
    }

    private int i = 0;
    private ResourceLocation getNextName() {
        return new ResourceLocation(registryName.getNamespace(), registryName.getPath()+i++);
    }

    @SafeVarargs
    @Override
    public final void registerAll(V... values) {
        registry.registerAll(values);
    }

    public V remove(@Nonnull ResourceLocation key) {
        return registry.remove(key);
    }

    @Nonnull
    @Override
    public Collection<V> getValuesCollection() {
        return registry.getValuesCollection();
    }

    @Nonnull
    public Collection<V> getDisplayRecipes() {
        return getValuesCollection();
    }

    @Override
    public Class<V> getRegistrySuperType() {
        return type;
    }
}
