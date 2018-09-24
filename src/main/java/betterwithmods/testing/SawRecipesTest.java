package betterwithmods.testing;

import betterwithmods.common.registry.block.managers.CraftingManagerSaw;
import betterwithmods.library.utils.ingredient.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.BlockIngredient;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import betterwithmods.testing.base.BaseBlockTest;
import betterwithmods.testing.base.Before;
import betterwithmods.testing.base.Test;
import betterwithmods.testing.base.world.FakeWorld;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SawRecipesTest extends BaseBlockTest<SawRecipe> {

    @Before
    @Override
    public void beforeTest() {
        world = new FakeWorld();

        TEST_MANAGER = new CraftingManagerSaw();
        world.setBlockState(origin, Blocks.PLANKS.getDefaultState());
        world.setBlockState(origin.up(), Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X));

        BlockIngredient dropIngredient = new BlockDropIngredient("logWood");

        BlockIngredient ingredient = new BlockIngredient(Blocks.PLANKS.getDefaultState());
        BlockIngredient oreIngredient = new BlockIngredient("plankWood");

        List<ItemStack> outputs = Lists.newArrayList(new ItemStack(Items.IRON_INGOT));
        recipe = new SawRecipe(ingredient, outputs);
        oreRecipe = new SawRecipe(oreIngredient, outputs);
        blockDropRecipe = new SawRecipe(dropIngredient, outputs);
    }

    @Test
    public void testLogs() {

        List<IBlockState> states = Blocks.LOG.getBlockState().getValidStates();
        for (IBlockState state : states) {
            world.setBlockState(origin.up(), state);
            testRecipe(blockDropRecipe, origin.up());
        }

    }
}
