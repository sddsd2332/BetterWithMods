package betterwithmods.api.modules;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;

import javax.annotation.Nonnull;
import java.util.Comparator;

@SuppressWarnings("unused")
public interface IStateHandler extends Comparable<IStateHandler> {

    default void onConstructed(FMLConstructionEvent event) {
    }

    default void onPreInit(FMLPreInitializationEvent event) {
    }

    default void onInit(FMLInitializationEvent event) {
    }

    default void onPostInit(FMLPostInitializationEvent event) {
    }

    default void onServerStarted(FMLServerStartedEvent event) {
    }

    default void onServerStarting(FMLServerStartingEvent event) {
    }

    default void onPreInitClient(FMLPreInitializationEvent event) {
    }

    default void onInitClient(FMLInitializationEvent event) {
    }

    default void onPostInitClient(FMLPostInitializationEvent event) {
    }

    default void registerRecipes(RegistryEvent.Register<IRecipe> event) {
    }

    String getName();

    String getType();

    boolean isEnabled();

    default int priority() {
        return 0;
    }

    @Override
    default int compareTo(@Nonnull IStateHandler other) {
        return Comparator.comparing(IStateHandler::priority).compare(this, other);
    }
}
