package betterwithmods;

import betterwithmods.client.BWGuiHandler;
import betterwithmods.common.BWMRegistry;
import betterwithmods.common.event.FakePlayerHandler;
import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.lib.ModLib;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.general.General;
import betterwithmods.module.hardcore.Hardcore;
import betterwithmods.module.recipes.Recipes;
import betterwithmods.module.tweaks.Tweaks;
import betterwithmods.proxy.IProxy;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
@Mod(modid = ModLib.MODID, name = ModLib.NAME, version = ModLib.VERSION, dependencies = ModLib.DEPENDENCIES, guiFactory = "betterwithmods.client.gui.BWGuiFactory", acceptedMinecraftVersions = "[1.12, 1.13)")
public class BWMod {
    public static final ModuleLoader MODULE_LOADER = new ModuleLoader(new File(ModLib.MODID)).addModules(
            new General(),
            new Recipes(),
            new Tweaks(),
            new Hardcore()
    );

    public static Logger logger;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @SidedProxy(serverSide = "betterwithmods.proxy.ServerProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static IProxy proxy;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @Mod.Instance(ModLib.MODID)
    public static BWMod instance;

    static {
        FluidRegistry.enableUniversalBucket();
        ForgeModContainer.fullBoundingBoxLadders = true;
    }

    public static Logger getLog() {
        return logger;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        BWMAttributes.registerAttributes();
        MODULE_LOADER.setLogger(logger);
        MODULE_LOADER.onPreInit(event);
        BWMRegistry.onPreInit();
        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        BWMRegistry.init();
        MODULE_LOADER.onInit(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        proxy.onInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        BWMRegistry.postInit();
        MODULE_LOADER.onPostInit(event);
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        MODULE_LOADER.onServerStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        MODULE_LOADER.onServerStarted(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        FakePlayerHandler.reset();
    }


}
