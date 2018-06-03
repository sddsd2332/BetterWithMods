package betterwithmods.common.registry.block.recipe;

import betterwithmods.api.recipe.IRecipeOutputs;
import betterwithmods.common.BWSounds;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.StackEjector;
import betterwithmods.util.VectorBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class SawRecipe extends BlockRecipe {

    public SawRecipe(BlockIngredient input, List<ItemStack> outputs) {
        super(input, outputs);
    }

    public SawRecipe(BlockIngredient input, IRecipeOutputs recipeOutput) {
        super(input, recipeOutput);
    }

    @Override
    public boolean craftRecipe(World world, BlockPos pos, Random rand, IBlockState state) {
        List<ItemStack> output = onCraft(world, pos);
        world.setBlockToAir(pos);

        VectorBuilder builder = new VectorBuilder();
        for(ItemStack stack: output)
            new StackEjector(world, stack, builder.set(pos).rand(0.4f).offset(0.25f).build(), builder.setGaussian(0.01f, 0.01f, 0.01f).build()).ejectStack();
        world.playSound(null, pos, BWSounds.SAW_CUT, SoundCategory.BLOCKS, 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
        return true;
    }
}
