package betterwithmods.module.general.moreheads.client.model;

public class ModelPigHead extends ModelHead {
    public ModelPigHead() {
        super(0, 0, 64, 32);
        layer2 = false;
        this.head.setTextureOffset(16, 16).addBox(-2.0F, -4, -5f, 4, 3, 1, 0.0f);
    }
}
