package betterwithmods.client.model.filters;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public abstract class ModelWithResource extends ModelBase {
    protected ResourceLocation location;

    public ModelWithResource(ResourceLocation location) {
        this.location = location;
    }

    public ModelWithResource() {
    }

    public void render(float scale) {
        renderModels(scale);
    }

    public ResourceLocation getResource() {
        return location;
    }

    public abstract void renderModels(float scale);

    public abstract boolean isSolid();
}
