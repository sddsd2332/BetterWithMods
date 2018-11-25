package betterwithmods.module.general.moreheads.client;

import betterwithmods.module.general.moreheads.common.HeadType;
import betterwithmods.module.general.moreheads.common.ItemHead;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class TEISRHead extends TileEntityItemStackRenderer {

    @Override
    public void renderByItem(@Nonnull ItemStack stack) {
        HeadType type = ItemHead.getHeadType(stack);
        if (type != null) {
            RenderHandler.render(type);
        }
    }
}
