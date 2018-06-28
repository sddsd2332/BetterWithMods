package betterwithmods.client.render;

import betterwithmods.client.model.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderInvisible extends Render<Entity> {

    public RenderInvisible(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        RenderUtils.renderDebugBoundingBox(x, y, z, Block.FULL_BLOCK_AABB.offset(entity.getPosition()));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return null;
    }
}
