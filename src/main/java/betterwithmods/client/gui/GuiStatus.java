package betterwithmods.client.gui;

import betterwithmods.BWMod;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.penalties.Penalty;
import betterwithmods.common.penalties.PenaltyHandler;
import betterwithmods.util.TooltipLib;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by primetoxinz on 5/13/17.
 */
@Mod.EventBusSubscriber(modid = BWMod.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class GuiStatus {

    @SideOnly(Side.CLIENT)
    public static final GuiStatus INSTANCE = new GuiStatus();
    public static boolean isGloomLoaded, isHungerLoaded, isInjuryLoaded;
    public static int offsetX, offsetY;
    private final Minecraft mc = Minecraft.getMinecraft();

    @SideOnly(Side.CLIENT)
    public void draw() {
        if (!PlayerHelper.isSurvival(mc.player))
            return;
        ScaledResolution scale = ((GuiIngameForge) mc.ingameGUI).getResolution();
        int left = scale.getScaledWidth() / 2 + 91 + offsetX;
        int top = scale.getScaledHeight() - GuiIngameForge.right_height - 10 + offsetY;
        drawPenaltyText(left, top);
    }


    @SideOnly(Side.CLIENT)
    private void drawPenaltyText(int left, int top) {
        if (this.mc.player.isEntityAlive()) {
            int y = top;
            FontRenderer fontRenderer = this.mc.fontRenderer;
            for (PenaltyHandler handler : BWRegistry.PENALTY_HANDLERS) {
                if (!handler.isDisplayed())
                    continue;
                Penalty p = handler.getPenalty(mc.player);
                if (p != null) {
                    String status = TooltipLib.getTooltip(p.getName());
                    if (status.isEmpty())
                        continue;
                    int width = fontRenderer.getStringWidth(status);
                    fontRenderer.drawStringWithShadow(status, left - width, y,
                            16777215);
                    y -= 10;
                }
            }
            GuiIngameForge.right_height += y;
        }
    }

}
