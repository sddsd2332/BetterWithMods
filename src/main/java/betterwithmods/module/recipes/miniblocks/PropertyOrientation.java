package betterwithmods.module.recipes.miniblocks;

import betterwithmods.module.recipes.miniblocks.orientations.IOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.IOrientationPlacer;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import net.minecraft.block.properties.IProperty;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

public class PropertyOrientation<O extends IOrientation<O> & Comparable<O>> implements IProperty<O> {

    private String name;

    private Set<O> values;

    private Class<O> clazz;

    private IOrientationPlacer<O> placer;

    @SafeVarargs
    public PropertyOrientation(String name, Class<O> clazz, IOrientationPlacer<O> placer, O... values) {
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
    public Collection<O> getAllowedValues() {
        return values;
    }

    @Nonnull
    @Override
    public Class<O> getValueClass() {
        return clazz;
    }

    @SuppressWarnings("All")
    @Nonnull
    @Override
    public Optional<O> parseValue(@Nonnull String value) {
        return Optional.fromJavaUtil(values.stream().filter(s -> s.getName().equals(value)).findFirst());
    }

    @Nonnull
    @Override
    public String getName(@Nonnull O value) {
        return value.getName();
    }

    public IOrientationPlacer<O> getPlacer() {
        return this.placer;
    }

    public O getDefault() {
        return values.stream().findFirst().get();
    }
}
