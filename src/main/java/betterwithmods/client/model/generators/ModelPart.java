package betterwithmods.client.model.generators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModelPart extends net.minecraft.client.model.ModelRenderer {
    private ResourceLocation texture;
    private float[] color = null;
    private boolean shouldColor;


    public ModelPart(ModelBase model) {
        super(model);
    }

    public ModelPart(ModelBase model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public ModelPart(ModelBase model, String boxNameIn) {
        super(model, boxNameIn);
    }

    @Override
    public void render(float scale) {
        if (this.texture != null)
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        if (this.color != null) {
            GlStateManager.color(color[0], color[1], color[2]);
        }
        super.render(scale);
        if (this.color != null) {
            GlStateManager.color(1, 1, 1);
        }
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public ModelPart setRotationCenter(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        super.setRotationPoint(rotationPointXIn, rotationPointYIn, rotationPointZIn);
        return this;
    }

    @Override
    public ModelPart addBox(float offX, float offY, float offZ, int width, int height, int depth) {
        return (ModelPart) super.addBox(offX, offY, offZ, width, height, depth);
    }

    @Override
    public ModelPart setTextureSize(int textureWidthIn, int textureHeightIn) {
        return (ModelPart) super.setTextureSize(textureWidthIn, textureHeightIn);
    }

    public ModelPart setTexture(ResourceLocation texture) {
        this.texture = texture;
        if (childModels != null) {
            for (net.minecraft.client.model.ModelRenderer renderer : childModels) {
                if (renderer instanceof ModelPart) {
                    ((ModelPart) renderer).setTexture(this.texture);
                }
            }
        }
        return this;
    }

    public ModelPart setRotateAngle(double x, double y, double z) {
        this.rotateAngleX = (float) x;
        this.rotateAngleY = (float) y;
        this.rotateAngleZ = (float) z;
        return this;
    }

    public boolean shouldColor() {
        return shouldColor;
    }

    public ModelPart setColored() {
        this.shouldColor = true;
        return this;
    }
}
