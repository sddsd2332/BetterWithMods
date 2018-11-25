package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSlime extends ModelBase {

    private ModelRenderer slimeBodies;
    private ModelRenderer slimeRightEye;
    private ModelRenderer slimeLeftEye;
    private ModelRenderer slimeMouth;
    private ModelRenderer slimeGel;

    public ModelSlime() {
        this.slimeBodies = new ModelRenderer(this, 0, 16);
        this.slimeBodies.addBox(-3.0F, -7, -3.0F, 6, 6, 6);
        this.slimeRightEye = new ModelRenderer(this, 32, 0);
        this.slimeRightEye.addBox(-3.25F, -6, -3.5F, 2, 2, 2);
        this.slimeLeftEye = new ModelRenderer(this, 32, 4);
        this.slimeLeftEye.addBox(1.25F, -6, -3.5F, 2, 2, 2);
        this.slimeMouth = new ModelRenderer(this, 32, 8);
        this.slimeMouth.addBox(0.0F, -3, -3.5F, 1, 1, 1);
        this.slimeGel = new ModelRenderer(this, 0, 0);
        this.slimeGel.addBox(-4.0F, -8, -4.0F, 8, 8, 8);

    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.translate(0.0F, 0.001F, 0.0F);
        this.slimeBodies.render(scale);
        this.slimeRightEye.render(scale);
        this.slimeLeftEye.render(scale);
        this.slimeMouth.render(scale);
        this.slimeGel.render(scale);

    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        this.slimeBodies.rotateAngleY = netHeadYaw * 0.017453292F;
        this.slimeBodies.rotateAngleX = headPitch * 0.017453292F;

        this.slimeGel.rotateAngleX = this.slimeBodies.rotateAngleX;
        this.slimeGel.rotateAngleY = this.slimeBodies.rotateAngleY;

        this.slimeRightEye.rotateAngleX = this.slimeBodies.rotateAngleX;
        this.slimeRightEye.rotateAngleY = this.slimeBodies.rotateAngleY;

        this.slimeLeftEye.rotateAngleX = this.slimeBodies.rotateAngleX;
        this.slimeLeftEye.rotateAngleY = this.slimeBodies.rotateAngleY;

        this.slimeMouth.rotateAngleX = this.slimeBodies.rotateAngleX;
        this.slimeMouth.rotateAngleY = this.slimeBodies.rotateAngleY;
    }
}
