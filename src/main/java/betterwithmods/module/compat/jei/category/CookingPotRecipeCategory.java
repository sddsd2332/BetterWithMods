package betterwithmods.module.compat.jei.category;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.compat.jei.JEILib;
import betterwithmods.module.compat.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class CookingPotRecipeCategory extends BWMRecipeCategory<BulkRecipeWrapper<CookingPotRecipe>> {

    private static final int width = 165;
    private static final int height = 57;
    private static final int inputSlots = 9;
    private static final int outputSlot = 0;


    @Nonnull
    private final ICraftingGridHelper craftingGrid;
    private final IGuiHelper helper;
    private IDrawableAnimated flame;

    public CookingPotRecipeCategory(IGuiHelper helper, String name) {
        super(helper.createDrawable(JEILib.COOKINGPOT_TEXTURE, 0, 0, width, height), name);
        this.helper = helper;
        this.craftingGrid = helper.createCraftingGridHelper(inputSlots, outputSlot);
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        flame.draw(minecraft, 77, 22);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull BulkRecipeWrapper<CookingPotRecipe> wrapper, @Nonnull IIngredients ingredients) {

        IDrawableStatic flameDrawable = helper.createDrawable(JEILib.COOKINGPOT_TEXTURE, 166, wrapper.getRecipe().getHeat() > 1 ? 14 : 0, 14, 14);
        this.flame = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);

        IGuiItemStackGroup stacks = layout.getItemStacks();
        IGuiIngredientGroup<IOutput> outputs = layout.getIngredientsGroup(IOutput.class);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = i + (j * 3);
                stacks.init(inputSlots + index, true, 7 + i * 18, 2 + j * 18);
                outputs.init(outputSlot + index, false, 106 + i * 18, 3 + j * 18);
            }
        }
        outputs.set(ingredients);

        int heat = wrapper.getRecipe().getHeat();
        List<ItemStack> heatSources = BWMHeatRegistry.getStacks(heat);
        if (!heatSources.isEmpty()) {
            stacks.init(19, true, 75, 38);
            stacks.set(19, heatSources);
        }

        List<List<ItemStack>> inputList = ingredients.getInputs(ItemStack.class);
        craftingGrid.setInputs(stacks, inputList);
    }
}
