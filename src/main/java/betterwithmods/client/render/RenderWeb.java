package betterwithmods.client.render;

import betterwithmods.common.entity.EntitySpiderWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class RenderWeb extends RenderSnowball<EntitySpiderWeb> {
    public RenderWeb(RenderManager renderManagerIn) {
        super(renderManagerIn, Item.getItemFromBlock(Blocks.WEB), Minecraft.getMinecraft().getRenderItem());
    }
}
