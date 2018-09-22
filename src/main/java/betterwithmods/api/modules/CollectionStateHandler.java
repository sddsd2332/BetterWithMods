package betterwithmods.api.modules;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class CollectionStateHandler<T extends IStateHandler, C extends Collection<T>> implements IStateHandler {

    private boolean enabled;
    protected C basedCollection;

    public CollectionStateHandler() {
        this.basedCollection = createCollection();
    }

    protected abstract C createCollection();


    public void add(T t) {
        this.basedCollection.add(t);
    }

    protected void forEachEnabled(Consumer<T> consumer) {
        forEach(i -> {
            if (i.isEnabled())
                consumer.accept(i);
        });
    }

    public void forEach(Consumer<T> t) {
        basedCollection.forEach(t);
    }


    @Override
    public void onConstructed(FMLConstructionEvent event) {
        forEachEnabled(i -> {
            getLogger().info("[Construction] {}: {}", i.getType(), i.getName());
            i.onConstructed(event);
        });
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        forEachEnabled(i -> {
            getLogger().info(" {}: {} is enabled", i.getType(), i.getName());
            i.onPreInit(event);
        });
    }

    public abstract Logger getLogger();

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        forEachEnabled(i -> i.onPreInitClient(event));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        forEachEnabled(i -> i.onInit(event));
    }

    @Override
    public void onInitClient(FMLInitializationEvent event) {
        forEachEnabled(i -> i.onInitClient(event));
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        forEachEnabled(i -> i.onPostInit(event));
    }

    @Override
    public void onPostInitClient(FMLPostInitializationEvent event) {
        forEachEnabled(i -> i.onPostInitClient(event));
    }

    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        forEachEnabled(i -> i.onServerStarted(event));
    }

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        forEachEnabled(i -> i.onServerStarting(event));
    }


    @Override
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        forEachEnabled(i -> i.registerRecipes(event));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Stream<T> stream() {
        return basedCollection.stream();
    }
}
