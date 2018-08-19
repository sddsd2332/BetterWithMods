package betterwithmods.client.model.generators;

import betterwithmods.client.tesr.TESRVerticalWindmill;
import betterwithmods.util.BannerUtils;
import net.minecraft.util.math.MathHelper;

public class ModelVerticalWindmill extends ModelRadialBlades {

    public ModelVerticalWindmill() {
        super(8);
    }

    @Override
    protected ModelPart createBlade(int index) {

        ModelPart part = new ModelPart(this, 0, 0);

        float angle = (float) ((Math.PI * 2 * index) / bladeCount);
        float radius = 72.5f;
        float x = radius * MathHelper.sin(angle), z = radius * MathHelper.cos(angle);

        double sailAngle = (Math.PI / (bladeCount * 2));
        
        //Sails
        part.addChild(new ModelPart(this, 0, 0)
                .addBanner(BannerUtils.VERTICAL_WINDMILL)
                .setTextureSize(128, 128)
                .addBox(0, 0, 0, 20, 100, 1, true)
                .setRotationCenter(x, -50, z)
                .setRotateAngle(0, angle + sailAngle, 0)
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
