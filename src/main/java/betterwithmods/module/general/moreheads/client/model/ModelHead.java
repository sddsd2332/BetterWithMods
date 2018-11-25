package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHead extends ModelBase {

    protected ModelRenderer head, headwear;
    protected boolean layer2;

    protected int offsetX, offsetY;


    public ModelHead(int offsetX, int offsetY, int width, int height) {
        this.layer2 = true;
        this.textureWidth = width;
        this.textureHeight = height;

        this.offsetX = offsetX;
        this.offsetY = offsetY;

        this.head = new ModelRenderer(this, offsetX, offsetY);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.headwear = new ModelRenderer(this, 32, 0);
        this.headwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.headwear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        head.render(scale);
        if (layer2) {
            headwear.render(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;

        this.headwear.showModel = true;

        this.headwear.rotationPointX = this.head.rotationPointX;
        this.headwear.rotationPointY = this.head.rotationPointY;
        this.headwear.rotationPointZ = this.head.rotationPointZ;
        this.headwear.rotateAngleX = this.head.rotateAngleX;
        this.headwear.rotateAngleY = this.head.rotateAngleY;
        this.headwear.rotateAngleZ = this.head.rotateAngleZ;
    }

}
