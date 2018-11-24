package betterwithmods.module.general.moreheads.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public interface IRenderHead<M extends ModelBase> {

    M getModel();

    ResourceLocation getTexture();

    void render(double x, double y, double z, float partialTicks, float rotation);
}
