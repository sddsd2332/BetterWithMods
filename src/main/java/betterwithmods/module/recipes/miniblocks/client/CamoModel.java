package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.client.baking.ModelFactory;
import betterwithmods.client.baking.WrappedBakedModel;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.blocks.camo.CamoInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class CamoModel extends ModelFactory<CamoInfo> {
    protected final IBakedModel wrapped;

    public CamoModel(IBakedModel wrapped) {
        super(BlockDynamic.CAMO_INFO, TextureMap.LOCATION_MISSING_TEXTURE);
        this.wrapped = wrapped;
    }

    @Override
    public IBakedModel bake(CamoInfo object, boolean isItem, BlockRenderLayer layer) {
        return new BakedCamo(wrapped, object).addDefaultBlockTransforms();
    }

    @Override
    public CamoInfo fromItemStack(ItemStack stack) {
        return new CamoInfo(stack);
    }

    private class BakedCamo extends RetexturedBakedModel<CamoInfo> {
        public BakedCamo(IBakedModel parent, CamoInfo object) {
            super(parent, object);
        }

        @Override
        public IBlockState getState(CamoInfo object) {
            return object.getState();
        }
    }



}
