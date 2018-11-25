package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelSpiderHead;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SpiderHead extends RenderHeadModel<ModelSpiderHead> {

    private float scale;

    public SpiderHead(@Nonnull ResourceLocation texture, float scale) {
        super(new ModelSpiderHead(0, 0, 64, 32), texture);
        this.scale = scale;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public ModelSpiderHead getModel() {
        return new ModelSpiderHead(0, 0, 64, 32);
    }
}
