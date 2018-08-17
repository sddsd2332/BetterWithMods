package betterwithmods.module.compat.jei.ingredient;

import betterwithmods.api.recipe.output.IOutput;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.color.ColorGetter;
import mezz.jei.startup.StackHelper;
import mezz.jei.util.ErrorUtil;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class OutputHelper<V extends IOutput> implements IIngredientHelper<V> {

    private final StackHelper stackHelper;

    public OutputHelper(StackHelper stackHelper) {
        this.stackHelper = stackHelper;
    }

    @Nonnull
    @Override
    public List<V> expandSubtypes(@Nonnull List<V> contained) {
        return contained;
    }

    @Nullable
    @Override
    public V getMatch(@Nonnull Iterable<V> ingredients, @Nonnull V ingredientToMatch) {
        for (V r : ingredients) {
            if (r.equals(ingredientToMatch)) {
                return r;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nonnull V ingredient) {
        return ErrorUtil.checkNotNull(ingredient.getOutput().getDisplayName(), "itemStack.getDisplayName()");
    }

    @Nonnull
    @Override
    public String getUniqueId(@Nonnull V ingredient) {
        ErrorUtil.checkNotEmpty(ingredient.getOutput());
        return stackHelper.getUniqueIdentifierForStack(ingredient.getOutput());
    }

    @Nonnull
    @Override
    public String getWildcardId(@Nonnull V ingredient) {
        ErrorUtil.checkNotEmpty(ingredient.getOutput());
        return stackHelper.getUniqueIdentifierForStack(ingredient.getOutput(), StackHelper.UidMode.WILDCARD);
    }

    @Nonnull
    @Override
    public String getModId(@Nonnull V ingredient) {
        ErrorUtil.checkNotEmpty(ingredient.getOutput());

        Item item = ingredient.getOutput().getItem();
        ResourceLocation itemName = item.getRegistryName();
        if (itemName == null) {
            String stackInfo = getErrorInfo(ingredient);
            throw new IllegalStateException("item.getRegistryName() returned null for: " + stackInfo);
        }

        return itemName.getNamespace();
    }

    @Nonnull
    @Override
    public Iterable<Color> getColors(@Nonnull V ingredient) {
        return ColorGetter.getColors(ingredient.getOutput(), 2);
    }

    @Nonnull
    @Override
    public String getResourceId(@Nonnull V ingredient) {
        ErrorUtil.checkNotEmpty(ingredient.getOutput());

        Item item = ingredient.getOutput().getItem();
        ResourceLocation itemName = item.getRegistryName();
        if (itemName == null) {
            String stackInfo = getErrorInfo(ingredient);
            throw new IllegalStateException("item.getRegistryName() returned null for: " + stackInfo);
        }

        return itemName.getPath();

    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public V copyIngredient(@Nonnull V ingredient) {
        return (V) ingredient.copy();
    }

    @Nonnull
    @Override
    public String getErrorInfo(V ingredient) {
        return ErrorUtil.getItemStackInfo(ingredient.getOutput());
    }
}
