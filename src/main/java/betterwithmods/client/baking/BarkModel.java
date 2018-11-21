package betterwithmods.client.baking;

import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.blocks.camo.CamoInfo;
import betterwithmods.lib.ModLib;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class BarkModel extends ModelFactory<CamoInfo> {

    public static final ModelResourceLocation LOCATION = new ModelResourceLocation(new ResourceLocation(ModLib.MODID, "bark"), "inventory");

    public static BarkModel BARK;

    public final IModel template;

    public BarkModel(IModel template) {
        super(BlockDynamic.CAMO_INFO, TextureMap.LOCATION_MISSING_TEXTURE);
        this.template = template;
    }

    @Override
    public IBakedModel bake(CamoInfo object, boolean isItem, BlockRenderLayer layer) {
        ImmutableMap.Builder<String, String> textures = new ImmutableMap.Builder<>();
        textures.put("layer0", RenderUtils.getParticleTexture(object.getState()).getIconName());
        TRSRTransformation state = TRSRTransformation.from(ModelRotation.X0_Y0);
        IModel retexture = template.retexture(textures.build()).uvlock(true);
        return new WrappedBakedModel(retexture.bake(state, DefaultVertexFormats.BLOCK, RenderUtils.textureGetter), RenderUtils.getParticleTexture(object.getState())).addDefaultBlockTransforms();
    }

    @Override
    public CamoInfo fromItemStack(ItemStack stack) {
        return new CamoInfo(stack);
    }
}
