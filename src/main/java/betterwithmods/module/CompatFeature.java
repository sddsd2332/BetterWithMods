package betterwithmods.module;

import net.minecraftforge.fml.common.Loader;

public abstract class CompatFeature extends Feature {

    private String modid;

    public CompatFeature(String modid) {
        this.modid = modid;
    }

    @Override
    public void setup() {
        super.setup();
        this.config().setCategoryComment(getName(), String.format("Dependency: %s . %s", modid, getDescription()));
    }

    @Override
    protected boolean canEnable() {
        if (!Loader.isModLoaded(modid)) {
            parent.getLogger().info("{} cannot be loaded due to missing dependency: {}", getName(), modid);
            return false;
        }
        return super.canEnable();
    }


}
