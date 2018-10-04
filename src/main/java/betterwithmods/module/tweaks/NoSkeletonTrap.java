package betterwithmods.module.tweaks;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class NoSkeletonTrap extends Feature {

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntitySkeletonHorse && ((EntitySkeletonHorse) event.getEntity()).isTrap()) {
            event.setCanceled(true);
        }
    }

    @Override
    public String getDescription() {
        return "Remove the vanilla feature of Skeleton Traps, they are quite dumb";
    }
}
