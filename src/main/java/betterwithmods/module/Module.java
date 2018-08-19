/*
  This class was created by <Vazkii>. It's distributed as
  part of the Quark Mod. Get the Source Code in github:
  https://github.com/Vazkii/Quark
  <p>
  Quark is Open Source and distributed under the
  CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
  <p>
  File Created @ [18/03/2016, 21:52:14 (GMT)]
 */
package betterwithmods.module;

import betterwithmods.api.modules.impl.ListStateHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Module extends ListStateHandler<Feature> {

    private final String name;
    private boolean enabled;
    private ConfigHelper config;
    private Logger logger;

    public Module() {
        this.name = getClass().getSimpleName().toLowerCase();
    }

    public void addFeatures() {
    }

    /**
     * @param helper supplies a {@link ConfigHelper} for creating configurable code ,
     * @param logger Mod log instance
     * @return list of {@link Feature}s that are enabled, used for {@link ModuleLoader#isFeatureEnabled(Class)}
     */
    public List<Feature> setup(ConfigHelper helper, Logger logger) {
        this.setLogger(logger);
        this.setConfig(helper);
        this.addFeatures();
        this.enabled = canEnable();
        if (isEnabled()) {
            forEach(Feature::setup);
        }
        config.save();
        return stream().filter(Feature::isEnabled).collect(Collectors.toList());
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        config.save();
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        config.save();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);
        config.save();
    }

    protected void addFeatures(Feature... features) {
        for (Feature feature : features)
            addFeature(feature);
    }

    protected void addFeature(Feature feature) {
        feature.parent = this;
        this.add(feature);
    }

    protected void addFeature(Class<? extends Feature> clazz, String... dependencies) {
        config().setCategoryComment(ConfigHelper.joinCategory(getName(), clazz.getSimpleName()), "Requires:" + String.join(",", dependencies));
        if (Arrays.stream(dependencies).allMatch(Loader::isModLoaded)) {
            try {
                this.addFeature(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public void setConfig(ConfigHelper config) {
        this.config = config;
    }

    public ConfigHelper config() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    protected boolean canEnable() {
        return config().load(getName(), "Enabled", isEnabledByDefault()).setComment("Enable this module").get();
    }

    protected boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public String getType() {
        return "Module";
    }

}

