package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelSheepHead;
import net.minecraft.util.ResourceLocation;

public class SheepHead extends RenderHeadModel<ModelSheepHead> {
    public SheepHead() {
        super(new ModelSheepHead(), new ResourceLocation("minecraft:textures/entity/sheep/sheep.png"));
    }
}
