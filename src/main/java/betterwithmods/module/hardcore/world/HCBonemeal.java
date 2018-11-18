package betterwithmods.module.hardcore.world;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockPlanter;
import betterwithmods.library.common.event.block.EntityCollidedEvent;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.library.utils.ingredient.collections.IngredientSet;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 5/14/17.
 */


public class HCBonemeal extends Feature {
    private static boolean removeBonemealRecipe;
    public static IngredientSet FERTILIZERS = new IngredientSet();

    public static void registerFertilizer(ItemStack stack) {
        FERTILIZERS.add(StackIngredient.fromStacks(stack));
    }

    @SubscribeEvent
    public void onBonemeal(BonemealEvent e) {
        if (!PlayerUtils.isSurvival(e.getEntityPlayer()))
            return;
        if (!(e.getBlock().getBlock() instanceof BlockGrass) && !(e.getBlock().getBlock() instanceof BlockPlanter) && e.getBlock().getBlock() instanceof IGrowable) {
            IBlockState below = e.getWorld().getBlockState(e.getPos().down());
            below.getBlock().onBlockClicked(e.getWorld(), e.getPos().down(), e.getEntityPlayer());
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityCollideWith(EntityCollidedEvent event) {
        if (event.getWorld().isRemote)
            return;
        if (event.getState().getBlock() instanceof BlockFarmland || event.getState().getBlock() == BlockPlanter.getBlock(BlockPlanter.Type.FARMLAND)) {
            if (event.getEntity() instanceof EntityItem) {
                ItemStack stack = ((EntityItem) event.getEntity()).getItem();
                if (FERTILIZERS.test(stack)) {
                    fertilizerBlock(event.getWorld(), event.getPos(), stack, null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent.RightClickBlock e) {
        ItemStack stack = e.getItemStack();
        if (!FERTILIZERS.test(stack))
            return;
        World world = e.getWorld();
        BlockPos pos = e.getPos();
        fertilizerBlock(world, pos, stack, e.getEntityPlayer());
    }

    private static void fertilizerBlock(World world, BlockPos pos, ItemStack stack, @Nullable EntityPlayer player) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IPlantable) {
            IBlockState below = world.getBlockState(pos.down());
            if (processFertilization(below, world, pos.down())) {
                if (player == null || !player.capabilities.isCreativeMode)
                    stack.shrink(1);
            }
        } else if (processFertilization(state, world, pos)) {
            if (player == null || !player.capabilities.isCreativeMode)
                stack.shrink(1);
        }
    }

    private static boolean processFertilization(IBlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        if (block == Blocks.FARMLAND) {
            world.setBlockState(pos, BWMBlocks.FERTILE_FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, world.getBlockState(pos).getValue(BlockFarmland.MOISTURE)));
            world.playEvent(2005, pos.up(), 0);
            return true;
        } else if (block == BlockPlanter.getBlock(BlockPlanter.Type.FARMLAND)) {
            world.setBlockState(pos, BlockPlanter.getBlock(BlockPlanter.Type.FERTILE).getDefaultState());
            world.playEvent(2005, pos.up(), 0);
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Removes the ability to instant-grow crops and trees with bonemeal";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        removeBonemealRecipe = loadProperty("Remove Bonemeal Crafting Recipe", true).get();
    }

    @Override
    public void registerRecipes() {
        if (removeBonemealRecipe) {
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT_SIZE, new ItemStack(Items.DYE, 3, EnumDyeColor.WHITE.getDyeDamage())));
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        registerFertilizer(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
        registerFertilizer(new ItemStack(BWMItems.FERTILIZER));
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
