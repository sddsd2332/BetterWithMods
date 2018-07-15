/*
  This class was created by <Vazkii>. It's distributed as
  part of the Quark Mod. Get the Source Code in github:
  https://github.com/Vazkii/Quark
  <p>
  Quark is Open Source and distributed under the
  CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
  <p>
  File Created @ [18/03/2016, 21:52:08 (GMT)]
 */
package betterwithmods.module;

import betterwithmods.BWMod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ModuleLoader {

    private final List<Class<? extends Module>> moduleClasses;
    private final Map<Class<? extends Module>, Module> moduleInstances;
    private final List<Module> enabledModules;
    public ConfigHelper configHelper;
    public Logger logger;

    public ModuleLoader() {
        enabledModules = Lists.newArrayList();
        moduleClasses = Lists.newArrayList();
        moduleInstances = Maps.newHashMap();
        registerModules();
    }

    public abstract void registerModules();

    public void preInit(FMLPreInitializationEvent event) {

        if (logger == null) {
            throw new RuntimeException("Logger for ModuleLoader was not set");
        }

        moduleClasses.forEach(clazz -> {
            try {
                moduleInstances.put(clazz, clazz.getConstructor(ModuleLoader.class).newInstance(this));
            } catch (Exception e) {
                throw new RuntimeException("Can't initialize module " + clazz, e);
            }
        });

        setupConfig(event);

        forEachModule(module -> logger.info("[BWM] Module " + module.getName() + " is " + (module.enabled ? "enabled" : "disabled")));
        enabledModules.sort(Comparator.comparingInt(Module::getPriority));
        forEachEnabled(module -> {
            logger.info("[BWM] Module PreInit : " + module.getName());
            module.preInit(event);
        });

        configHelper.save();
    }

    public void init(FMLInitializationEvent event) {
        forEachEnabled(module -> module.init(event));
        configHelper.save();
    }

    public void postInit(FMLPostInitializationEvent event) {
        forEachEnabled(module -> module.postInit(event));
        forEachEnabled(module -> module.finalInit(event));
        configHelper.save();
    }

    public void finalInit(FMLPostInitializationEvent event) {
        forEachEnabled(module -> module.finalInit(event));
    }

    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event) {
        GlobalConfig.initGlobalClient(this);
        forEachEnabled(module -> module.preInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void initClient(FMLInitializationEvent event) {

        forEachEnabled(module -> module.initClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void postInitClient(FMLPostInitializationEvent event) {
        forEachEnabled(module -> module.postInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        forEachEnabled(module -> module.registerModels(event));
    }

    public void serverStarting(FMLServerStartingEvent event) {
        forEachEnabled(module -> module.serverStarting(event));
    }

    public void setupConfig(FMLPreInitializationEvent event) {
        File file = event.getSuggestedConfigurationFile();
        configHelper = new ConfigHelper(file.getParent(), new Configuration(file));

        GlobalConfig.initGlobalConfig(this);

        forEachModule(module -> {
            module.enabled = true;
            if (module.canBeDisabled()) {
                configHelper.setRestartNeed(true);
                configHelper.setCategoryComment(module.name, module.getModuleDescription());
                module.enabled = configHelper.loadPropBool("enabled", module.name, "Enable this module", module.isEnabledByDefault());
            }
        });

        for (Module module : moduleInstances.values()) {
            if (module.isEnabled()) {
                enabledModules.add(module);
            }
        }

        loadModuleConfigs();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void loadModuleConfigs() {
        forEachModule(Module::setupConfig);
        configHelper.save();
    }

    public void forEachModule(Consumer<Module> consumer) {
        moduleInstances.values().forEach(consumer);
    }

    public void forEachEnabled(Consumer<Module> consumer) {
        enabledModules.forEach(consumer);
    }

    protected void registerModule(Class<? extends Module> clazz) {
        if (!moduleClasses.contains(clazz))
            moduleClasses.add(clazz);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(BWMod.MODID))
            loadModuleConfigs();
    }

    public boolean isFeatureEnabled(Class<? extends Feature> clazz) {
        for (Module module : this.moduleInstances.values()) {
            if (module.isEnabled()) {
                if (module.isFeatureEnabled(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
