package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelCowHead extends ModelHead {
    public ModelCowHead() {
        super(0, 0, 64, 32);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 6, 0.0F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(22, 0).addBox(-5,-9,-2,1, 3, 1, 0.0F);
        this.head.setTextureOffset(22, 0).addBox(4,-9,-2,1, 3, 1, 0.0F);
        layer2 = false;
    }
}