package betterwithmods.manual.client.manual.provider;

import betterwithmods.manual.api.API;
import betterwithmods.manual.api.manual.ImageProvider;
import betterwithmods.manual.api.manual.ImageRenderer;
import betterwithmods.manual.client.manual.segment.render.MissingItemRenderer;
import betterwithmods.manual.client.manual.segment.render.TextureImageRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TextureImageProvider implements ImageProvider {
    private static final String WARNING_IMAGE_MISSING = API.MOD_ID + ".manual.warning.missing.image";

    private final String images;

    public TextureImageProvider(String images) {
        this.images = images;
    }

    @Override
    @Nullable
    public ImageRenderer getImage(@Nonnull final String data) {
        try {
            ResourceLocation loc = new ResourceLocation(data);

            return new TextureImageRenderer(new ResourceLocation(loc.getResourceDomain(), images + loc.getResourcePath()));
        } catch (final Throwable t) {
            return new MissingItemRenderer(WARNING_IMAGE_MISSING);
        }
    }
}
