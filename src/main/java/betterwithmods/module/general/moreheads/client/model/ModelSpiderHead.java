package betterwithmods.module.general.moreheads.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelSpiderHead extends ModelHead {
    public ModelSpiderHead(int offsetX, int offsetY, int width, int height) {
        super(offsetX, offsetY, width, height);
        this.head = new ModelRenderer(this, 32, 4);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);

        this.head.setRotationPoint(0, 0, 0);

        layer2 = false;
    }
}
