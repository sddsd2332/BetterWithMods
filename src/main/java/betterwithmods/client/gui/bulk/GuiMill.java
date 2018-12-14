package betterwithmods.client.gui.bulk;

import betterwithmods.common.container.bulk.ContainerMill;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiProgress;
import betterwithmods.library.utils.LocaleUtils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

import static betterwithmods.lib.TooltipLib.MILLSTONE_BLOCKED;

public class GuiMill extends GuiProgress<ContainerMill> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/mill.png");

    public GuiMill(ContainerMill container) {
        super(container, TEXTURE, 80, 18, 176, 14, 14, 14);
        this.ySize = 158;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public int getTitleY() {
        return 6;
    }

    @Override
    protected void drawExtras(float partialTicks, int mouseX, int mouseY, int centerX, int centerY) {
        super.drawExtras(partialTicks, mouseX, mouseY, centerX, centerY);
        if (getContainer().getPropertyValue(ContainerMill.BLOCKED)) {
            String str = LocaleUtils.getMessage(ModLib.MODID, MILLSTONE_BLOCKED);
            int width = fontRenderer.getStringWidth(str) / 2;
            drawString(fontRenderer, str, centerX + this.xSize / 2 - width, centerY + 32, EnumDyeColor.RED.getColorValue());
            drawToolTip(mouseX, mouseY, centerX + this.xSize / 2 - width, centerY + 32, 32, 32,
                    LocaleUtils.getTooltip(ModLib.MODID, MILLSTONE_BLOCKED));
        }
    }

    private void drawToolTip(int mouseX, int mouseY, int x, int y, int w, int h, String text) {
        if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= (y + h)) {
            drawHoveringText(text, mouseX, mouseY);
        }
    }

}
