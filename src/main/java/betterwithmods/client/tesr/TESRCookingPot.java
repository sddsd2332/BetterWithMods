package betterwithmods.client.tesr;

import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.common.tile.TileCauldron;
import betterwithmods.common.tile.TileCookingPot;
import betterwithmods.common.tile.TileCrucible;
import betterwithmods.lib.ModLib;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 3/20/17
 */
public class TESRCookingPot extends TileEntitySpecialRenderer<TileCookingPot> {
    private int occupiedStacks;

    public static final ResourceLocation CAULDRON_CONTENTS = new ResourceLocation(ModLib.MODID, "blocks/cauldron_contents");

    @Override
    public void render(TileCookingPot te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te != null) {
            if (occupiedStacks != te.filledSlots())
                occupiedStacks = te.filledSlots();
            if (occupiedStacks != 0) {
                float fillOffset = 0.75F * occupationMod(te);
                RenderUtils.renderFill(getResource(te), te.getPos(), x, y, z, 0.123D, 0.125D, 0.123D, 0.877D, 0.248D + fillOffset, 0.877D);
            }
        }
    }

    private ResourceLocation getResource(TileCookingPot tile) {
        boolean stoked = tile.getHeat(tile.getBlockWorld(), tile.getBlockPos()) >= BWMHeatRegistry.STOKED_HEAT;

        if (tile instanceof TileCauldron) {
            return CAULDRON_CONTENTS;
        } else if (tile instanceof TileCrucible) {
            return stoked ? new ResourceLocation("minecraft", "blocks/lava_still") : new ResourceLocation("minecraft", "blocks/gravel");
        }
        return null;
    }

    private float occupationMod(TileCookingPot tile) {
        float visibleSlots = (float) tile.getMaxVisibleSlots();
        return (float) occupiedStacks / visibleSlots;
    }

}
