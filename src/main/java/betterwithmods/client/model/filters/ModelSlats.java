package betterwithmods.client.model.filters;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelSlats extends ModelWithResource {
    private final ModelRenderer model;

    public ModelSlats(ItemStack slats) {
        super();
        ResourceLocation item = slats.getItem().getRegistryName();
        if (item != null) {
            this.location = new ResourceLocation(item.getResourceDomain(), "textures/blocks/" + item.getResourcePath() + ".png");
        }
        model = new ModelRenderer(this, 0, 0);
        model.setTextureSize(16, 16);
        model.addBox(-5.5F, 5.8F, -8F, 2, 2, 16);
        model.addBox(-2.67F, 5.8F, -8F, 2, 2, 16);
        model.addBox(0.26F, 5.8F, -8F, 2, 2, 16);
        model.addBox(3.19F, 5.8F, -8F, 2, 2, 16);
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
