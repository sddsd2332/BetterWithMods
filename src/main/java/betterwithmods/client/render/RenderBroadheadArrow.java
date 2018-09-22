package betterwithmods.client.render;

import betterwithmods.common.entity.EntityBroadheadArrow;
import betterwithmods.lib.ModLib;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/18/16
 */
public class RenderBroadheadArrow extends RenderArrow<EntityBroadheadArrow> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/entity/broadhead_arrow.png");

    public RenderBroadheadArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBroadheadArrow entity) {
        return TEXTURE;
    }
}
