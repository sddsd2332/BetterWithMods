package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.base.CraftingManagerBase;
import betterwithmods.common.registry.block.recipe.BlockRecipe;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/11/16
 */
public abstract class CraftingManagerBlock<V extends BlockRecipe<V>> extends CraftingManagerBase<V> {

    private final HashMap<IBlockState, V> recipeCache = Maps.newHashMap();

    public CraftingManagerBlock(ResourceLocation registryName, Class<V> type) {
        super(registryName, type);
    }

    protected List<V> findRecipe(List<ItemStack> outputs) {
        List<V> recipes = findRecipeExact(outputs);
        if (recipes.isEmpty())
            recipes = findRecipeFuzzy(outputs);
        return recipes;
    }

    protected List<V> findRecipeFuzzy(List<ItemStack> outputs) {
        return getValuesCollection().stream().filter(r -> r.getRecipeOutput().matchesFuzzy(outputs)).collect(Collectors.toList());
    }

    protected List<V> findRecipeExact(List<ItemStack> outputs) {
        return getValuesCollection().stream().filter(r -> r.getRecipeOutput().matches(outputs)).collect(Collectors.toList());
    }

    protected List<V> findRecipeByInput(ItemStack input) {
        return getValuesCollection().stream().filter(r -> r.getInput().apply(input)).collect(Collectors.toList());
    }

    public List<V> findRecipes(World world, BlockPos pos, IBlockState state) {
        return getValuesCollection().stream().filter(r -> r.matches(world, pos, state)).collect(Collectors.toList());
    }

    public Optional<V> findRecipe(World world, BlockPos pos, IBlockState state) {
        //Don't do caching for input states that have TEs, can't properly put extended states into hashmaps anyways.
        boolean hasTile = state.getBlock().hasTileEntity(state);
        if (!hasTile && recipeCache.containsKey(state)) {
            V v = recipeCache.get(state);
            if (v != null && v.matches(world, pos, state)) {
                return Optional.of(v);
            }
        }

        Optional<V> recipe = findRecipes(world, pos, state).stream().findFirst();
        if (!hasTile) {
            recipe.ifPresent(v -> recipeCache.put(state, v));
        }
        return recipe;
    }

    @Override
    public void register(V value) {
        if(value.isInvalid())
            return;
        super.register(value);
    }

    @Nonnull
    public Collection<V> getDisplayRecipes() {
        return getValuesCollection().stream().filter(r -> !r.isHidden()).collect(Collectors.toList());
    }

}
