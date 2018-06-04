package betterwithmods.module.hardcore.world.stumping;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

@TextureType(value = "bwm_stump")
public class TextureTypeStump implements ITextureType {

    @Override
    public TextureStump makeTexture(TextureInfo info) {
        return new TextureStump(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, ICTMTexture<?> tex) {
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
