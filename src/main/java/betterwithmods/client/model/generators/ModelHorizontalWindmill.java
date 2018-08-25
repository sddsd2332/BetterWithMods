package betterwithmods.client.model.generators;

import betterwithmods.client.tesr.TESRVerticalWindmill;
import betterwithmods.client.tesr.TESRWindmill;
import betterwithmods.util.BannerUtils;

public class ModelHorizontalWindmill extends ModelRadialBlades {


    public ModelHorizontalWindmill() {
        super(4);
    }

    @Override
    protected ModelPart createBlade(int index) {
        double rotation = (Math.PI * index) / (bladeCount / 2);

        ModelPart combo = new ModelPart(this, 0, 0);

        //SHAFT
        combo.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(2, -2.0F, -2.0F, 110, 4, 4)
                .setRotationCenter(0F, 0F, 0F)
                .setRotateAngle(0, 0, rotation)
                .setTexture(TESRWindmill.WINDMILL_FRAME)
        );


        //SAIL
        combo.addChild(new ModelPart(this, 0, 0)
                .addBanner(BannerUtils.WINDMILLS)
                .setTextureSize(128, 128)
                .addBox(0, 10, 0, 20, 100, 1, true)
                .setRotateAngle(0, 0, -rotation)
                .setTexture(TESRVerticalWindmill.WINDMILL_SAIL));


        return combo;
    }

    @Override
    public void render(float scale) {
        super.render(scale);
    }
}
