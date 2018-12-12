package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.lib.ModLib;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import javax.annotation.Nonnull;

public class DynamicModelLoader implements ICustomModelLoader {

    private DynamicType type;

    public DynamicModelLoader(DynamicType type) {
        this.type = type;
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {

    }

    @Override
    public boolean accepts(@Nonnull ResourceLocation modelLocation) {
        return modelLocation.getNamespace().equals(ModLib.MODID) && modelLocation.getPath().startsWith(type.getName());
    }

    @Override
    public IModel loadModel(@Nonnull ResourceLocation modelLocation) throws Exception {
        return RenderUtils.getModel(new ResourceLocation(ModLib.MODID, type.getName()));
    }
}
