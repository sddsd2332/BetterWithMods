package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.registry.BrokenToolRegistry;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.common.variants.IBlockVariants;
import betterwithmods.library.utils.VariantUtils;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.util.PlayerUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static betterwithmods.library.common.variants.IBlockVariants.EnumBlock.*;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCLumber extends Feature {
    private static int plankAmount, barkAmount, sawDustAmount;

    public static int axePlankAmount = 4, axeBarkAmount = 1, axeSawDustAmount = 2;

    private static boolean hasAxe(EntityPlayer harvester, BlockPos pos, IBlockState state) {
        if (harvester == null)
            return false;
        return PlayerUtils.isCurrentToolEffectiveOnBlock(harvester, pos, state);
    }

    @Override
    public String getDescription() {
        return "Makes Punching Wood return a single plank and secondary drops instead of a log, to get a log an axe must be used.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        plankAmount = loadProperty("Plank Amount", 2).setComment("Amount of Planks dropped when Punching Wood").get();
        barkAmount = loadProperty("Bark Amount", 1).setComment("Amount of Bark dropped when Punching Wood").get();
        sawDustAmount = loadProperty("Sawdust Amount", 2).setComment("Amount of Sawdust dropped when Punching Wood").get();

        axePlankAmount = loadProperty("Axe Plank Amount", 3).setComment("Amount of Planks dropped when crafted with an axe").get();
        axeBarkAmount = loadProperty("Axe Bark Amount", 1).setComment("Amount of Bark dropped when crafted with an axe").get();
        axeSawDustAmount = loadProperty("Axe Sawdust Amount", 2).setComment("Amount of Sawdust dropped when crafted with an axe").get();


    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void harvestLog(BlockEvent.HarvestDropsEvent event) {
        if (!event.getWorld().isRemote) {
            IBlockVariants wood = VariantUtils.getVariantFromState(LOG, event.getState());
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
    public void onInit(FMLInitializationEvent event) {
        BrokenToolRegistry.init();
        for (IRecipe recipe : BWMOreDictionary.logRecipes) {
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.EXACT, recipe));
        }
    }

}
