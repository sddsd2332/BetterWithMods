package betterwithmods.module.compat.jei.category;


import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.compat.jei.JEILib;
import betterwithmods.module.compat.jei.wrapper.KilnRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;


public class KilnRecipeCategory extends BWMRecipeCategory<KilnRecipeWrapper> {
    public static final int width = 145;
    public static final int height = 80;

    private final IGuiHelper helper;
    private IDrawableAnimated flame;

    public KilnRecipeCategory(IGuiHelper helper, String name) {
        super(helper.createDrawable(JEILib.KILN_TEXTURE, 0, 0, width, height), name);
        this.helper = helper;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        flame.draw(minecraft, 67, 33);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull KilnRecipeWrapper wrapper, @Nonnull IIngredients ingredients) {
        IDrawableStatic flameDrawable = helper.createDrawable(JEILib.KILN_TEXTURE, 145, wrapper.getRecipe().getHeat() > 1 ? 14 : 0, 14, 14);
        this.flame = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);

        IGuiItemStackGroup stacks = layout.getItemStacks();
        IGuiIngredientGroup<IOutput> outputs = layout.getIngredientsGroup(IOutput.class);

        stacks.init(0, true, 20, 31);
        createSlotsHorizontal(outputs, false, 3, 1, 87, 32);
        stacks.set(ingredients);
        outputs.set(ingredients);

        int heat = wrapper.getRecipe().getHeat();
        List<ItemStack> heatSources = BWMHeatRegistry.getStacks(heat);
        if (!heatSources.isEmpty()) {
            stacks.init(5, true, 65, 52);
            stacks.set(5, heatSources);
        }
    }
}
