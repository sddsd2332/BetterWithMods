package betterwithmods.module.tweaks;

import betterwithmods.common.BWMRecipes;
import betterwithmods.library.recipes.RecipeMatchers;
import betterwithmods.library.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/20/17.
 */
@Mod.EventBusSubscriber
public class MossGeneration extends Feature {
    private static final HashMap<BlockStateIngredient, IBlockState> CONVERTED_BLOCKS = new HashMap<>();
    public static int RADIUS;
    public static int RATE;
    public static boolean DISABLE_VINE_RECIPES;

    public static void addBlockConversion(BlockStateIngredient ingredient, IBlockState mossyState) {
        CONVERTED_BLOCKS.put(ingredient, mossyState);
    }

    public static void mossify(World world, BlockPos pos) {
        IBlockState mossy;
        if (world.rand.nextInt(RATE) == 0 && (mossy = getMossyVariant(world, pos, world.getBlockState(pos))) != null) {
            world.setBlockState(pos, mossy);
        }
    }

    public static IBlockState getMossyVariant(World world, BlockPos pos, IBlockState state) {
        return CONVERTED_BLOCKS.keySet().stream().filter(i -> i.apply(world, pos, state)).map(CONVERTED_BLOCKS::get).findFirst().orElse(null);
    }

    public static int randomRange(int start, int end) {
        int d = end - start;
        return start + RandomUtils.nextInt(0, d);
    }

    private static Optional<BlockPos> randomPosition(World world, BlockPos start, BlockPos end) {
        if (world.isAreaLoaded(start, end)) {
            return Optional.of(new BlockPos(
                    randomRange(start.getX(), end.getX()),
                    randomRange(start.getY(), end.getY()),
                    randomRange(start.getZ(), end.getZ())
            ));
        }
        return Optional.empty();
    }

    @SubscribeEvent
    public static void generateMossNearSpawner(TickEvent.WorldTickEvent event) {
        World world = event.world;
        List<BlockPos> positions;
        if (world.isRemote || event.phase != TickEvent.Phase.END || event.side != Side.SERVER)
            return;
        if (world.rand.nextInt(RATE) != 0)
            return;
        synchronized (world.loadedTileEntityList) {
            positions = world.loadedTileEntityList
                    .stream()
                    .filter(t -> t instanceof TileEntityMobSpawner)
                    .map(TileEntity::getPos)
                    .collect(Collectors.toList());
        }
        positions.forEach(pos -> {
            BlockPos min = pos.add(-RADIUS, -RADIUS, -RADIUS), max = pos.add(RADIUS, RADIUS, RADIUS);
            randomPosition(world, min, max).ifPresent(p -> mossify(world, p));
        });
    }


    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        RADIUS = loadProperty("Moss radius from the mob spawner",5).get();
        RATE = loadProperty("Moss grow rate",100).setComment("1 out of this rate will cause a moss to try to generate").get();
        DISABLE_VINE_RECIPES = loadProperty("Disable Vine Recipes", true).setComment("Disables the mossy cobblestone and mossy brick recipes involving vines.").get();

        if (DISABLE_VINE_RECIPES) {
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:mossy_cobblestone"));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:mossy_stonebrick"));
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        addBlockConversion(new BlockStateIngredient(new ItemStack(Blocks.COBBLESTONE)), Blocks.MOSSY_COBBLESTONE.getDefaultState());
        addBlockConversion(new BlockStateIngredient(new ItemStack(Blocks.STONEBRICK)), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));
    }



    @Override
    public String getDescription() {
        return "Cobblestone or Stonebrick within the spawning radius of a Mob Spawner will randomly grow into the Mossy version.";
    }


}
