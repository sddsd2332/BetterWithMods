package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SlimeHead extends RenderHeadModel<ModelSlime> {

    public SlimeHead() {
        super(new ModelSlime(), new ResourceLocation("minecraft:textures/entity/slime/slime.png"));
    }

    @Override
    public void render(double x, double y, double z, float partialTicks, float rotation) {
        manager.bindTexture(getTexture());
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableCull();

        getModel().render(null, partialTicks, 0.0F, 0.0F, rotation, 0.0F, getScale());

        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

}
