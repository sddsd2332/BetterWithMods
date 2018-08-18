package betterwithmods;

import betterwithmods.client.BWGuiHandler;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.event.FakePlayerHandler;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.general.General;
import betterwithmods.proxy.IProxy;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
@Mod(modid = BWMod.MODID, name = BWMod.NAME, version = BWMod.VERSION, dependencies = BWMod.DEPENDENCIES, guiFactory = "betterwithmods.client.gui.BWGuiFactory", acceptedMinecraftVersions = "[1.12, 1.13)")
public class BWMod {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "${version}";
    public static final String NAME = "Better With Mods";
    public static final String DEPENDENCIES = "before:survivalist;after:traverse;after:thaumcraft;after:natura;after:mantle;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering;after:mekanism;after:thermalexpansion;after:ctm;after:geolosys;";
    public static final ModuleLoader MODULE_LOADER = new ModuleLoader().addModule(new General());

    public static Logger logger;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @SidedProxy(serverSide = "betterwithmods.proxy.ServerProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static IProxy proxy;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @Mod.Instance(BWMod.MODID)
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
        BWRegistry.onPreInit();
        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMlInitializationEvent event) {
        BWRegistry.init();
        MODULE_LOADER.onInit(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        BWRegistry.postInit();
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
        //TODO tests
//        if (isDev()) {
//            BWMTests.runTests();
//        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        FakePlayerHandler.reset();
    }

}
