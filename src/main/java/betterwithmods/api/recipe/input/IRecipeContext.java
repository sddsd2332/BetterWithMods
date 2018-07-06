package betterwithmods.api.recipe.input;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.items.IItemHandler;

public interface IRecipeContext {

    IItemHandler getInventory();

    IBlockAccess getWorld();

    BlockPos getPos();
}
