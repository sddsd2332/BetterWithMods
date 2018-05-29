package betterwithmods.client.gui;

import betterwithmods.api.util.IProgressSource;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiProgress extends GuiBase {

    private IProgressSource progressSource;

    public GuiProgress(Container inventorySlotsIn, ResourceLocation background, IProgressSource progressSource) {
        super(inventorySlotsIn, background);
        this.progressSource = progressSource;
    }

    @Override
    protected void drawExtras(float partialTicks, int mouseX, int mouseY, int centerX, int centerY) {
        if (progressSource.showProgress()) {
            int progress = toPixels();
            drawTexturedModalRect(
                    centerX + getX(),
                    centerY + getY() + getHeight() - progress,
                    getTextureX(),
                    getTextureY() - progress,
                    getWidth(),
                    getHeight());
        }
    }


    protected double getPercentage() {
        return (double) progressSource.getProgress() / (double) progressSource.getMax();
    }

    protected abstract int toPixels();

    public abstract int getX();

    public abstract int getY();

    public abstract int getTextureX();

    public abstract int getTextureY();

    public abstract int getHeight();

    public abstract int getWidth();



}
