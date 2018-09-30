package betterwithmods.client.model.generators;

import betterwithmods.client.tesr.TESRVerticalWindmill;
import betterwithmods.client.tesr.TESRWindmill;
import betterwithmods.common.Registration;
import betterwithmods.proxy.BWMClientProxy;
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

        int height = 100;
        int arm_size = 2, shaft_size = 4;
        int arm_length = (int) (radius) - shaft_size;
        int border_length = 52;

        float top = height / 2, bottom = -top;

        //Sails
        part.addChild(new ModelPart(this, 0, 0)
                .addBanner(BWMClientProxy.WINDMILLS)
                .setTextureSize(128, 128)
                .addBox(0, 0, 0, 20, 100, 1, true)
                .setRotationCenter(x, -50, z)
                .setRotateAngle(0, angle + sailAngle, 0)
                .setTexture(TESRWindmill.WINDMILL_SAIL)
        );

        int shaft_height = height + arm_size * 2;
        float shaft_top = shaft_height / 2, shaft_bottom = -shaft_top;
        //Shafts
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(radius - shaft_size, shaft_bottom, -2.0F, shaft_size, shaft_height, shaft_size)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL_SHAFTS)
        );

        //Frame Arms
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(arm_size, top, -arm_size / 2, arm_length, arm_size, arm_size)
                .addBox(arm_size, bottom, -arm_size / 2, arm_length, arm_size, arm_size)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL_FRAME)
        );

        //Frame Border
        part.addChild(new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(radius - (2 * shaft_size), bottom, -26f, arm_size, arm_size, border_length)
                .addBox(radius - (2 * shaft_size), top, -26f, arm_size, arm_size, border_length)
                .setRotationCenter(0.0F, 0.0F, 0.0F)
                .setRotateAngle(0, angle + 0.39F, 0)
                .setTexture(TESRVerticalWindmill.WINDMILL_FRAME)
        );

        return part;
    }
}
