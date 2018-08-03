package betterwithmods.module.hardcore.world.stumping;

import team.chisel.ctm.api.texture.ITextureContext;

public class TextureContextStump implements ITextureContext {


    private boolean stump;

    public TextureContextStump(boolean stump) {
        this.stump = stump;
    }

    @Override
    public long getCompressedData() {
        return stump ? 1 : 0;
    }
}