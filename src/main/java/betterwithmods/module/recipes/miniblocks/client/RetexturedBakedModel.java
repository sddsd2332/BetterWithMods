package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.client.baking.WrappedBakedModel;
import betterwithmods.client.model.render.RenderUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class RetexturedBakedModel<T> extends WrappedBakedModel {
    private T object;

    public RetexturedBakedModel(IBakedModel parent, T object) {
        super(parent, null);
        this.object = object;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = super.getQuads(state, side, rand);
        List<BakedQuad> newQuads = Lists.newArrayList();
        for (BakedQuad quad : quads) {
            EnumFacing facing = quad.getFace();
            newQuads.add(RenderUtils.retexture(quad, RenderUtils.getTextureFromFace(getState(object), facing)));
        }
        return newQuads;
    }

    public abstract IBlockState getState(T object);

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return RenderUtils.getParticleTexture(getState(object));
    }
}