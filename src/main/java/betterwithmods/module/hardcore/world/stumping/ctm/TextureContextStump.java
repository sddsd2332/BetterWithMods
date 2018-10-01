package betterwithmods.module.hardcore.world.stumping.ctm;

import team.chisel.ctm.api.texture.ITextureContext;

public class TextureContextStump implements ITextureContext {


    private int stump;

    public TextureContextStump(int stump) {
        this.stump = stump;
    }

    @Override
    public long getCompressedData() {
        return stump;
    }
}