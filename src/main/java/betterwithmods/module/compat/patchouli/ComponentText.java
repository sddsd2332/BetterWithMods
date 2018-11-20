package betterwithmods.module.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.VariableHolder;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;

public class ComponentText implements ICustomComponent {

    protected int x, y;

    @VariableHolder
    @SerializedName("text")
    public String text;

    @VariableHolder
    @SerializedName("color")
    protected String colorStr;

    @SerializedName("max_width")
    protected int maxWidth = 116;
    @SerializedName("line_height")
    protected int lineHeight = 9;

    private transient BookTextRenderer textRenderer;
    private transient int color;

    @Override
    public void onDisplayed(IComponentRenderContext context) {
        this.textRenderer = new BookTextRenderer((GuiBook) context.getGui(), this.text, this.x, this.y, this.maxWidth, this.lineHeight, this.color);
    }

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;

        try {
            this.color = Integer.parseInt(this.colorStr, 16);
        } catch (NumberFormatException var5) {
            this.color = EnumDyeColor.BLACK.getColorValue();
        }
    }

    @Override
    public void render(IComponentRenderContext iComponentRenderContext, float pticks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        textRenderer.render(mouseX, mouseY);
        renderAdditional(pticks, mouseX, mouseY);
        GlStateManager.popMatrix();
    }

    public void renderAdditional(float pticks, int mouseX, int mouseY) {

    }
}
