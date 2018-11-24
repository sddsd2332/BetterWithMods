package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class BasicHead extends RenderHeadModel<ModelSkeletonHead> {

    public BasicHead(@Nonnull ModelSkeletonHead model, @Nonnull ResourceLocation texture) {
        super(model, texture);
    }

    public BasicHead(@Nonnull ResourceLocation texture, int width, int height) {
        super(new ModelSkeletonHead(0, 0, width, height), texture);
    }
}
