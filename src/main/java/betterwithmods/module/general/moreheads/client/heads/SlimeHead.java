package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.util.ResourceLocation;

public class SlimeHead extends RenderHeadModel<ModelSlime> {
    public SlimeHead() {
        super(new ModelSlime(16),  new ResourceLocation("minecraft:textures/entity/slime/slime.png"));
    }

    @Override
    public float getScale() {
        return super.getScale()*1.5f;
    }
}
