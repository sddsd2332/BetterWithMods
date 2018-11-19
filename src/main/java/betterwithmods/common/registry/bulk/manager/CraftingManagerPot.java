package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.tile.IBulkTile;
import betterwithmods.api.tile.IHeated;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.tile.TileCookingPot;
import betterwithmods.lib.ModLib;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CraftingManagerPot<V extends CookingPotRecipe<V>> extends CraftingManagerBulk<V> {

    public CraftingManagerPot(String name, Class<V> type) {
        super(new ResourceLocation(ModLib.MODID, name), type);
    }

    @Override
    public boolean canCraft(CookingPotRecipe recipe, IBulkTile tile) {
        return recipe != null && recipe.canCraft((IHeated) tile, tile.getWorld(), tile.getPos());
    }

    @Override
    public boolean craftRecipe(World world, IBulkTile tile) {
        if (tile instanceof TileCookingPot) {
            TileCookingPot pot = (TileCookingPot) tile;
            V r = findRecipe(tile);

            if (canCraft(r, tile)) {
                if (pot.cookProgress >= pot.getMax()) {
                    InventoryUtils.insert(world, pot.getBlockPos().up(), tile.getInventory(), craftItem(r, world, tile), false);
                    pot.cookProgress = 0;
                    return true;
                }
                pot.cookProgress++;
            } else {
                pot.cookProgress = 0;
            }
        }
        return false;
    }

    @Override
    protected Optional<V> findRecipe(Collection<V> recipes, IBulkTile tile) {
        if (tile instanceof IHeated) {
            List<V> r1 = recipes.stream().filter(r -> r.canCraft((IHeated) tile, tile.getWorld(), tile.getPos())).collect(Collectors.toList());
            return super.findRecipe(r1, tile);
        }
        return Optional.empty();
    }


    public List<CookingPotRecipe> getRecipesForHeat(int heat) {
        return getValuesCollection().stream().filter(r -> r.getHeat() == heat).collect(Collectors.toList());
    }

    public void addStokedRecipe(Object... objects) {
    }

    public void addUnstokedRecipe(Object... objects) {
    }

    public void addHeatlessRecipe(Object... objects) {
    }


}
