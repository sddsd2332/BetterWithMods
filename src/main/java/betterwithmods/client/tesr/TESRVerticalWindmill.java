package betterwithmods.client.tesr;

import betterwithmods.BWMod;
import betterwithmods.client.model.ModelVerticalFrame;
import betterwithmods.client.model.ModelVerticalSails;
import betterwithmods.client.model.ModelVerticalShafts;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.tile.TileWindmillVertical;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TESRVerticalWindmill extends TileEntitySpecialRenderer<TileWindmillVertical> {
    public static final ResourceLocation WINDMILL_SHAFTS = new ResourceLocation(BWMod.MODID, "textures/blocks/vertical_windmill_shaft.png");
    public static final ResourceLocation WINDMILL = new ResourceLocation(BWMod.MODID, "textures/blocks/vertical_windmill.png");
    public static final ResourceLocation WINDMILL_SAIL = new ResourceLocation(BWMod.MODID, "textures/blocks/vertical_windmill_sail.png");
    private static final ModelVerticalShafts modelShafts = new ModelVerticalShafts();
    private static final ModelVerticalSails modelSails = new ModelVerticalSails();
    private static final ModelVerticalFrame modelFrame = new ModelVerticalFrame();

    public static void renderWindmill(float rotation, double x, double y, double z, int[] colors) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        modelShafts.setRotateAngle(modelShafts.axle, 0, (float) Math.toRadians(rotation), 0);
        modelSails.setRotateAngleForSails(0, (float) Math.toRadians(rotation), 0);
        modelFrame.setRotateAngle(modelFrame.axle, 0, (float) Math.toRadians(rotation), 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(WINDMILL_SHAFTS);
        modelShafts.render(0.0625F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(WINDMILL);
        modelFrame.render(0.0625F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(WINDMILL_SAIL);
        modelSails.render(0.0625F, colors);
        GlStateManager.popMatrix();
    }

    @Override
    public void render(TileWindmillVertical te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float rotation = -(te.getCurrentRotation() + (te.getMechanicalOutput(EnumFacing.UP) == 0 ? 0 : partialTicks * te.getPrevRotation()));
        BlockPos pos = te.getBlockPos();
        RenderUtils.renderDebugBoundingBox(x, y, z, te.getRenderBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ()));
        renderWindmill(rotation, x, y, z, te.getColors());
    }

}
