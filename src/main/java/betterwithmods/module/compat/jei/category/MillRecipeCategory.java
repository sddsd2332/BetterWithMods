package betterwithmods.module.compat.jei.category;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.module.compat.jei.JEILib;
import betterwithmods.module.compat.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class MillRecipeCategory extends BWMRecipeCategory<BulkRecipeWrapper> {
    private static final int width = 149;
    private static final int height = 32;
    private static final int inputSlots = 0;
    private static final int outputSlot = 3;


    @Nonnull
    private final IDrawableAnimated gear;

    public MillRecipeCategory(IGuiHelper helper) {
        super(helper.createDrawable(JEILib.MILLSTONE_TEXTURE, 0, 0, width, height), JEILib.MILLSTONE_BASE);
        IDrawableStatic flameDrawable = helper.createDrawable(JEILib.MILLSTONE_TEXTURE, 150, 0, 14, 14);
        this.gear = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        gear.draw(minecraft, 68, 8);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull BulkRecipeWrapper wrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStackGroup = layout.getItemStacks();
        IGuiIngredientGroup<IOutput> guiOutputs = layout.getIngredientsGroup(IOutput.class);

        createSlotsHorizontal(guiItemStackGroup, true, 3, inputSlots, 7, 7);
        createSlotsHorizontal(guiOutputs, false, 3, outputSlot, 90, 8);

        guiItemStackGroup.set(ingredients);
        guiOutputs.set(ingredients);

        guiOutputs.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> tooltip.add(wrapper.getRecipe().getRegistryName().toString()));

    }
}
