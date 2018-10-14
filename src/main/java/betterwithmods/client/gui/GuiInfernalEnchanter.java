package betterwithmods.client.gui;

import betterwithmods.common.container.other.ContainerInfernalEnchanter;
import betterwithmods.common.tile.TileInfernalEnchanter;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiBase;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by primetoxinz on 9/11/16.
 */
public class GuiInfernalEnchanter extends GuiBase<ContainerInfernalEnchanter> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/infernal_enchanter.png");
    private static final HashMap<Integer, String> numerals = Maps.newHashMap();

    static {
        numerals.put(22, "I");
        numerals.put(41, "II");
        numerals.put(60, "III");
        numerals.put(79, "IV");
        numerals.put(98, "V");
    }

    private FontRenderer fontGalactic;

    public GuiInfernalEnchanter(ContainerInfernalEnchanter container) {
        super(container, TEXTURE);
        this.ySize = 211;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (fontGalactic == null)
            fontGalactic = this.mc.standardGalacticFontRenderer;
    }

    @Override
    protected void drawExtras(float partialTicks, int mouseX, int mouseY, int centerX, int centerY) {
        if (!inventorySlots.getSlot(0).getHasStack())
            drawTexturedModalRect(centerX + 17, centerY + 37, 176, 0, 16, 16);
        if (!inventorySlots.getSlot(1).getHasStack())
            drawTexturedModalRect(centerX + 17, centerY + 75, 192, 0, 16, 16);
        EnchantmentNameParts.getInstance().reseedRandomGenerator((long) getContainer().xpSeed);

        int x, y;
        for (int levelIndex = 0; levelIndex < getContainer().enchantLevels.length; levelIndex++) {
            this.mc.renderEngine.bindTexture(background);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int level = getContainer().enchantLevels[levelIndex];
            if (level > 0) {
                String levelString = String.valueOf(level);
                if (getContainer().hasLevels(getContainer().getPlayer(), levelIndex) && getContainer().hasBooks(levelIndex)) {
                    y = centerY + 17 + (19 * levelIndex);
                    x = centerX + 60;
                    if (mouseX >= x && mouseX <= x + 108 && mouseY >= y && mouseY <= y + 19) {
                        drawTexturedModalRect(x, y, 108, 211, 108, 19);
                    } else {
                        drawTexturedModalRect(x, y, 0, 211, 108, 19);
                    }
                }
                String galacticText = EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, 86 - this.fontRenderer.getStringWidth(levelString));
                fontGalactic.drawSplitString(galacticText, centerX + 62, centerY + 19 + 19 * levelIndex, 108, 0x342f25);
                fontRenderer.drawStringWithShadow(levelString, centerX + xSize - 10 - this.fontRenderer.getStringWidth(levelString), centerY + 8 + 19 * (levelIndex + 1), 0x80FF20);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        for (Map.Entry<Integer, String> e : numerals.entrySet()) {
            this.fontRenderer.drawString(e.getValue(), 50 - this.fontRenderer.getStringWidth(e.getValue()) / 2, e.getKey(), 0x404040);
        }
    }

    @Override
    public String getTitle() {
        return getContainer().getTile().getName();
    }

    @Override
    public int getTitleY() {
        return 6;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        int x, y;
        for (int levelIndex = 0; levelIndex < getContainer().enchantLevels.length; levelIndex++) {
            if (getContainer().enchantLevels[levelIndex] > -1 && getContainer().hasLevels(getContainer().getPlayer(), levelIndex) && getContainer().hasBooks(levelIndex)) {
                y = yPos + 17 + (19 * levelIndex);
                x = xPos + 60;
                if (mouseX >= x && mouseX <= x + 108 && mouseY >= y && mouseY <= y + 19) {
                    if (getContainer().enchantItem(getContainer().getPlayer(), levelIndex)) {
                        this.mc.playerController.sendEnchantPacket(getContainer().windowId, levelIndex);
                    }
                }
            }
        }
    }
}

