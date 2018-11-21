package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.tile.IBulkTile;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.common.tile.TileMill;
import betterwithmods.lib.ModLib;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.internal.SoundRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class CraftingManagerMill extends CraftingManagerBulk<MillRecipe> {

    public CraftingManagerMill() {
        super(new ResourceLocation(ModLib.MODID, "millstone"), MillRecipe.class);
    }

    @Override
    public boolean canCraft(MillRecipe recipe, IBulkTile tile) {
        if (recipe != null && tile instanceof TileMill) {
            TileMill mill = (TileMill) tile;
            return mill.grindCounter >= recipe.getTicks();
        }
        return false;
    }

    @Override
    public boolean craftRecipe(World world, IBulkTile tile) {
        if (tile instanceof TileMill) {
            TileMill mill = (TileMill) tile;
            MillRecipe recipe = findRecipe(getValuesCollection(), tile).orElse(null);

            if (mill.getBlockWorld().rand.nextInt(20) == 0)
                mill.getBlockWorld().playSound(null, mill.getBlockPos(), SoundRegistry.BLOCK_GRIND_NORMAL, SoundCategory.BLOCKS, 0.5F + mill.getBlockWorld().rand.nextFloat() * 0.1F, 0.5F + mill.getBlockWorld().rand.nextFloat() * 0.1F);

            if (recipe != null) {
                if (mill.grindMax != recipe.getTicks())
                    mill.grindMax = recipe.getTicks();
                //Play sounds
                if (mill.getBlockWorld().rand.nextInt(40) < 2)
                    mill.getBlockWorld().playSound(null, mill.getBlockPos(), recipe.getSound(), SoundCategory.BLOCKS, 0.75F, mill.getWorld().rand.nextFloat() * 0.4F + 0.8F);
                if (canCraft(recipe, tile)) {
                    mill.ejectRecipe(RecipeRegistry.MILLSTONE.craftItem(recipe, world, tile));
                    mill.grindCounter = 0;
                    return true;
                } else {
                    mill.grindCounter = Math.min(mill.grindMax, mill.grindCounter + mill.getIncrement());
                }
                mill.markDirty();
            } else {
                mill.grindCounter = 0;
                mill.grindMax = -1;
            }
        }

        return false;
    }
}
