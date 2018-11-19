package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.tile.IBulkTile;
import betterwithmods.common.registry.base.CraftingManagerBase;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CraftingManagerBulk<V extends BulkRecipe<V>> extends CraftingManagerBase<V> {

    public CraftingManagerBulk(ResourceLocation registryName, Class<V> type) {
        super(registryName, type);
    }

    public abstract boolean craftRecipe(World world, IBulkTile tile);

    @Nonnull
    public NonNullList<ItemStack> craftItem(V recipe, World world, IBulkTile tile) {
        return canCraft(recipe, tile) ? recipe.onCraft(world, tile) : NonNullList.create();
    }

    protected Optional<V> findRecipe(Collection<V> recipes, IBulkTile tile) {
        return recipes.stream().map(r -> {
            int i = r.matches(tile);
            return Pair.of(r, i);
        }).filter(p -> p.getValue() > -1).sorted(Comparator.comparingInt(Pair::getValue)).map(Pair::getKey).sorted().findFirst();
    }

    public V findRecipe(IBulkTile tile) {
        return findRecipe(getValuesCollection(), tile).orElse(null);
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

    public boolean canCraft(V recipe, IBulkTile tile) {
        return recipe != null;
    }

    public V getRecipe(IBulkTile tile) {
        return findRecipe(getValuesCollection(), tile).orElse(null);
    }
}
