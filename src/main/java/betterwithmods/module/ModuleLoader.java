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

import betterwithmods.api.modules.impl.ListStateHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class ModuleLoader extends ListStateHandler<Module> {

    static final HashMap<String, Boolean> JSON_CONDITIONS = Maps.newHashMap();


    private Set<Class<? extends Feature>> enabledFeatures = Sets.newHashSet();

    private Logger logger;
    private File relativeConfigDir;

    public ModuleLoader(File relativeConfigDir) {
        this.relativeConfigDir = relativeConfigDir;
    }

    public ModuleLoader addModules(Module... modules) {
        for(Module module: modules)
            addModule(module);
        return this;
    }

    public ModuleLoader addModule(Module module) {
        this.add(module);
        return this;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        File file = event.getSuggestedConfigurationFile();
        ConfigHelper helper = new ConfigHelper(file.getParent(), new Configuration(file));
        forEach(module -> {
            //FIXME Currently have a config for each module, not entirely sure about this
//            File file = new File(event.getModConfigurationDirectory(), relativeConfigDir.getPath());
//            ConfigHelper helper = new ConfigHelper(file.getPath(), new Configuration(new File(file, module.getName() + ".cfg")));

            List<Feature> feature = module.setup(helper, getLogger());
            //Add all feature classes to the set;
            enabledFeatures.addAll(feature.stream().map(Feature::getClass).collect(Collectors.toSet()));
        });
        super.onPreInit(event);
    }

    public boolean isFeatureEnabled(Class<? extends Feature> clazz) {
        return enabledFeatures.contains(clazz);
    }

    @SuppressWarnings("unused")
    public static class ConditionConfig implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            String enabled = JsonUtils.getString(json, "enabled");
            return () -> JSON_CONDITIONS.getOrDefault(enabled, false);
        }
    }

    @Override
    public String getType() {
        return "ModuleLoader";
    }

    @Override
    public String getName() {
        return "ModuleLoader";
    }
}
