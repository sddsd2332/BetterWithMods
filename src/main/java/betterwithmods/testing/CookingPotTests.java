package betterwithmods.testing;

import betterwithmods.api.tile.IHeated;
import betterwithmods.common.registry.bulk.manager.CraftingManagerPot;
import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.testing.base.BaseBulkTest;
import betterwithmods.testing.base.Before;
import betterwithmods.testing.base.MockInventory;
import betterwithmods.testing.base.Test;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreIngredient;
import org.fest.assertions.Assertions;

import java.util.List;


public class CookingPotTests extends BaseBulkTest<CookingPotRecipe> {
    private CookingPotRecipe stokedRecipe;
    public CookingPotTests() {
        super(new IHeated() {
            private final MockInventory inventory = MockInventory.createInventory(0, new ItemStack(Blocks.COBBLESTONE, 64));

            @Override
            public int getHeat(World world, BlockPos pos) {
                return 1;
            }

            @Override
            public World getWorld() {
                return null;
            }

            @Override
            public BlockPos getPos() {
                return null;
            }

            @Override
            public ItemStackHandler getInventory() {
                return inventory;
            }
        });
    }

    @Before
    public void beforeTest() {
        TEST_MANAGER = new CraftingManagerPot();
        List<Ingredient> inputs = Lists.newArrayList(Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)));
        List<Ingredient> inputs2 = Lists.newArrayList(new OreIngredient("cobblestone"));

        List<ItemStack> outputs = Lists.newArrayList(new ItemStack(Items.DIAMOND));
        recipe = new CookingPotRecipe(inputs, outputs, BWMHeatRegistry.UNSTOKED_HEAT);
        oreRecipe = new CookingPotRecipe(inputs2, outputs, BWMHeatRegistry.UNSTOKED_HEAT);
        stokedRecipe = new CookingPotRecipe(inputs, outputs, BWMHeatRegistry.STOKED_HEAT);
    }


    @Test
    public void testWrongHeatRecipe() {
        Assertions.assertThat(stokedRecipe.isInvalid()).isFalse();

        Assertions.assertThat(TEST_MANAGER.getRecipes()).isEmpty();
        TEST_MANAGER.addRecipe(stokedRecipe);
        Assertions.assertThat(TEST_MANAGER.getRecipes()).hasSize(1);

        Assertions.assertThat(TEST_MANAGER.canCraft(stokedRecipe, TILE, TILE.getInventory())).isFalse();
        Assertions.assertThat(TEST_MANAGER.craftItem(stokedRecipe,null, TILE, TILE.getInventory())).isEmpty();
    }
}
