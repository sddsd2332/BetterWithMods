package betterwithmods.common.registry.anvil;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class AnvilRecipe implements IForgeRegistryEntry<AnvilRecipe> {

    private IRecipe recipe;

    public AnvilRecipe(IRecipe recipe) {
        this.recipe = recipe;
    }

    public boolean matches(InventoryCrafting inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return recipe.getCraftingResult(inv);
    }

    public boolean canFit(int width, int height) {
        return recipe.canFit(width, height);
    }

    public ItemStack getRecipeOutput() {
        return recipe.getRecipeOutput();
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return recipe.getRemainingItems(inv);
    }

    public NonNullList<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }

    public boolean isDynamic() {
        return recipe.isDynamic();
    }


    public String getGroup() {
        return recipe.getGroup();
    }

    @Override
    public AnvilRecipe setRegistryName(ResourceLocation name) {
        recipe.setRegistryName(name);
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return recipe.getRegistryName();
    }

    @Override
    public Class<AnvilRecipe> getRegistryType() {
        return AnvilRecipe.class;
    }

    public IRecipe getRecipe() {
        return recipe;
    }
}
