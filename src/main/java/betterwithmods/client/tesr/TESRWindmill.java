package betterwithmods.client.tesr;

import betterwithmods.BWMod;
import betterwithmods.client.model.generators.ModelHorizontalWindmill;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.tile.TileWindmillHorizontal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TESRWindmill extends TileEntitySpecialRenderer<TileWindmillHorizontal> {
    public static final ResourceLocation WINDMILL = new ResourceLocation(BWMod.MODID, "textures/blocks/horizontal_windmill.png");
    public static final ResourceLocation WINDMILL_SAIL = new ResourceLocation(BWMod.MODID, "textures/blocks/horizontal_windmill_sail.png");
    private static ModelHorizontalWindmill windmill = new ModelHorizontalWindmill();

    public static void renderWindmill(float direction, float rotation, double x, double y, double z, int[] colors) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        GlStateManager.rotate(direction, 0.0F, 1.0F, 0.0F);
        //TODO colors
        windmill.setAngle(0, 0, -(float) Math.toRadians(rotation));
        windmill.setColor(0, new float[]{1, 0, 0});
        windmill.render(0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    public void render(TileWindmillHorizontal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        BlockPos pos = te.getBlockPos();
        RenderUtils.renderDebugBoundingBox(x, y, z, te.getRenderBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ()));
        EnumFacing facing = te.getOrientation();
        float rotation = (te.getCurrentRotation() + (te.getMechanicalOutput(facing) == 0 ? 0 : partialTicks * te.getPrevRotation()));
        renderWindmill((facing == EnumFacing.SOUTH ? 180 : 90f), rotation, x, y, z, null);
    }


}
