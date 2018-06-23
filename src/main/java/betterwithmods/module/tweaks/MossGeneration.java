package betterwithmods.module.tweaks;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
public class MossGeneration extends Feature {
    public static int RADIUS;
    public static int RATE;
    public static boolean DISABLE_VINE_RECIPES;
    private static final HashMap<BlockIngredient, IBlockState> CONVERTED_BLOCKS = new HashMap<>();

    public static void addBlockConversion(BlockIngredient ingredient, IBlockState mossyState) { //TODO: Could be the new block meta ingredient possibly
        CONVERTED_BLOCKS.put(ingredient, mossyState);
    }

    public static void mossify(World world, BlockPos pos) {
        IBlockState mossy;
        if (world.rand.nextInt(RATE) == 0 && (mossy = getMossyVariant(world, pos, world.getBlockState(pos))) != null) {
            world.setBlockState(pos, mossy);
        }
    }

    public static IBlockState getMossyVariant(World world, BlockPos pos, IBlockState state) {
        return CONVERTED_BLOCKS.keySet().stream().filter(i -> i.apply(world, pos, state)).map(k -> CONVERTED_BLOCKS.get(k)).findFirst().orElse(null);
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
    public void generateMossNearSpawner(TickEvent.WorldTickEvent event) {
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
    public void setupConfig() {
        RADIUS = loadPropInt("Moss radius from the mob spawner", "", 5);
        RATE = loadPropInt("Moss grow rate", "1 out of this rate will cause a moss to try to generate", 100);
        DISABLE_VINE_RECIPES = loadPropBool("Disable Vine Recipes", "Disables the mossy cobblestone and mossy brick recipes involving vines.", true);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (DISABLE_VINE_RECIPES) {
            BWMRecipes.removeRecipe("minecraft:mossy_cobblestone");
            BWMRecipes.removeRecipe("minecraft:mossy_stonebrick");
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addBlockConversion(new BlockIngredient(new ItemStack(Blocks.COBBLESTONE)), Blocks.MOSSY_COBBLESTONE.getDefaultState());
        addBlockConversion(new BlockIngredient(new ItemStack(Blocks.STONEBRICK)), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Cobblestone or Stonebrick within the spawning radius of a Mob Spawner will randomly grow into the Mossy version.";
    }


}
