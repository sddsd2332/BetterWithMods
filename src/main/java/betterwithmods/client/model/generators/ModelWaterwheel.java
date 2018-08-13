package betterwithmods.client.model.generators;

import betterwithmods.client.tesr.TESRWaterwheel;
import net.minecraft.util.math.MathHelper;

public class ModelWaterwheel extends ModelRadialBlades {

    public ModelWaterwheel() {
        super(8);
    }

    @Override
    protected ModelPart createBlade(int index) {
        double rotation = (Math.PI * index) / (bladeCount / 2);

        ModelPart combo = new ModelPart(this, 0, 0);

        ModelPart paddle = new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(2.5F, -1.0F, -7.0F, 36, 2, 14)
                .setRotationCenter(0F, 0F, 0F)
                .setRotateAngle(0, 0, rotation);
        combo.addChild(paddle);

        ModelPart blade = new ModelPart(this, 0, 0)
                .setTextureSize(16, 16)
                .addBox(0.0F, -1.0F, -6.0F, 22, 2, 12)
                .setRotationCenter(30.0F * MathHelper.cos((float) rotation), 30.0F * MathHelper.sin((float) rotation), 0.0F)
                .setRotateAngle(0, 0, 1.96 + rotation);

        combo.addChild(blade);

        combo.setTexture(TESRWaterwheel.WATERWHEEL);
        return combo;
    }
}
