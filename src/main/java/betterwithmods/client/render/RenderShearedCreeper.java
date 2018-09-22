package betterwithmods.client.render;

import betterwithmods.common.entity.EntityShearedCreeper;
import betterwithmods.lib.ModLib;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;


public class RenderShearedCreeper extends RenderLiving<EntityShearedCreeper> {

    public RenderShearedCreeper(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelCreeper(), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityShearedCreeper entity) {
        return new ResourceLocation(ModLib.MODID, "textures/entity/sheared_creeper.png");
    }
}