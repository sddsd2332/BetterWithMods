package betterwithmods.module.hardcore.world.stumping;

import betterwithmods.api.util.IBlockVariants;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.world.strata.HCStrata;
import betterwithmods.util.item.ToolsManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCStumping extends Feature {
    public static boolean ENABLED;
    public static boolean SPEED_UP_WITH_TOOLS;
    public static float STUMP_BREAK_SPEED;
    public static float ROOT_BREAK_SPEED;

    public static Set<Block> STUMP_BLACKLIST = Sets.newHashSet(BWMBlocks.BLOOD_LOG);

    public HCStumping() {
        ENABLED = true;
    }

    @Override
    public String getFeatureDescription() {
        return "Makes the bottom block of trees into stumps which cannot be removed by hand, making your mark on the world more obvious";
    }

    @Nullable
    public static BlockPlanks.EnumType getWoodType(IBlockState state) {
        if (state.getProperties().containsKey(BlockPlanks.VARIANT)) {
            return state.getValue(BlockPlanks.VARIANT);
        } else if (state.getProperties().containsKey(BlockOldLog.VARIANT)) {
            return state.getValue(BlockOldLog.VARIANT);
        } else if (state.getProperties().containsKey(BlockNewLog.VARIANT)) {
            return state.getValue(BlockNewLog.VARIANT);
        } else
            return null;
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    public static String[] BLACKLIST_CONFIG;

    public static boolean isActualStump(World world, BlockPos pos) {
        return isStump(world.getBlockState(pos)) && isRoots(world.getBlockState(pos.down()));
    }

    public static boolean isStump(IBlockState state) {
        if (!STUMP_BLACKLIST.contains(state.getBlock()) && state.getBlock() instanceof BlockLog) {
            if (state.getPropertyKeys().contains(BlockLog.LOG_AXIS)) {
                return state.getValue(BlockLog.LOG_AXIS).equals(BlockLog.EnumAxis.Y);
            }
            return true;
        }
        return BWOreDictionary.getVariantFromState(IBlockVariants.EnumBlock.LOG, state) != null;
    }

    public static boolean isRoots(IBlockState state) {
        return state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS;
    }

    @Override
    public void setupConfig() {
        BLACKLIST_CONFIG = loadPropStringList("Stump Blacklist", "Logs which do not create stumps", new String[0]);
        for (String block : BLACKLIST_CONFIG) {
            STUMP_BLACKLIST.add(Block.REGISTRY.getObject(new ResourceLocation(block)));
        }
        SPEED_UP_WITH_TOOLS = loadPropBool("Speed up with tool", "Speed up Stump mining with tools", true);
        STUMP_BREAK_SPEED = (float) loadPropDouble("Stump Break speed", "Base break speed of stumps, scaled by tool speed option", 0.03f);
        ROOT_BREAK_SPEED = (float) loadPropDouble("Root Break speed", "Base break speed of roots, scaled by tool speed option", 0.01f);
    }

    @SubscribeEvent
    public void getHarvest(PlayerEvent.BreakSpeed event) {
        World world = event.getEntityPlayer().getEntityWorld();
        if (isStump(world.getBlockState(event.getPos())) && isRoots(world.getBlockState(event.getPos().down()))) {
            float scale = SPEED_UP_WITH_TOOLS ? ToolsManager.getSpeed(event.getEntityPlayer().getHeldItemMainhand(), event.getState()) : 1;
            event.setNewSpeed(STUMP_BREAK_SPEED * scale);
        }
        if (isRoots(world.getBlockState(event.getPos())) && isStump(world.getBlockState(event.getPos().up()))) {
            float scale = SPEED_UP_WITH_TOOLS ? ToolsManager.getSpeed(event.getEntityPlayer().getHeldItemMainhand(), event.getState()) : 1;
            event.setNewSpeed(ROOT_BREAK_SPEED * scale);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {

        if (isStump(event.getState()) && isRoots(event.getWorld().getBlockState(event.getPos().down()))) {
            IBlockVariants wood = BWOreDictionary.getVariantFromState(IBlockVariants.EnumBlock.LOG,event.getState());
            if (wood != null) {
                event.getDrops().clear();
                event.getDrops().addAll(Lists.newArrayList(wood.getVariant(IBlockVariants.EnumBlock.SAWDUST,1), wood.getVariant(IBlockVariants.EnumBlock.BARK,1)));
            }
        }
        if (isRoots(event.getState()) && isStump(event.getWorld().getBlockState(event.getPos().up()))) {
            IBlockVariants wood = BWOreDictionary.getVariantFromState(IBlockVariants.EnumBlock.LOG,event.getWorld().getBlockState(event.getPos().up()));
            if (wood != null) {
                event.setResult(Event.Result.DENY);
                event.getDrops().clear();
                event.getDrops().addAll(Lists.newArrayList(new ItemStack(BWMItems.DIRT_PILE, 2), wood.getVariant(IBlockVariants.EnumBlock.SAWDUST,1), wood.getVariant(IBlockVariants.EnumBlock.BARK,1)));
            }
        }
    }

}
