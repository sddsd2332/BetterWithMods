package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelEndermanHead extends ModelHead {


    public ModelEndermanHead() {
        super(32, 16, 64, 32);

        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0f);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.headwear = new ModelRenderer(this, 0, 16);
        this.headwear.addBox(-4.0F, -8F, -4.0F, 8, 8, 8, -0.5F);
        this.headwear.setRotationPoint(0.0F, -14.0F, 0.0F);


    }
}
