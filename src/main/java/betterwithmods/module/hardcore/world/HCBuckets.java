package betterwithmods.module.hardcore.world;

import betterwithmods.BWMod;
import betterwithmods.module.ConfigHelper;
import betterwithmods.module.Feature;
import betterwithmods.module.GlobalConfig;
import betterwithmods.util.FluidUtils;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCBuckets extends Feature {
    private static List<ResourceLocation> fluidWhitelist;
    private static List<ItemStack> fluidcontainerBacklist;
    private static List<Integer> dimensionBlacklist;

    @Override
    public String getFeatureDescription() {
        return "Makes it so water buckets cannot move an entire source block, making water a more valuable resource";
    }

    @Override
    public void setupConfig() {
        dimensionBlacklist = Ints.asList(loadPropIntList("Dimension Black List", "A List of dimension ids in which water buckets will work normally. This is done in the End by default to make Enderman Farms actually reasonable to create.", new int[]{DimensionType.THE_END.getId()}));
        fluidWhitelist = ConfigHelper.loadPropRLList("Fluid Whitelist", configCategory, "List of fluids that will be handled by HCBuckets.", new String[]{
                FluidRegistry.WATER.getName()
        });
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        //TODO dispenser behavior; for water and lava bucket

        fluidcontainerBacklist = loadItemStackList("Fluid container blacklist", "Blacklist itemstacks from being effected by HCBuckets", new String[]{
                "thermalcultivation:watering_can"
        });
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }


    @SubscribeEvent
    public void onInteractFluidHandlerItem(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        IFluidHandlerItem handlerItem = FluidUtil.getFluidHandler(stack);
        if (handlerItem != null) {
            //Don't need to do buckets
            if (stack.getItem() instanceof ItemBucket)
                return;
            RayTraceResult result = new RayTraceResult(event.getHitVec(), event.getFace(), event.getPos());
            if (MinecraftForge.EVENT_BUS.post(new FillBucketEvent(event.getEntityPlayer(), stack, event.getWorld(), result)))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUseFluidContainer(FillBucketEvent event) {
        if (event.isCanceled()) return;
        if (event.getTarget() != null) {
            if (GlobalConfig.debug) {
                event.getEntityPlayer().sendMessage(new TextComponentTranslation("FillBucketEvent: %s,%s,%s,%s", event.getTarget().getBlockPos(), event.getTarget().typeOfHit, event.getEmptyBucket() != null ? event.getEmptyBucket().getDisplayName() : null, event.getFilledBucket() != null ? event.getFilledBucket().getDisplayName() : null));
                BWMod.getLog().info("FillBucketEvent: {}, {}, {}", event.getTarget(), event.getEmptyBucket(), event.getFilledBucket());
            }

            ItemStack container = event.getEmptyBucket();
            RayTraceResult raytraceresult = event.getTarget();
            BlockPos pos = raytraceresult.getBlockPos();
            World world = event.getWorld();
            EntityPlayer player = event.getEntityPlayer();
            if (!PlayerHelper.isSurvival(player))
                return;

            FluidActionResult result = FluidUtils.tryPickUpFluid(container, player, world, pos, raytraceresult.sideHit);

            if (result.isSuccess()) {
                event.setCanceled(true);
                handleFluidActionResult(result, player, container);
            } else {
                BlockPos offset = pos.offset(raytraceresult.sideHit);
                IBlockState state = world.getBlockState(offset);
                FluidStack fluidStack = FluidUtil.getFluidContained(container);
                if (fluidStack != null) {
                    event.setCanceled(true);
                    if (state.getMaterial().isReplaceable()) {
                        if (fluidStack.amount == Fluid.BUCKET_VOLUME) {
                            FluidActionResult placeResult = FluidUtils.tryPlaceFluid(player, world, offset, container, fluidStack);
                            handleFluidActionResult(placeResult, player, container);
                        }
                    }

                }
            }
        }
    }


    public void handleFluidActionResult(FluidActionResult result, EntityPlayer player, ItemStack container) {
        if (player.capabilities.isCreativeMode)
            return;
        container.shrink(1);
        ItemStack newItem = result.getResult().copy();
        if (container.isEmpty()) {
            //THIS WON'T DO. need the actual hand
            player.setHeldItem(EnumHand.MAIN_HAND, newItem);
        }
        if (!player.inventory.addItemStackToInventory(newItem))
            player.dropItem(newItem, false);
    }

    @SubscribeEvent
    public void onFluidDraining(FluidEvent.FluidDrainingEvent event) {

    }
}
