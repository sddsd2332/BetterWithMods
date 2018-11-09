package betterwithmods.client.gui;

import betterwithmods.common.penalties.Penalty;
import betterwithmods.common.penalties.PenaltyHandler;
import betterwithmods.lib.ModLib;
import betterwithmods.library.utils.TooltipUtils;
import betterwithmods.module.internal.MiscRegistry;
import betterwithmods.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by primetoxinz on 5/13/17.
 */
@Mod.EventBusSubscriber(modid = ModLib.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class GuiStatus {

    @SideOnly(Side.CLIENT)
    public static final GuiStatus INSTANCE = new GuiStatus();
    public static boolean isGloomLoaded, isHungerLoaded, isInjuryLoaded;
    public static int offsetX, offsetY;
    private final Minecraft mc = Minecraft.getMinecraft();

    @SideOnly(Side.CLIENT)
    public void draw() {
        if (!PlayerUtils.isSurvival(mc.player))
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
            for (PenaltyHandler handler : MiscRegistry.PENALTY_HANDLERS) {
                if (!handler.isDisplayed())
                    continue;
                Penalty p = handler.getPenalty(mc.player);
                if (p != null) {
                    String status = TooltipUtils.getTooltip(ModLib.MODID, p.getName());
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderStatus(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            GuiStatus.INSTANCE.draw();
        }
    }

}
