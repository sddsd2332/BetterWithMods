package betterwithmods.api.modules.impl;

import betterwithmods.api.modules.IStateHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class ListStateHandler<T extends IStateHandler> extends ArrayList<T> implements IStateHandler {

    private boolean enabled;

    protected void forEachEnabled(Consumer<T> consumer) {
        forEach(i -> {
            if (i.isEnabled()) consumer.accept(i);
        });
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

    @Override
    public void onInit(FMLInitializationEvent event) {
        forEachEnabled(i -> i.onInit(event));
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        forEachEnabled(i -> i.onPostInit(event));
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

    public abstract Logger getLogger();
}
