package betterwithmods.manual.client.manual.segment;

import betterwithmods.manual.common.api.ManualDefinitionImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.net.URI;
import java.util.Optional;

public final class LinkSegment extends TextSegment implements InteractiveSegment {
    private static final int NORMAL_COLOR = 0x333399;
    private static final int NORMAL_COLOR_HOVER = 0x6666CC;
    private static final int ERROR_COLOR = 0x993333;
    private static final int ERROR_COLOR_HOVER = 0xCC6666;
    private static final int FADE_TIME = 500;

    private final String url;
    private boolean isLinkValid; // Lazy computation.
    private boolean isLinkValidInitialized;
    private long lastHovered = System.currentTimeMillis() - FADE_TIME;

    public LinkSegment(final Segment parent, final String text, final String url) {
        super(parent, text);
        this.url = url;
    }

    private boolean isLinkValid() {
        if (!isLinkValidInitialized) {
            isLinkValid = (url.startsWith("http://") || url.startsWith("https://")) ||
                    myManual.contentFor(ManualDefinitionImpl.makeRelative(url, myManual.peekPath())) != null;
            isLinkValidInitialized = true;
        }
        return isLinkValid;
    }

    @Override
    protected Optional<Integer> color() {
        final int color, hoverColor;
        if (isLinkValid()) {
            color = NORMAL_COLOR;
            hoverColor = NORMAL_COLOR_HOVER;
        } else {
            color = ERROR_COLOR;
            hoverColor = ERROR_COLOR_HOVER;
        }

        final int timeSinceHover = (int) (System.currentTimeMillis() - lastHovered);
        if (timeSinceHover > FADE_TIME) {
            return Optional.of(color);
        } else {
            return Optional.of(fadeColor(hoverColor, color, timeSinceHover / (float) FADE_TIME));
        }
    }

    @Override
    public Optional<String> tooltip() {
        return Optional.of(url);
    }

    @Override
    public boolean onMouseClick(final int mouseX, final int mouseY) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            handleUrl(url);
        } else {
            myManual.navigate(ManualDefinitionImpl.makeRelative(url, myManual.peekPath()));
        }
        return true;
    }

    @Override
    public void notifyHover() {
        lastHovered = System.currentTimeMillis();
    }

    private static int fadeColor(final int c1, final int c2, final float t) {
        final int r1 = (c1 >>> 16) & 0xFF;
        final int g1 = (c1 >>> 8) & 0xFF;
        final int b1 = c1 & 0xFF;
        final int r2 = (c2 >>> 16) & 0xFF;
        final int g2 = (c2 >>> 8) & 0xFF;
        final int b2 = c2 & 0xFF;
        final int r = (int) (r1 + (r2 - r1) * t);
        final int g = (int) (g1 + (g2 - g1) * t);
        final int b = (int) (b1 + (b2 - b1) * t);
        return (r << 16) | (g << 8) | b;
    }

    private static void handleUrl(final String url) {
        // Pretty much copy-paste from GuiChat.
        try {
            final Class<?> desktop = Class.forName("java.awt.Desktop");
            final Object instance = desktop.getMethod("getDesktop").invoke(null);
            desktop.getMethod("browse", URI.class).invoke(instance, new URI(url));
        } catch (final Throwable t) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(t.toString()));
        }
    }

    @Override
    public String toString() {
        return String.format("[%s](%s)", text(), url);
    }
}
