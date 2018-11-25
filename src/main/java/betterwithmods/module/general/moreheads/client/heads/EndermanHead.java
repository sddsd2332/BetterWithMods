package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelEndermanHead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class EndermanHead extends RenderHeadModel<ModelEndermanHead> {

    private static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");

    public EndermanHead() {
        super(new ModelEndermanHead(), new ResourceLocation("minecraft:textures/entity/enderman/enderman.png"));
    }

    @Override
    public void render(double x, double y, double z, float partialTicks, float rotation) {
        super.render(x, y, z, partialTicks, rotation);
    }


    public void renderEyes(float partialTicks, float rotation) {
        manager.bindTexture(RES_ENDERMAN_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(true);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        model.render(null, partialTicks, 0.0F, 0.0F, rotation, 0.0F, 0.0625F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        setLightmap();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
}
