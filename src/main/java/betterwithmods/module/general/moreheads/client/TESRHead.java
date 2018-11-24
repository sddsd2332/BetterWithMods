package betterwithmods.module.general.moreheads.client;

import betterwithmods.module.general.moreheads.common.TileHead;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TESRHead extends TileEntitySpecialRenderer<TileHead> {

    @Override
    public void render(TileHead te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        RenderHandler.render(te.getType(), x + 0.5, y, z + 0.5, partialTicks, te.getRotation());
    }


}
