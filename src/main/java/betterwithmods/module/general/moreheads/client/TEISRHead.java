package betterwithmods.module.general.moreheads.client;

import betterwithmods.module.general.moreheads.common.HeadType;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TEISRHead extends TileEntityItemStackRenderer {

    @Override
    public void renderByItem(@Nonnull ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey("type")) {
                HeadType type = HeadType.VALUES[tag.getInteger("type")];
                RenderHandler.render(type);
            }
        }
    }
}
