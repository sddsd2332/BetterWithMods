package betterwithmods.client.model.generators;

import betterwithmods.util.BannerUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModelRadialBlades extends ModelBase {

    protected final int bladeCount;
    private BannerUtils.BannerData[] banners;
    private ModelPart base;

    public ModelRadialBlades(int bladeCount) {
        this.bladeCount = bladeCount;
        this.base = new ModelPart(this, 0, 0);
        for (int i = 0; i < bladeCount; i++) {
            base.addChild(createBlade(i));
        }
        banners = new BannerUtils.BannerData[this.bladeCount];
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
                        if (r.hasBanner()) {
                            r.setBanner(banners[i]);
                            i++;
                        }
                    }
                }
            }
        }
        this.base.render(scale);
    }

    public void setBanner(int index, BannerUtils.BannerData data) {
        this.banners[index] = data;
    }

}
