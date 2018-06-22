package betterwithmods;

import betterwithmods.client.BWGuiHandler;
import betterwithmods.common.BWIMCHandler;
import betterwithmods.common.BWRegistry;
import betterwithmods.event.FakePlayerHandler;
import betterwithmods.module.CompatModule;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.Gameplay;
import betterwithmods.module.hardcore.Hardcore;
import betterwithmods.module.industry.Industry;
import betterwithmods.module.tweaks.Tweaks;
import betterwithmods.proxy.IProxy;
import betterwithmods.testing.BWMTests;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid = BWMod.MODID, name = BWMod.NAME, version = BWMod.VERSION, dependencies = BWMod.DEPENDENCIES, guiFactory = "betterwithmods.client.gui.BWGuiFactory", acceptedMinecraftVersions = "[1.12, 1.13)")
public class BWMod {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "${version}";
    public static final String NAME = "Better With Mods";
    public static final String DEPENDENCIES = "after:thaumcraft;after:natura;before:survivalist;after:mantle;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering;after:mekanism;after:thermalexpansion;after:ctm;after:geolosys;";

    public static Logger logger;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @SidedProxy(serverSide = "betterwithmods.proxy.ServerProxy", clientSide = "betterwithmods.proxy.ClientProxy")
    public static IProxy proxy;
    @SuppressWarnings({"CanBeFinal", "unused"})
    @Mod.Instance(BWMod.MODID)
    public static BWMod instance;

    public static ModuleLoader MODULE_LOADER = new ModuleLoader() {
        @Override
        public void registerModules() {
            registerModule(Gameplay.class);
            registerModule(Hardcore.class);
            registerModule(Tweaks.class);
            registerModule(CompatModule.class);
            registerModule(Industry.class);
        }
    };

    public static Logger getLog() {
        return logger;
    }

    @Mod.EventHandler
    public void onConstruct(FMLConstructionEvent event) {
        ForgeModContainer.fullBoundingBoxLadders = true;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        BWMAttributes.registerAttributes();
        MODULE_LOADER.preInit(evt);
        BWRegistry.preInit();
        proxy.preInit(evt);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        BWRegistry.init();
        MODULE_LOADER.init(evt);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new BWGuiHandler());
        proxy.init(evt);
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        BWRegistry.postInit();
        MODULE_LOADER.postInit(evt);
        proxy.postInit(evt);
    }

    @Mod.EventHandler
    public void processIMCMessages(IMCEvent evt) {
        BWIMCHandler.processIMC(evt.getMessages());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        MODULE_LOADER.serverStarting(evt);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent evt) {
        if(isDev()) {
            BWMTests.runTests();
        }
        if(GlobalConfig.debug) {
            evt.registerServerCommand(new HealthCommand());
        }
    }


    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent evt) {
        FakePlayerHandler.setPlayer(null);
        FakePlayerHandler.setCreativePlayer(null);
    }

    public static boolean isDev() {
        return BWMod.VERSION.equalsIgnoreCase("${version}");
    }

}
