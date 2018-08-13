package betterwithmods.client.model.generators;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModelRadialBlades extends ModelBase {

    protected final int bladeCount;
    private float[][] colors;
    private ModelPart base;

    public ModelRadialBlades(int bladeCount) {
        this.bladeCount = bladeCount;
        this.base = new ModelPart(this, 0, 0);
        for (int i = 0; i < bladeCount; i++) {
            base.addChild(createBlade(i));
        }
        colors = new float[bladeCount][3];
    }

    protected abstract ModelPart createBlade(int index);

    public void setAngle(double x, double y, double z) {
        this.base.setRotateAngle(x, y, z);
    }

    public void render(float scale) {
        if (this.base.childModels != null) {
            int i = 0;
            for (ModelRenderer r1 : this.base.childModels) {
                if (r1.childModels != null) {
                    for (ModelRenderer r2 : r1.childModels) {
                        ModelPart r = (ModelPart) r2;
                        if (r.shouldColor()) {
                            r.setColor(colors[i]);
                            i++;
                        }
                    }
                }
            }
        }
        this.base.render(scale);
    }

    public void setColor(int index, float[] color) {
        this.colors[index % this.colors.length] = color;
    }

}
