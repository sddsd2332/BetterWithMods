package betterwithmods.module.compat.jei.ingredient;

import betterwithmods.api.recipe.output.IOutput;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.plugins.vanilla.ingredients.ItemStackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class OutputRenderer<V extends IOutput> implements IIngredientRenderer<V> {

    private final ItemStackRenderer itemStackRenderer;

    public OutputRenderer() {
        itemStackRenderer = new ItemStackRenderer();
    }

    @Override
    public void render(@Nonnull Minecraft minecraft, int xPosition, int yPosition, @Nullable V ingredient) {
        if (ingredient != null) {
            itemStackRenderer.render(minecraft, xPosition, yPosition, ingredient.getOutput());
        }
    }

    @Nonnull
    @Override
    public List<String> getTooltip(@Nonnull Minecraft minecraft, @Nonnull V ingredient, ITooltipFlag tooltipFlag) {
        List<String> tooltip = itemStackRenderer.getTooltip(minecraft, ingredient.getOutput(), tooltipFlag);
        tooltip.add(ingredient.getTooltip());
        return tooltip;
    }

    @Nonnull
    @Override
    public FontRenderer getFontRenderer(Minecraft minecraft, V ingredient) {
        return itemStackRenderer.getFontRenderer(minecraft, ingredient.getOutput());
    }
}
