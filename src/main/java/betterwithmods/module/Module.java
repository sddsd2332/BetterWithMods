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
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Module extends ListStateHandler<Feature> {

    private final String name;
    private boolean enabled;
    private ConfigHelper config;
    private Logger logger;

    public Module() {
        this.name = getClass().getSimpleName().toLowerCase();
        this.addFeatures();
    }

    public void addFeatures() {
    }

    /**
     * @param file   Directory from {@link FMLPreInitializationEvent#getModConfigurationDirectory()},
     * @param logger Mod log instance
     * @return list of {@link Feature}s that are enabled, used for {@link ModuleLoader#isFeatureEnabled(Class)}
     */
    public List<Feature> setup(File file, Logger logger) {
        this.setLogger(logger);
        this.setConfig(new ConfigHelper(file.getPath(), new Configuration(new File(file, getName())), getName()));
        this.enabled = canEnable();
        if (isEnabled()) {
            forEachEnabled(Feature::setup);
        }
        return stream().filter(Feature::isEnabled).collect(Collectors.toList());
    }


    protected void addFeature(Feature feature) {
        feature.parent = this;
        this.add(feature);
    }

    protected String getName() {
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
        return config().load(getName(), "Enabled", isEnabledByDefault()).get();
    }

    protected boolean isEnabledByDefault() {
        return true;
    }
}

