package betterwithmods.client.model.generators;

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
                .addBox(2, -2.0F, -2.0F, 99, 4, 4)
                .setRotationCenter(0F, 0F, 0F)
                .setRotateAngle(0, 0, rotation)
                .setTexture(TESRWindmill.WINDMILL)
        );



        //SAIL
        combo.addChild(new ModelPart(this, 0, 0)
                .addBanner(BannerUtils.HORIZONTAL_WINDMILL)
                .setTextureSize(168, 168)
                .addBox(15.0F, 1.75F, 1.0F, 84, 16, 1)
                .setRotateAngle(-0.26F, 0, rotation)
                .setTexture(TESRWindmill.WINDMILL_SAIL));


        return combo;
    }

    @Override
    public void render(float scale) {
        super.render(scale);
    }
}
