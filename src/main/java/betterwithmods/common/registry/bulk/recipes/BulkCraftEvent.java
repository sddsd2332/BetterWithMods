package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.tile.IBulkTile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;

public class BulkCraftEvent extends Event {
    private final IBulkTile tile;
    private final World world;
    private final BulkRecipe recipe;
    private final NonNullList<ItemStack> outputs;


    public BulkCraftEvent(IBulkTile tile, World world, BulkRecipe recipe, NonNullList<ItemStack> outputs) {
        this.tile = tile;
        this.world = world;
        this.recipe = recipe;
        this.outputs = outputs;
    }

    public static NonNullList<ItemStack> fireOnCraft(IBulkTile tile, World world, BulkRecipe recipe, NonNullList<ItemStack> outputs) {
        BulkCraftEvent event = new BulkCraftEvent(tile, world, recipe, outputs);

        if (MinecraftForge.EVENT_BUS.post(event)) {
            return NonNullList.create();
        } else {
            return outputs;
        }
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public IBulkTile getTile() {
        return tile;
    }

    @Nullable
    public World getWorld() {
        return world;
    }

    public BulkRecipe getRecipe() {
        return recipe;
    }

    public NonNullList<ItemStack> getOutputs() {
        return outputs;
    }
}
