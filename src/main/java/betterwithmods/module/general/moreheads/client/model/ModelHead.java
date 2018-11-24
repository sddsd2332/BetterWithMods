package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.entity.Entity;

public class ModelHead extends ModelSkeletonHead {

    private ModelRenderer headwear;

    public ModelHead(int offsetX, int offsetY, int width, int height) {
        super(offsetX, offsetY, width, height);
        this.headwear = new ModelRenderer(this, 32, 0);
        this.headwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.headwear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        headwear.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.headwear.rotateAngleY = netHeadYaw * 0.017453292F;
        this.headwear.rotateAngleX = headPitch * 0.017453292F;
    }
}
