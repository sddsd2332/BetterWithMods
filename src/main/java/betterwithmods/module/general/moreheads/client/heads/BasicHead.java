package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelHead;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class BasicHead extends RenderHeadModel<ModelHead> {

    public BasicHead(@Nonnull ModelHead model, @Nonnull ResourceLocation texture) {
        super(model, texture);
    }

    public BasicHead(@Nonnull ResourceLocation texture, int width, int height) {
        super(new ModelHead(0, 0, width, height), texture);
    }
}
