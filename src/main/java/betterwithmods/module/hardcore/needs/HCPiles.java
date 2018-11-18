package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.registry.advanceddispenser.BehaviorSilkTouch;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.module.internal.AdvancedDispenserRegistry;
import betterwithmods.util.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/20/17.
 */


public class HCPiles extends Feature {
    public static final Map<IBlockState, ItemStack> blockStateToPile = new HashMap<>();
    public static boolean keepSoilDrops;

    public static void removePile(ItemStack input) {
        blockStateToPile.remove(GlobalUtils.getStateFromStack(input));
    }

    public static void registerPile(Block block, int meta, ItemStack stack) {
        registerPile(block.getStateFromMeta(meta), stack);
    }

    public static void registerPile(Block block, ItemStack stack) {
        AdvancedDispenserRegistry.BLOCK_COLLECT_REGISTRY.put(block, new BehaviorSilkTouch());
        for (IBlockState state : block.getBlockState().getValidStates())
            registerPile(state, stack);
    }

    public static void registerPile(IBlockState block, ItemStack stack) {
        blockStateToPile.put(block, stack);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        IBlockState state = event.getState();
        if (event.isSilkTouching() || event.getResult().equals(Event.Result.DENY) || !blockStateToPile.containsKey(state))
            return;

        if (PlayerUtils.isCurrentToolEffectiveOnBlock(event.getHarvester(), event.getPos(), event.getState()))
            return;

        if (blockStateToPile.containsKey(state)) {
            ItemStack pile = blockStateToPile.get(state).copy();
            ArrayList<ItemStack> extraDrops = null;
            if (keepSoilDrops) //Save a bit of time if it's disabled.
                extraDrops = event.getDrops().stream().filter(drop -> !drop.isItemEqual(pile) && !isBlockDrop(drop)).collect(Collectors.toCollection(ArrayList::new));
            event.getDrops().clear();
            if (event.getWorld().rand.nextFloat() <= event.getDropChance()) {
                event.getDrops().add(pile);
            }
            if (extraDrops != null)
                event.getDrops().addAll(extraDrops);
        }
    }

    private static boolean isBlockDrop(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemBlock;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        keepSoilDrops = loadProperty("Soil Blocks Keep Their Drops", true).setComment("Blocks affected by HC Piles that drop things other than themselves will keep those drops.").get();

        registerPile(Blocks.DIRT, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.DIRT, 1, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.DIRT, 2, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.FARMLAND, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(BWMBlocks.FERTILE_FARMLAND, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.GRASS, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.MYCELIUM, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.GRASS_PATH, new ItemStack(BWMItems.DIRT_PILE, 3));
        registerPile(Blocks.GRAVEL, new ItemStack(BWMItems.GRAVEL_PILE, 3));
        registerPile(Blocks.SAND, new ItemStack(BWMItems.SAND_PILE, 3));
        registerPile(Blocks.SAND, 1, new ItemStack(BWMItems.RED_SAND_PILE, 3));
        registerPile(BWMBlocks.DIRT_SLAB, new ItemStack(BWMItems.DIRT_PILE, 1));
        registerPile(Blocks.CLAY, new ItemStack(Items.CLAY_BALL, 3));
    }

    @Override
    public String getDescription() {
        return "Makes soils drop 75% of their content if not broken with a shovel to incentivize the use of shovels";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
