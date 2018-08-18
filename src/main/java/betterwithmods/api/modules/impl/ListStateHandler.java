package betterwithmods.api.modules.impl;

import betterwithmods.api.modules.IStateHandler;
import net.minecraftforge.fml.common.event.*;

import java.util.HashSet;
import java.util.function.Consumer;

public class ListStateHandler<T extends IStateHandler> extends HashSet<T> implements IStateHandler {

    private boolean enabled;

    protected void forEachEnabled(Consumer<T> consumer) {
        forEach(i -> {
            if (i.isEnabled()) consumer.accept(i);
        });
    }

    @Override
    public void onConstructed(FMLConstructionEvent event) {
        forEachEnabled(i -> i.onConstructed(event));
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        forEachEnabled(i -> i.onPreInit(event));
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
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
