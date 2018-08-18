package betterwithmods.module.hardcore.crafting;

import betterwithmods.api.util.IBlockVariants;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.BrokenToolRegistry;
import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static betterwithmods.api.util.IBlockVariants.EnumBlock.*;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCLumber extends Feature {
    public static int plankAmount, barkAmount, sawDustAmount;

    public static int axePlankAmount, axeBarkAmount, axeSawDustAmount;

    public static boolean hasAxe(EntityPlayer harvester, BlockPos pos, IBlockState state) {
        if (harvester == null)
            return false;
        return PlayerHelper.isCurrentToolEffectiveOnBlock(harvester, pos, state);
    }

    @Override
    public void setupConfig() {
        plankAmount = loadPropInt("Plank Amount", "Amount of Planks dropped when Punching Wood", 2);
        barkAmount = loadPropInt("Bark Amount", "Amount of Bark dropped when Punching Wood", 1);
        sawDustAmount = loadPropInt("Sawdust Amount", "Amount of Sawdust dropped when Punching Wood", 2);

        axePlankAmount = loadPropInt("Axe Plank Amount", "Amount of Planks dropped when crafted with an axe", 3);
        axeBarkAmount = loadPropInt("Axe Bark Amount", "Amount of Bark dropped when crafted with an axe", 1);
        axeSawDustAmount = loadPropInt("Axe Sawdust Amount", "Amount of Sawdust dropped when crafted with an axe", 2);
    }

    @Override
    public String getDescription() {
        return "Makes Punching Wood return a single plank and secondary drops instead of a log, to get a log an axe must be used.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        BrokenToolRegistry.init();
        BWOreDictionary.logRecipes.forEach(r -> BWMRecipes.removeRecipe(r.getRegistryName()));
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void harvestLog(BlockEvent.HarvestDropsEvent event) {
        if (!event.getWorld().isRemote) {
            IBlockVariants wood = BWOreDictionary.getVariantFromState(LOG, event.getState());
            if (wood != null) {
                if (event.isSilkTouching() || hasAxe(event.getHarvester(), event.getPos(), event.getState()))
                    return;
                event.setDropChance(1);
                event.getDrops().clear();
                event.getDrops().addAll(Lists.newArrayList(wood.getVariant(BLOCK, plankAmount), wood.getVariant(SAWDUST, sawDustAmount), wood.getVariant(BARK, barkAmount)));
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
