package betterwithmods;

import betterwithmods.common.Registration;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.ModuleLoader;
import betterwithmods.library.modularity.impl.proxy.Proxy;
import betterwithmods.module.general.General;
import betterwithmods.module.hardcore.Hardcore;
import betterwithmods.module.internal.InternalRegistries;
import betterwithmods.module.recipes.Recipes;
import betterwithmods.module.tweaks.Tweaks;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;


@Mod(modid = ModLib.MODID, name = ModLib.NAME, version = ModLib.VERSION, dependencies = ModLib.DEPENDENCIES, guiFactory = ModLib.GUI_FACTORY, acceptedMinecraftVersions = ModLib.MINECRAFT_VERISONS)
public class BetterWithMods {
    public static Logger LOGGER;

    @SidedProxy(serverSide = ModLib.SERVER_PROXY, clientSide = ModLib.CLIENT_PROXY, modId = ModLib.MODID)
    public static Proxy PROXY;

    @Mod.Instance(ModLib.MODID)
    public static BetterWithMods instance;

    public static final ModuleLoader MODULE_LOADER = new ModuleLoader(new File(ModLib.MODID)).addModules(
            new InternalRegistries(),
            new General(),
            new Recipes(),
            new Tweaks(),
            new Hardcore()
    );


    static {  //TODO move
        FluidRegistry.enableUniversalBucket();
        ForgeModContainer.fullBoundingBoxLadders = true;
    }

    public static Logger getLog() {
        return LOGGER;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        MODULE_LOADER.setLogger(LOGGER);
        PROXY.setLoader(MODULE_LOADER);
        PROXY.onPreInit(event);

        Registration.onPreInit();//TODO
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        Registration.init();    //TODO
        PROXY.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        Registration.postInit(); //TODO
        PROXY.onPostInit(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        PROXY.onServerStarting(event);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        PROXY.onServerStarted(event);
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        PROXY.onServerStopping(event);
    }



}
