package betterwithmods.client.tesr;

import betterwithmods.client.model.generators.ModelVerticalWindmill;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.tile.TileWindmillVertical;
import betterwithmods.lib.ModLib;
import betterwithmods.library.utils.BannerUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TESRVerticalWindmill extends TileEntitySpecialRenderer<TileWindmillVertical> {
    public static final ResourceLocation WINDMILL_SHAFTS = new ResourceLocation(ModLib.MODID, "textures/blocks/windmills/vertical_shaft.png");
    public static final ResourceLocation WINDMILL_FRAME = new ResourceLocation(ModLib.MODID, "textures/blocks/windmills/vertical_frame.png");
    public static final ResourceLocation WINDMILL_SAIL = new ResourceLocation(ModLib.MODID, "textures/blocks/windmills/sail.png");

    private static ModelVerticalWindmill render = new ModelVerticalWindmill();

    public static void renderWindmill(float rotation, double x, double y, double z, float alpha, @Nullable BannerUtils.BannerData[] data) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        //Why is it upside down?
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        if (data != null) {
            for (int i = 0; i < render.bladeCount; i++) {
                render.setBanner(i, data[i]);
            }
        }

        render.setAngle(0, (float) Math.toRadians(rotation), 0);
        render.render(0.0625f);
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
    }

    @Override
    public void render(TileWindmillVertical te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float rotation = -(te.getCurrentRotation() + (te.getMechanicalOutput(EnumFacing.UP) == 0 ? 0 : partialTicks * te.getPrevRotation()));
        BlockPos pos = te.getBlockPos();
        RenderUtils.renderDebugBoundingBox(x, y, z, te.getRenderBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ()));
        renderWindmill(rotation, x, y, z, alpha, te.getData());
    }

}
