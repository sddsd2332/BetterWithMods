package betterwithmods.module.hardcore.world.stumping.ctm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

import javax.annotation.Nonnull;

@TextureType(value = "bwm_stump")
public class TextureTypeStump implements ITextureType {

    @SuppressWarnings("unchecked")
    @Override
    public TextureStump makeTexture(@Nonnull TextureInfo info) {
        return new TextureStump(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull ICTMTexture<?> tex) {
        return new TextureContextStump(pos);
    }

    @Override
    public ITextureContext getContextFromData(long data) {
        return new TextureContextStump(BlockPos.fromLong(data));
    }

    @Override
    public int requiredTextures() {
        return 2;
    }
}
