package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import betterwithmods.common.tile.TileTurntable;
import betterwithmods.lib.ModLib;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerTurntable extends CraftingManagerBlock<TurntableRecipe> {

    public CraftingManagerTurntable() {
        super(new ResourceLocation(ModLib.MODID, "turntable"), TurntableRecipe.class);
    }

    public static TileTurntable findTurntable(World world, BlockPos craftingPos) {
        for (int i = 1; i <= 2; i++) {
            TileEntity tile = world.getTileEntity(craftingPos.down(i));
            if (tile instanceof TileTurntable) {
                return (TileTurntable) tile;
            }
        }
        return null;
    }

    public TurntableRecipe addDefaultRecipe(ItemStack input, ItemStack productState) {
        return addDefaultRecipe(new BlockStateIngredient(input), GlobalUtils.getStateFromStack(productState), Lists.newArrayList());
    }

    public TurntableRecipe addDefaultRecipe(ItemStack input, ItemStack productState, List<ItemStack> outputs) {
        return addDefaultRecipe(new BlockStateIngredient(input), GlobalUtils.getStateFromStack(productState), outputs);
    }

    public TurntableRecipe addDefaultRecipe(ItemStack input, IBlockState productState, List<ItemStack> outputs) {
        return addDefaultRecipe(new BlockStateIngredient(input), productState, outputs);
    }

    public TurntableRecipe addDefaultRecipe(BlockStateIngredient input, IBlockState productState, List<ItemStack> outputs) {
        return addRecipe(input, productState, outputs, 8);
    }

    public TurntableRecipe addRecipe(BlockStateIngredient input, IBlockState productState, List<ItemStack> outputs, int rotations) {
        TurntableRecipe recipe = new TurntableRecipe(input, productState, outputs, rotations);
        register(recipe);
        return recipe;
    }

    protected List<TurntableRecipe> findRecipe(IBlockState output) {
        return getValuesCollection().stream().filter(r -> r.getProductState() == output).collect(Collectors.toList());
    }

    public boolean remove(IBlockState output) {
        return getValuesCollection().removeAll(findRecipe(output));
    }

}
