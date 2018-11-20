package betterwithmods.module.compat.jei.category;

import betterwithmods.lib.ModLib;
import betterwithmods.module.compat.jei.JEILib;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public abstract class BWMRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    @Nonnull
    private final IDrawable background;

    @Nonnull
    private final String localizedName, uid;


    public BWMRecipeCategory(@Nonnull IDrawable background, @Nonnull String name) {
        this.background = background;
        this.localizedName = JEILib.name(name);
        this.uid = JEILib.uid(name);
    }

    public static void createSlotsHorizontal(IGuiIngredientGroup group, boolean input, int count, int start, int x, int y) {
        for (int i = 0; i < count; i++) {
            group.init(i + start, input, x + (i * 18), y);
        }
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nonnull
    public String getUid() {
        return this.uid;
    }

    @Nonnull
    @Override
    public String getModName() {
        return ModLib.NAME;
    }

}
