package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.InventoryUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 5/21/17.
 */

@Mod.EventBusSubscriber
public class HCSeeds extends Feature {
    private static final Random RANDOM = new Random();
    public static Set<ItemStack> SEED_BLACKLIST;
    public static Set<IBlockState> BLOCKS_TO_STOP = Sets.newHashSet();
    private static boolean stopZombieCropLoot;

    private static Predicate<IBlockState> STOP_SEEDS = state -> {
        Block block = state.getBlock();
        return BLOCKS_TO_STOP.contains(state) || block instanceof BlockTallGrass || (block instanceof BlockDoublePlant && (state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS || state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.FERN));
    };

    @Override
    public String getDescription() {
        return "Requires Tilling the ground with a hoe to get seeds.";
    }


    @SubscribeEvent
    public static void onHarvest(BlockEvent.HarvestDropsEvent event) {
        if (STOP_SEEDS.test(event.getState()))
            event.getDrops().clear();
    }

    public static NonNullList<ItemStack> getDrops(boolean isGrass, int fortune) {
        if (RANDOM.nextInt(8) != 0) return NonNullList.create();
        ItemStack seed = net.minecraftforge.common.ForgeHooks.getGrassSeed(RANDOM, 0);
        if (SEED_BLACKLIST.stream().anyMatch(s -> InventoryUtils.matches(s, seed)) || seed.isEmpty() || (!isGrass && seed.getItem().equals(Item.getItemFromBlock(BWMBlocks.HEMP))))
            return NonNullList.create();
        else
            return NonNullList.withSize(1, seed);
    }

    @SubscribeEvent
    public static void onHoe(UseHoeEvent e) {
        if (e.getResult() == Event.Result.DENY)
            return;
        World world = e.getWorld();
        if (!world.isRemote) {
            BlockPos pos = e.getPos();
            if (world.isAirBlock(pos.up())) {
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof BlockGrass || state.getBlock() instanceof BlockDirt) {
                    InventoryUtils.ejectStackWithOffset(world, pos.up(), getDrops(state.getBlock() instanceof BlockGrass, 0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void mobDrop(LivingDropsEvent e) {
        if (!stopZombieCropLoot || !(e.getEntityLiving() instanceof EntityZombie))
            return;
        Iterator<EntityItem> iter = e.getDrops().iterator();
        EntityItem item;
        while (iter.hasNext()) {
            item = iter.next();
            ItemStack stack = item.getItem();
            if (BWMOreDictionary.hasPrefix(stack, "crop"))
                iter.remove();
        }

    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        stopZombieCropLoot = loadProperty("Stop Zombie Crop Loot", true).setComment("Stops Zombies from dropping potatoes or carrots").get();
        SEED_BLACKLIST = Sets.newHashSet(config().loadItemStackList("Seed Blacklist", getCategory(), "Blacklist seeds from being dropped when tilling grass. Defaulted to Wheat seeds for HCVillages.", new ItemStack[]{new ItemStack(Items.WHEAT_SEEDS)}));
    }

}
