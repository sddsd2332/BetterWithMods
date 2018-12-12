package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.lib.ModLib;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class DynamicStateMapper extends StateMapperBase {

    private DynamicType type;

    public DynamicStateMapper(DynamicType type) {
        this.type = type;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        return new ModelResourceLocation(new ResourceLocation(ModLib.MODID , this.type.getName()), this.getPropertyString(state.getProperties()));
    }
}
