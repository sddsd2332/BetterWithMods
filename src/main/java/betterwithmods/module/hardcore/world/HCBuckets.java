package betterwithmods.module.hardcore.world;

import betterwithmods.BWMod;
import betterwithmods.module.ConfigHelper;
import betterwithmods.module.Feature;
import betterwithmods.module.GlobalConfig;
import betterwithmods.util.FluidUtils;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.primitives.Ints;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidRegistry;
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
    public void onUseFluidContainer(FillBucketEvent event) {
        if (event.isCanceled()) return;
        if (event.getTarget() != null) {

            if (GlobalConfig.debug) {
                event.getEntityPlayer().sendMessage(new TextComponentTranslation("FillBucketEvent: %s,%s,%s,%s", event.getTarget().getBlockPos(), event.getTarget().typeOfHit, event.getEmptyBucket() != null ? event.getEmptyBucket().getDisplayName() : null, event.getFilledBucket() != null ? event.getFilledBucket().getDisplayName() : null));
                BWMod.getLog().info("FillBucketEvent: {}, {}, {}", event.getTarget(), event.getEmptyBucket(), event.getFilledBucket());
            }

            event.setFilledBucket(ItemStack.EMPTY);

            ItemStack container = event.getEmptyBucket();
            RayTraceResult raytraceresult = event.getTarget();
            BlockPos pos = raytraceresult.getBlockPos();
            World world = event.getWorld();
            EntityPlayer player = event.getEntityPlayer();
            if (!PlayerHelper.isSurvival(player))
                return;

            event.setCanceled(true);

            FluidActionResult result = FluidUtils.tryPickUpFluid(container, player, world, pos, raytraceresult.sideHit);


            if (result.isSuccess()) {
                if (player.capabilities.isCreativeMode)
                    return;

                container.shrink(1);
                if (container.isEmpty()) {
                    //THIS WON'T DO. need the actual hand
                    player.setHeldItem(EnumHand.MAIN_HAND, result.getResult());
                }
                if (!player.inventory.addItemStackToInventory(result.getResult()))
                    player.dropItem(result.getResult(), false);
            }
        }
    }


    @SubscribeEvent
    public void onFluidDraining(FluidEvent.FluidDrainingEvent event) {

    }
}
