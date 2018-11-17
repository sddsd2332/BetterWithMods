package betterwithmods.client.render;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.EntityDynamite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;

public class RenderDynamite extends RenderSnowball<EntityDynamite> {
    public RenderDynamite(RenderManager renderManagerIn) {
        super(renderManagerIn, BWMItems.DYNAMITE, Minecraft.getMinecraft().getRenderItem());
    }
}
