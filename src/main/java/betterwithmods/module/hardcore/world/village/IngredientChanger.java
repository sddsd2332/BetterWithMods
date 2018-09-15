package betterwithmods.module.hardcore.world.village;

import betterwithmods.common.registry.block.recipe.BlockIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;

public class IngredientChanger implements IChanger {
    private BlockIngredient ingredient;
    protected IBlockState state;

    public IngredientChanger(BlockIngredient ingredient, IBlockState state) {
        this.ingredient = ingredient;
        this.state = state;
    }

    @Override
    public boolean canChange(World world, BlockPos pos, IBlockState original) {
        return ingredient.apply(world, pos, original);
    }

    @Override
    public IBlockState change(World world, BlockPos pos, IBlockState original) {
        BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(world.getBiome(pos), original);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.getReplacement() != null) {
            return event.getReplacement();
        }
        return state;
    }
}
