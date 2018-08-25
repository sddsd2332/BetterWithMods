package betterwithmods.client.model.generators;

import betterwithmods.BWMod;
import betterwithmods.util.BannerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.util.ResourceLocation;

public class ModelPart extends net.minecraft.client.model.ModelRenderer {
    private ResourceLocation texture;
    private BannerUtils.BannerData banner = null;
    private BannerTextures.Cache cache;

    private boolean hasBanner;


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
        if (texture != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        }
        if (banner != null && cache != null) {
            try {
                ResourceLocation texture = banner.getTexture(cache);
                if (texture != null)
                    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
            } catch (Exception e) {
                BWMod.logger.error(e);
            }
        }

        super.render(scale);
    }

    public void setBanner(BannerUtils.BannerData banner) {
        this.banner = banner;
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
    public ModelPart addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored) {
        return (ModelPart) super.addBox(offX, offY, offZ, width, height, depth, mirrored);
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

    @Override
    public ModelPart setTextureOffset(int x, int y) {
        return (ModelPart) super.setTextureOffset(x, y);
    }

    public ModelPart setRotateAngle(double x, double y, double z) {
        this.rotateAngleX = (float) x;
        this.rotateAngleY = (float) y;
        this.rotateAngleZ = (float) z;
        return this;
    }

    public boolean hasBanner() {
        return hasBanner;
    }

    public ModelPart addBanner(BannerTextures.Cache cache) {
        this.hasBanner = true;
        this.cache = cache;
        return this;
    }
}
