package betterwithmods.module.hardcore.world.stumping.ctm;

import betterwithmods.module.hardcore.world.stumping.HCStumping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import team.chisel.ctm.client.texture.ctx.TextureContextPosition;

import javax.annotation.Nonnull;

public class TextureContextStump extends TextureContextPosition {
    public TextureContextStump(@Nonnull BlockPos pos) {
        super(pos);
    }

    public int isStump() {
        WorldClient world = Minecraft.getMinecraft().world;
        return HCStumping.isStump(world,position) ? 1 : 0;
    }

    @Override
    public long getCompressedData() {
        return isStump();
    }
}