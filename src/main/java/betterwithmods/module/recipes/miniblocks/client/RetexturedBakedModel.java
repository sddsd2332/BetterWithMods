package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.client.baking.WrappedBakedModel;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.lib.ModLib;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import scala.tools.nsc.interactive.REPL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class RetexturedBakedModel<T> extends WrappedBakedModel {
    private T object;

    private static final String MISSING_TEXTURE = "missingno";


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
            //Only replace quads with missing textures
            if(MISSING_TEXTURE.equals(quad.getSprite().getIconName()))
                newQuads.add(RenderUtils.retexture(quad, RenderUtils.getTextureFromFace(getState(object), facing)));
            else
                newQuads.add(quad);
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