package betterwithmods.module.general.moreheads.client.heads;

import betterwithmods.module.general.moreheads.client.RenderHeadModel;
import betterwithmods.module.general.moreheads.client.model.ModelVillagerHead;
import net.minecraft.util.ResourceLocation;

public class VillagerHead extends RenderHeadModel<ModelVillagerHead> {
    public VillagerHead(ResourceLocation texture, int width, int height) {
        super(new ModelVillagerHead(width, height), texture);
    }

    public VillagerHead() {
        this(new ResourceLocation("minecraft:textures/entity/villager/villager.png"), 64, 64);
    }
}
