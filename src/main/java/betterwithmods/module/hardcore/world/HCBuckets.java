package betterwithmods.module.hardcore.world;

import betterwithmods.BWMod;
import betterwithmods.module.ConfigHelper;
import betterwithmods.module.Feature;
import betterwithmods.module.GlobalConfig;
import betterwithmods.util.FluidUtils;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCBuckets extends Feature {
    private static List<String> fluidWhitelist;
    private static List<ResourceLocation> fluidcontainerBacklist;
    private static List<Integer> dimensionBlacklist;

    @Override
    public String getFeatureDescription() {
        return "Makes it so water buckets cannot move an entire source block, making water a more valuable resource";
    }

    @Override
    public void setupConfig() {
        dimensionBlacklist = Ints.asList(loadPropIntList("Dimension Black List", "A List of dimension ids in which water buckets will work normally. This is done in the End by default to make Enderman Farms actually reasonable to create.", new int[]{DimensionType.THE_END.getId()}));
        fluidWhitelist = Lists.newArrayList(loadPropStringList("Fluid Whitelist", "List of fluids that will be handled by HCBuckets.", new String[]{
                FluidRegistry.WATER.getName()
        }));
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        //TODO dispenser behavior; for water and lava bucket

        fluidcontainerBacklist = ConfigHelper.loadPropRLList("Fluid container blacklist", configCategory, "Blacklist itemstacks from being effected by HCBuckets", new String[]{
                "thermalcultivation:watering_can"
        });
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }


    @SubscribeEvent
    public void onInteractFluidHandlerItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        IFluidHandlerItem handlerItem = FluidUtil.getFluidHandler(stack);
        if (handlerItem != null) {
            //Don't need to do buckets
            if (stack.getItem() instanceof ItemBucket)
                return;
            FluidStack contained = FluidUtil.getFluidContained(stack);

            RayTraceResult raytraceresult = stack.getItem().rayTrace(event.getWorld(), event.getEntityPlayer(), contained == null);

            FillBucketEvent fillBucketEvent = new FillBucketEvent(event.getEntityPlayer(), stack, event.getWorld(), raytraceresult);
            if (MinecraftForge.EVENT_BUS.post(fillBucketEvent))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUseFluidContainer(FillBucketEvent event) {
        if (event.isCanceled()) return;
        if (event.getTarget() != null) {
            ItemStack container = event.getEmptyBucket();
            RayTraceResult raytraceresult = event.getTarget();
            BlockPos pos = raytraceresult.getBlockPos();
            World world = event.getWorld();
            EntityPlayer player = event.getEntityPlayer();
            if (!PlayerHelper.isSurvival(player))
                return;


            //Skip blacklisted fluidcontainers
            if (fluidcontainerBacklist.contains(container.getItem().getRegistryName()))
                return;

            FluidStack fluidStack = FluidUtil.getFluidContained(container);
            //Ignore blacklisted dimensions
            if (dimensionBlacklist.contains(world.provider.getDimension()))
                return;

            //Only use whitelisted fluids.
            if (fluidStack != null && !fluidWhitelist.contains(fluidStack.getFluid().getName())) {
                return;
            }

            FluidActionResult result = FluidUtils.tryPickUpFluid(container, player, world, pos, raytraceresult.sideHit);

            if (result.isSuccess()) {
                if (player.getCooldownTracker().hasCooldown(container.getItem())) {
                    event.setCanceled(true);
                    return;
                }
                event.setResult(Event.Result.ALLOW);
                event.setFilledBucket(result.getResult());
                if (container.getItem() instanceof ItemBucket)
                    player.getCooldownTracker().setCooldown(container.getItem(), 20);
            } else {
                BlockPos offset = pos.offset(raytraceresult.sideHit);
                IBlockState state = world.getBlockState(offset);
                if (state.getMaterial().isReplaceable()) {
                    if (fluidStack != null) {
                        if (fluidStack.amount == Fluid.BUCKET_VOLUME) {
                            FluidActionResult placeResult = FluidUtils.tryPlaceFluid(player, world, offset, container, fluidStack);
                            if (placeResult.isSuccess()) {
                                event.setResult(Event.Result.ALLOW);
                                event.setFilledBucket(placeResult.getResult());
                            }
                        }
                    }
                } else if (!state.getMaterial().isOpaque()) {
                    event.setCanceled(true);
                }
            }

            if (GlobalConfig.debug) {
                event.getEntityPlayer().sendMessage(new TextComponentTranslation("FillBucketEvent: %s,%s,%s,%s", event.getTarget().getBlockPos(), event.getTarget().typeOfHit, event.getEmptyBucket() != null ? event.getEmptyBucket().getDisplayName() : null, event.getFilledBucket() != null ? event.getFilledBucket().getDisplayName() : null));
                BWMod.getLog().info("FillBucketEvent: {}, {}, {}", event.getTarget(), event.getEmptyBucket(), event.getFilledBucket());
            }

        }
    }


    @SubscribeEvent
    public void onFluidDraining(FluidEvent.FluidDrainingEvent event) {

    }
}
