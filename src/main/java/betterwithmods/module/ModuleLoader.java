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
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class ModuleLoader extends ListStateHandler<Module> {

    static final HashMap<String, Boolean> JSON_CONDITIONS = Maps.newHashMap();


    private Set<Class<? extends Feature>> enabledFeatures = Sets.newHashSet();

    private Logger logger;

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
        forEach(module -> {
            List<Feature> feature = module.setup(event.getModConfigurationDirectory(), getLogger());
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

}
