package betterwithmods.client.tesr;

import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.blocks.BlockFurnace;
import betterwithmods.common.blocks.BlockInfernalEnchanter;
import betterwithmods.common.tile.TileInfernalEnchanter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class TESRInfernalEnchanter extends TileEntitySpecialRenderer<TileInfernalEnchanter> {

    @Override
    public void render(TileInfernalEnchanter tileEnchanter, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack activeItem = tileEnchanter.getActiveItem();
        if (!activeItem.isEmpty()) {
            IBlockState state = tileEnchanter.getWorld().getBlockState(tileEnchanter.getPos());
            if (state.getBlock() instanceof BlockInfernalEnchanter) {
                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
                GlStateManager.enableBlend();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                GlStateManager.pushMatrix();
                double offset = Math.sin((tileEnchanter.getWorld().getTotalWorldTime() + partialTicks) / 8) / 8.0;
                GlStateManager.translate(x + 0.5, y + .75 + offset, z + 0.5);
                GlStateManager.rotate((tileEnchanter.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

                IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(activeItem, tileEnchanter.getWorld(), null);
                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(activeItem, model);

                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }


    }
}
