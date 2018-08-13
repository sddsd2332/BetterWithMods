package betterwithmods.client.model.generators;

import betterwithmods.client.tesr.TESRVerticalWindmill;

public class ModelVerticalWindmill extends ModelRadialBlades {

    public ModelVerticalWindmill() {
        super(8);
    }

    @Override
    protected ModelPart createBlade(int index) {

        ModelPart part = new ModelPart(this, 0, 0);

        float angle = (float) ((Math.PI * 2 * index) / bladeCount);
        //Sails
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(70.4F, -50.0F, -20.0F, 1, 100, 20)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL_SAIL)

        );

        //Shafts
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(68.4F, -54.0F, -2.0F, 4, 108, 4)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL_SHAFTS)
        );

        //Frame Arms
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(2.0F, -52.9F, -1.0F, 67, 2, 2)
                .addBox(2.0F, 50.9F, -1.0F, 67, 2, 2)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL)
        );

        //Frame Border
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(64.900002F, -52.9F, -26.0F, 2, 2, 52)
                .addBox(64.900002F, 50.900002F, -26.0F, 2, 2, 52)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle + 0.39F, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL)
        );

        return part;
    }
}
