package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelVillagerHead extends ModelHead {

    private ModelRenderer nose;

    public ModelVillagerHead(int width, int height) {
        super(0, 0, width, height);

        this.head = (new ModelRenderer(this)).setTextureSize(width, height);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0);

        this.nose = (new ModelRenderer(this)).setTextureSize(width, height);
        this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0);
        this.head.addChild(this.nose);

        layer2 = false;
    }
}
