/*
  This class was created by <Vazkii>. It's distributed as
  part of the Quark Mod. Get the Source Code in github:
  https://github.com/Vazkii/Quark
  <p>
  Quark is Open Source and distributed under the
  CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
  <p>
  File Created @ [18/03/2016, 22:46:32 (GMT)]
 */
package betterwithmods.module;

import betterwithmods.api.modules.IStateHandler;
import betterwithmods.api.modules.config.ConfigProperty;
import net.minecraftforge.fml.common.Loader;

public abstract class Feature implements IStateHandler {

    public boolean enabled, recipeCondition;
    protected Module parent;
    private String name, category;

    public Feature recipes() {
        recipeCondition = true;
        ModuleLoader.JSON_CONDITIONS.put(getName(), recipeCondition);
        return this;
    }

    public void setup() {
        this.name = getClass().getSimpleName().toLowerCase();
        this.category = ConfigHelper.joinCategory(parent.getName(), getName());
        this.enabled = canEnable();
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public ConfigHelper config() {
        return parent.config();
    }


    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return name;
    }

    public <T> ConfigProperty<T> loadProperty(String property, T defaultValue) {
        return config().load(this.category, property, defaultValue);
    }

    public abstract String getDescription();

    public String[] getIncompatibleMods() {
        return new String[0];
    }


    protected boolean isEnabledByDefault() {
        return true;
    }

    /**
     * @return boolean that dictates whether a feature will be enabled on load. Default implementation is based on a config entry
     */
    protected boolean canEnable() {
        for (String modid : getIncompatibleMods()) {
            if (Loader.isModLoaded(modid)) {
                parent.getLogger().info("Feature: {} was not loaded due to an incompatible mod being installed - {}", getName(), modid);
                return false;
            }
        }
        return config().load(this.category, "Enabled", isEnabledByDefault()).setCategoryComment(getDescription()).get();
    }


    @Override
    public String getType() {
        return "Feature";
    }
}
