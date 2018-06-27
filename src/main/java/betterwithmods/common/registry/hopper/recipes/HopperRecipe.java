package betterwithmods.common.registry.hopper.recipes;

import betterwithmods.api.recipe.IRecipeOutputs;
import betterwithmods.api.recipe.impl.ListOutputs;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.mechanical.tile.TileFilteredHopper;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.util.InvUtils;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class HopperRecipe {
    protected final ResourceLocation filterName;
    protected final Ingredient input;

    protected final IRecipeOutputs recipeOutputWorld;
    protected final IRecipeOutputs recipeOutputInsert;

    public HopperRecipe(String filterName, Ingredient input, ItemStack output, ItemStack... secondaryOutput) {
        this(filterName, input, Lists.newArrayList(output), Lists.newArrayList(secondaryOutput));
    }

    public HopperRecipe(String filterName, Ingredient input, IRecipeOutputs recipeOutputInsert, IRecipeOutputs recipeOutputWorld) {
        this.filterName = new ResourceLocation(filterName);
        this.input = input;
        this.recipeOutputWorld = recipeOutputWorld;
        this.recipeOutputInsert = recipeOutputInsert;
    }

    public HopperRecipe(String filterName, Ingredient input, List<ItemStack> output, List<ItemStack> secondaryOutput) {
        this(filterName, input, new ListOutputs(output), new ListOutputs(secondaryOutput));
    }

    public boolean matches(ResourceLocation filterName, ItemStack stack) {
        return filterName.equals(this.filterName) && input.apply(stack);
    }

    public boolean craftRecipe(EntityItem inputStack, World world, BlockPos pos, TileFilteredHopper tile) {
        if(!canCraft(world,pos, tile))
            return false;

        SimpleStackHandler inventory = tile.inventory;
        for (ItemStack output : getOutputs()) {
            ItemStack remainder = InvUtils.insert(inventory, output, false);

            if (!remainder.isEmpty())
                InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), remainder);
        }
        InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), getSecondaryOutputs());
        onCraft(world, pos, inputStack, tile);
        return true;
    }

    public void onCraft(World world, BlockPos pos, EntityItem item, TileFilteredHopper tile) {
        int count = input instanceof StackIngredient ? ((StackIngredient) input).getCount(item.getItem()) : 1;
        item.getItem().shrink(count);
        if (item.getItem().getCount() <= 0)
            item.setDead();
    }

    public ResourceLocation getFilterType() {
        return filterName;
    }

    public List<ItemStack> getFilters() {
        return Lists.newArrayList(BWRegistry.HOPPER_FILTERS.getFilter(getFilterType()).getFilter().getMatchingStacks());
    }

    public List<ItemStack> getInputContainer() {
        return Lists.newArrayList();
    } //For showing that it needs urns

    public List<ItemStack> getOutputContainer() {
        return Lists.newArrayList();
    } //For showing that it needs urns

    public Ingredient getInputs() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return recipeOutputInsert.getOutputs();
    }

    public List<ItemStack> getSecondaryOutputs() {
        return recipeOutputWorld.getOutputs();
    }

    public IRecipeOutputs getRecipeOutputWorld() {
        return recipeOutputWorld;
    }

    public IRecipeOutputs getRecipeOutputInsert() {
        return recipeOutputInsert;
    }

    protected boolean canCraft(World world, BlockPos pos, TileFilteredHopper tile) {
        ItemStackHandler inventory = tile.inventory;
        List<ItemStack> outputs = getOutputs();
        if (outputs.isEmpty())
            return true;
        return outputs.stream().allMatch(stack -> InvUtils.insert(inventory, stack, true).isEmpty());
    }
}