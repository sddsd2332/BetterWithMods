package betterwithmods.client.render;

import net.minecraft.client.renderer.culling.ICamera;
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

    }

    @Override
    public boolean shouldRender(Entity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return null;
    }
}
