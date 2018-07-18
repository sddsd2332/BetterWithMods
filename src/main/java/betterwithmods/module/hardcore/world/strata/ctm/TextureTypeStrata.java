package betterwithmods.module.hardcore.world.strata.ctm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

import javax.annotation.Nonnull;

@TextureType(value = "bwm_strata")
public class TextureTypeStrata implements ITextureType {


    @SuppressWarnings("unchecked")
    @Override
    public TextureStrata makeTexture(@Nonnull TextureInfo info) {
        return new TextureStrata(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull ICTMTexture<?> tex) {
        return new TextureContextStrata(pos);
    }

    @Override
    public ITextureContext getContextFromData(long data) {
        return new TextureContextStrata(BlockPos.fromLong(data));
    }

    @Override
    public int requiredTextures() {
        return 3;
    }
}
