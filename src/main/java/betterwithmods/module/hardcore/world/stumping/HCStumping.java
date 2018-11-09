package betterwithmods.module.hardcore.world.stumping;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.event.StructureSetBlockEvent;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.variants.IBlockVariants;
import betterwithmods.library.utils.ToolUtils;
import betterwithmods.library.utils.VariantUtils;
import betterwithmods.module.hardcore.world.HCBonemeal;
import betterwithmods.network.BWMNetwork;
import betterwithmods.network.messages.MessagePlaced;
import betterwithmods.util.PlayerUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

/**
 * Created by primetoxinz on 4/20/17.
 */
@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class HCStumping extends Feature {
    public static final Set<Block> STUMP_BLACKLIST = Sets.newHashSet(BWMBlocks.BLOOD_LOG);
    private static final ResourceLocation PLACED_LOG = new ResourceLocation(ModLib.MODID, "placed_log");
    public static boolean CTM;
    public static boolean SPEED_UP_WITH_TOOLS;
    public static float STUMP_BREAK_SPEED;
    public static float ROOT_BREAK_SPEED;
    public static String[] BLACKLIST_CONFIG;

    public static boolean isStump(World world, BlockPos pos) {
        return isLog(world.getBlockState(pos)) && !isPlaced(world, pos) && isSoil(world.getBlockState(pos.down()), world, pos);
    }

    public static boolean isRoots(World world, BlockPos pos) {
        return isLog(world.getBlockState(pos.up())) && !isPlaced(world, pos.up()) && isSoil(world.getBlockState(pos), world, pos);
    }

    public static boolean isLog(IBlockState state) {
        if (!STUMP_BLACKLIST.contains(state.getBlock()) && state.getBlock() instanceof BlockLog) {
            if (state.getPropertyKeys().contains(BlockLog.LOG_AXIS)) {
                return state.getValue(BlockLog.LOG_AXIS).equals(BlockLog.EnumAxis.Y);
            }
            return true;
        }
        return VariantUtils.getVariantFromState(IBlockVariants.EnumBlock.LOG, state) != null;
    }

    public static boolean isSoil(IBlockState state, World world, BlockPos pos) {
        return state.isSideSolid(world, pos, EnumFacing.UP) && (state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS);
    }

    public static PlacedCapability getCapability(World world) {
        if (world.hasCapability(PlacedCapability.PLACED_CAPABILITY, null)) {
            return world.getCapability(PlacedCapability.PLACED_CAPABILITY, null);
        }
        return null;
    }

    public static boolean addPlacedLog(World world, BlockPos pos) {
        PlacedCapability capability = getCapability(world);
        if (capability != null) {
            if (capability.addPlaced(pos)) {
                BWMNetwork.INSTANCE.sendToAllAround(new MessagePlaced(pos), world, pos);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isPlaced(World world, BlockPos pos) {
        PlacedCapability capability = getCapability(world);
        if (capability != null) {
            return capability.isPlaced(pos);
        }
        return false;
    }


    @Override
    public String getDescription() {
        return "Makes the bottom block of trees into stumps which cannot be removed by hand, making your mark on the world more obvious";
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
        World world = event.getWorld();
        if (world.isRemote || !(event.getPlayer() instanceof EntityPlayerMP))
            return;

        if (PlayerUtils.isHolding(event.getPlayer(), HCBonemeal.FERTILIZERS))
            return;

        if (isLog(event.getState())) {
            addPlacedLog(world, event.getPos());
        }
    }

    @SubscribeEvent
    public static void getHarvest(BreakSpeed event) {
        World world = event.getEntityPlayer().getEntityWorld();
        if (isStump(world, event.getPos())) {
            float scale = SPEED_UP_WITH_TOOLS ? ToolUtils.getSpeed(event.getEntityPlayer().getHeldItemMainhand(), event.getState()) : 1;
            event.setNewSpeed(STUMP_BREAK_SPEED * scale);
        }
        if (isRoots(world, event.getPos())) {
            float scale = SPEED_UP_WITH_TOOLS ? ToolUtils.getSpeed(event.getEntityPlayer().getHeldItemMainhand(), event.getState()) : 1;
            event.setNewSpeed(ROOT_BREAK_SPEED * scale);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHarvest(BlockEvent.HarvestDropsEvent event) {
        if (isStump(event.getWorld(), event.getPos())) {
            IBlockVariants wood = VariantUtils.getVariantFromState(IBlockVariants.EnumBlock.LOG, event.getState());
            if (wood != null) {
                event.getDrops().clear();
                event.getDrops().addAll(Lists.newArrayList(wood.getVariant(IBlockVariants.EnumBlock.SAWDUST, 1), wood.getVariant(IBlockVariants.EnumBlock.BARK, 1)));
            }
        }
        if (isRoots(event.getWorld(), event.getPos())) {
            IBlockVariants wood = VariantUtils.getVariantFromState(IBlockVariants.EnumBlock.LOG, event.getWorld().getBlockState(event.getPos().up()));
            if (wood != null) {
                event.setResult(Event.Result.DENY);
                event.getDrops().clear();
                event.getDrops().addAll(Lists.newArrayList(new ItemStack(BWMItems.DIRT_PILE, 2), wood.getVariant(IBlockVariants.EnumBlock.SAWDUST, 1), wood.getVariant(IBlockVariants.EnumBlock.BARK, 1)));
            }
        }
    }

    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<World> evt) {
        evt.addCapability(PLACED_LOG, new PlacedCapability());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            PlacedCapability capability = getCapability(event.player.world);
            if (capability != null) {
                BWMNetwork.INSTANCE.sendTo(new MessagePlaced(capability.getPlaced().toArray(new BlockPos[0])), (EntityPlayerMP) event.player);
            }
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(PlacedCapability.class, new PlacedCapability.Storage(), PlacedCapability::new);
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        BLACKLIST_CONFIG = loadProperty("Stump Blacklist", new String[0]).setComment("Logs which do not createBlock stumps").get();
        for (String block : BLACKLIST_CONFIG) {
            STUMP_BLACKLIST.add(Block.REGISTRY.getObject(new ResourceLocation(block)));
        }
        SPEED_UP_WITH_TOOLS = loadProperty("Speed up with tool", true).setComment("Speed up Stump mining with tools").get();
        STUMP_BREAK_SPEED = loadProperty("Stump Break speed", 0.3f).setComment("Base break speed of stumps, scaled by tool speed option").get();
        ROOT_BREAK_SPEED = loadProperty("Root Break speed", 0.01f).setComment("Base break speed of roots, scaled by tool speed option").get();
        CTM = loadProperty("CTM Support", true).setComment("Use ConnectedTextureMod to show the stumps").get();
    }

    @SideOnly(Side.CLIENT)
    public static void syncPlaced(BlockPos[] pos) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return;
        PlacedCapability capability = HCStumping.getCapability(world);
        if (capability != null) {
            capability.addAll(pos);
        }
    }

    @SubscribeEvent
    public static void onStructureSetBlock(StructureSetBlockEvent event) {
        if (event.getWorld().isRemote)
            return;
        if (event.getState().getBlock() instanceof BlockLog) {
            addPlacedLog(event.getWorld(), event.getPos());
        }
    }
}
