package betterwithmods.client.model.filters;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelGrate extends ModelWithResource {
    private final ModelRenderer model;

    public ModelGrate(ItemStack grate) {
        ResourceLocation item = grate.getItem().getRegistryName();
        if (item != null) {
            this.location = new ResourceLocation(item.getResourceDomain(), "textures/blocks/" + item.getResourcePath() + ".png");
        }
        model = new ModelRenderer(this, 0, 0);
        model.setTextureSize(16, 16);
        model.addBox(-5F, 5.8F, -8F, 1, 1, 16);
        model.addBox(-0.5F, 5.8F, -8F, 1, 1, 16);
        model.addBox(4F, 5.8F, -8F, 1, 1, 16);
        model.addBox(-8F, 5.75F, -5F, 16, 1, 1);
        model.addBox(-8F, 5.75F, -0.5F, 16, 1, 1);
        model.addBox(-8F, 5.75F, 4F, 16, 1, 1);
    }

    @Override
    public void renderModels(float scale) {
        model.render(scale);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
