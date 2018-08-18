package betterwithmods.api.modules;

import net.minecraftforge.fml.common.event.*;

@SuppressWarnings("unused")
public interface IStateHandler {

    default void onConstructed(FMLConstructionEvent event) {}

    default void onPreInit(FMLPreInitializationEvent event) { }

    default void onInit(FMLInitializationEvent event) {
    }

    default void onPostInit(FMLPostInitializationEvent event) {
    }

    default void onServerStarted(FMLServerStartedEvent event) {}

    default void onServerStarting(FMLServerStartingEvent event) {}

    default void onPreInitClient(FMLPreInitializationEvent event) {}

    default void onInitClient(FMLInitializationEvent event) {}

    default void onPostInitClient(FMLPostInitializationEvent event) {
    }


    boolean isEnabled();
}
