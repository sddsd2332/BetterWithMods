package betterwithmods.client.tesr;

import betterwithmods.BWMod;
import betterwithmods.client.model.generators.ModelHorizontalWindmill;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.tile.TileWindmillHorizontal;
import betterwithmods.util.BannerUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TESRWindmill extends TileEntitySpecialRenderer<TileWindmillHorizontal> {
    public static final ResourceLocation WINDMILL = new ResourceLocation(BWMod.MODID, "textures/blocks/horizontal_windmill.png");
    public static final ResourceLocation WINDMILL_SAIL = new ResourceLocation(BWMod.MODID, "textures/blocks/horizontal_windmill_sail.png");
    private static ModelHorizontalWindmill render = new ModelHorizontalWindmill();

    public static void renderWindmill(float direction, float rotation, double x, double y, double z, float alpha, @Nullable BannerUtils.BannerData[] data) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        GlStateManager.rotate(direction, 0.0F, 1.0F, 0.0F);
        if(data != null) {
            for (int i = 0; i < data.length; i++) {
                render.setBanner(i, data[i]);
            }
        }
        render.setAngle(0, 0, -(float) Math.toRadians(rotation));
        render.render(0.0625F);
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
    }

    @Override
    public void render(TileWindmillHorizontal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        BlockPos pos = te.getBlockPos();
        RenderUtils.renderDebugBoundingBox(x, y, z, te.getRenderBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ()));
        EnumFacing facing = te.getOrientation();
        float rotation = (te.getCurrentRotation() + (te.getMechanicalOutput(facing) == 0 ? 0 : partialTicks * te.getPrevRotation()));
        renderWindmill((facing == EnumFacing.SOUTH ? 180 : 90f), rotation, x, y, z, alpha, te.getData());
    }


}
