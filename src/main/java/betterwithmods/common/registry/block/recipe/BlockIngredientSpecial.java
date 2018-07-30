package betterwithmods.common.registry.block.recipe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public class BlockIngredientSpecial extends BlockIngredient {

    private final BiPredicate<IBlockAccess, BlockPos> predicate;

    public BlockIngredientSpecial(BiPredicate<IBlockAccess, BlockPos> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean apply(World world, BlockPos pos, @Nullable IBlockState state) {
        return predicate.test(world,pos);
    }
}
