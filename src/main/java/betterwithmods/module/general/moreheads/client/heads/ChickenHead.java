package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelChickenHead;
import net.minecraft.util.ResourceLocation;

public class ChickenHead extends RenderHeadModel<ModelChickenHead> {
    public ChickenHead() {
        super(new ModelChickenHead(), new ResourceLocation("minecraft:textures/entity/chicken.png"));
    }
}
