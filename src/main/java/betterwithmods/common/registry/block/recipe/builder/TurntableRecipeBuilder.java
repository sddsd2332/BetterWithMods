package betterwithmods.common.registry.block.recipe.builder;

import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import betterwithmods.library.utils.GlobalUtils;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class TurntableRecipeBuilder extends BlockRecipeBuilder<TurntableRecipe> {

    private int rotations = 8;
    private IBlockState productState;
    private ItemStack representativeStack = ItemStack.EMPTY;

    public TurntableRecipeBuilder rotations(int rotations) {
        this.rotations = rotations;
        return this;
    }

    public TurntableRecipeBuilder displayStack(ItemStack stack) {
        this.representativeStack = stack;
        return this;
    }

    public TurntableRecipeBuilder productState(Block block) {
        return productState(block.getDefaultState());
    }

    public TurntableRecipeBuilder productState(IBlockState state) {
        this.productState = state;
        return this;
    }

    @Override
    public void reset() {
        super.reset();
        this.rotations = 8;
    }

    @Override
    protected TurntableRecipe create() {
        if (representativeStack.isEmpty()) {
            representativeStack = GlobalUtils.getStackFromState(productState);
        }
        Preconditions.checkState(rotations > 0);
        Preconditions.checkNotNull(productState);
        Preconditions.checkState(!representativeStack.isEmpty());

        return new TurntableRecipe(input, outputs, rotations, productState, representativeStack);
    }
}
