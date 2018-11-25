package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelSheepHead extends ModelHead {
    public ModelSheepHead() {
        super(0, 0, 64, 32);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3F, -7F, -4.5F, 6, 6, 8, 0.6F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);

        layer2 = false;
    }
}