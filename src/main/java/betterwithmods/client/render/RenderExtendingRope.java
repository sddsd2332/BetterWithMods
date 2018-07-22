package betterwithmods.client.render;

import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockBucket;
import betterwithmods.common.entity.EntityExtendingRope;
import betterwithmods.common.tile.TileBucket;
import betterwithmods.util.AABBArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.entity.EntityExtendingRope;
import betterwithmods.util.AABBArray;

/**
 * @author mrebhan
 */

public class RenderExtendingRope extends Render<EntityExtendingRope> {

    public RenderExtendingRope(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityExtendingRope entity) {
        return null;
    }

    @Override
    public void doRender(@Nonnull EntityExtendingRope entity, double x, double y, double z, float entityYaw,
                         float partialTicks) {
        World world = entity.getEntityWorld();
        IBlockState iblockstate = BWMBlocks.ROPE.getDefaultState();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
        } else {
            GlStateManager.shadeModel(7424);
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
        BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ);
        GlStateManager.translate(x - blockpos.getX() - 0.5, (float) (y - (double) blockpos.getY()), z - blockpos.getZ() - 0.5);
        GlStateManager.translate(0, (entity.posY - entity.prevPosY) * partialTicks, 0);
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

        int i = 0;
        while (entity.getPulleyPosition().getY() - entity.posY > i && i < 2) {
            blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos.up(i), vertexbuffer,
                    false, 0);
            i++;
        }

        IBlockState state;

        for (Vec3i vec : entity.getBlocks().keySet()) {
            state = entity.getBlocks().get(vec);
            blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
                    blockrendererdispatcher.getModelForState(state), state, blockpos.add(vec), vertexbuffer, false, 0);
        }

        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();

        RenderUtils.renderDebugBoundingBox(x, y, z, AABBArray.getParts(entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ)));

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

}
