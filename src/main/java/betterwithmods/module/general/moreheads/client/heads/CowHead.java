package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelCowHead;
import net.minecraft.util.ResourceLocation;

public class CowHead extends RenderHeadModel<ModelCowHead> {
    public CowHead(ResourceLocation texture) {
        super(new ModelCowHead(), texture);
    }

    public CowHead() {
        this(new ResourceLocation("minecraft:textures/entity/cow/cow.png"));
    }
}
