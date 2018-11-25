package betterwithmods.module.general.moreheads.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderHeadModel<M extends ModelBase> implements IRenderHead<M> {

    protected static final TextureManager manager = Minecraft.getMinecraft().getTextureManager();

    @Nonnull
    protected M model;
    @Nonnull
    private ResourceLocation texture;

    public RenderHeadModel(@Nonnull M model, @Nonnull ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public M getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public void render(double x, double y, double z, float partialTicks, float rotation) {
        manager.bindTexture(getTexture());
        GlStateManager.pushMatrix();

        GlStateManager.disableCull();
        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        getModel().render(null, partialTicks, 0.0F, 0.0F, rotation, 0.0F, getScale());

        GlStateManager.popMatrix();
    }

    public void setLightmap() {
        int i = 15728640;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
    }

    public float getScale() {
        return 0.0625f;
    }
}
