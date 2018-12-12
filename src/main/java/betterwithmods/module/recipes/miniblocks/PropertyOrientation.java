package betterwithmods.module.recipes.miniblocks;

import betterwithmods.module.recipes.miniblocks.orientations.IOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.IOrientationPlacer;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import net.minecraft.block.properties.IProperty;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

public class PropertyOrientation<T extends IOrientation & Comparable<T>> implements IProperty<T> {

    private String name;

    private Set<T> values;

    private Class<T> clazz;

    private IOrientationPlacer<T> placer;

    @SafeVarargs
    public PropertyOrientation(String name, Class<T> clazz, IOrientationPlacer<T> placer, T... values) {
        this.name = name;
        this.placer = placer;
        this.values = Sets.newHashSet(values);
        this.clazz = clazz;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public Collection<T> getAllowedValues() {
        return values;
    }

    @Nonnull
    @Override
    public Class<T> getValueClass() {
        return clazz;
    }

    @SuppressWarnings("All")
    @Nonnull
    @Override
    public Optional<T> parseValue(@Nonnull String value) {
        return Optional.fromJavaUtil(values.stream().filter(s -> s.getName().equals(value)).findFirst());
    }

    @Nonnull
    @Override
    public String getName(@Nonnull T value) {
        return value.getName();
    }

    public IOrientationPlacer<T> getPlacer() {
        return this.placer;
    }

    public T getDefault() {
        return values.stream().findFirst().get();
    }
}
