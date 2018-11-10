package betterwithmods.manual.custom;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockChime;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.collections.BlockStateIngredientSet;
import betterwithmods.manual.api.manual.PathProvider;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class StatePathProvider implements PathProvider {

    private final Set<PathOverride> PATH_OVERRIDES = Sets.newHashSet();

    {
        addBlock(new BlockStateIngredientSet(BlockChime.BLOCKS), "wind_chime");
        addBlock(new BlockIngredient(BWMBlocks.WOODEN_BROKEN_GEARBOX), "wooden_gearbox");

        addBlock(new BlockStateIngredient(BlockAesthetic.getStack(BlockAesthetic.Type.CHOPBLOCK), BlockAesthetic.getStack(BlockAesthetic.Type.CHOPBLOCKBLOOD)), "chopping_block");

        addBlock(new BlockStateIngredient(BlockAesthetic.getStack(BlockAesthetic.Type.WHITESTONE), BlockAesthetic.getStack(BlockAesthetic.Type.WHITECOBBLE)), "white_stone");
        addItem(new BlockStateIngredient(BlockAesthetic.getStack(BlockAesthetic.Type.NETHERCOAL)), "nether_coal");

        addBlock(new BlockIngredient(BWMBlocks.TURNTABLE), "turntable");
        addBlock(new BlockIngredient(BWMBlocks.MILLSTONE), "millstone");
        addBlock(new BlockIngredient(BWMBlocks.FILTERED_HOPPER), "hopper");
        addBlock(new BlockIngredient(BWMBlocks.PULLEY), "pulley");
        addItem(new BlockIngredient(BWMBlocks.ROPE), "rope");

        addBlock(new BlockStateIngredientSet(
//                        new BlockIngredient(BWMBlocks.GRATE),
                        new BlockIngredient(BWMBlocks.WICKER)
//                        new BlockIngredient(BWMBlocks.SLATS)
                ),
                "decoration");
    }

    private void addBlock(BlockStateIngredient ingredient, String name) {
        PATH_OVERRIDES.add(new PathOverride(ingredient, name));
    }

    private void addItem(BlockStateIngredient ingredient, String name) {
        PATH_OVERRIDES.add(new PathOverride(ingredient, "items", name));
    }

    @Nullable
    @Override
    public String pathFor(@Nonnull ItemStack stack) {
        return null;
    }

    @Nullable
    @Override
    public String pathFor(@Nonnull World world, @Nonnull BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        for (PathOverride override : PATH_OVERRIDES)
            if (override.apply(world, pos, state))
                return String.format("%%LANGUAGE%%/%s/%s.md", override.path, override.name);
        return defaultPathFor(state);
    }

    private String defaultPathFor(IBlockState state) {
        Block block = state.getBlock();
        ResourceLocation loc = block.getRegistryName();
        if (loc != null) {
            String name = loc.getPath();
            return String.format("%%LANGUAGE%%/blocks/%s.md", name);
        }
        return null;
    }

    private class PathOverride {
        private BlockStateIngredient ingredient;

        private String name, path;

        public PathOverride(BlockStateIngredient ingredient, String name) {
            this(ingredient, "blocks", name);
        }

        public PathOverride(BlockStateIngredient ingredient, String path, String name) {
            this.ingredient = ingredient;
            this.name = name;
            this.path = path;
        }

        public boolean apply(World world, BlockPos pos, IBlockState state) {
            return ingredient.test(world, pos, state);
        }
    }
}
