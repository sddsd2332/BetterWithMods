package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSkeletonTrap extends Feature {

    @SubscribeEvent
    public void onEntitySpawn(LivingSpawnEvent event) {
        if(event.getEntityLiving() instanceof EntitySkeletonHorse && ((EntitySkeletonHorse)event.getEntityLiving()).isTrap()) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Remove the vanilla feature of Skeleton Traps, they are quite dumb";
    }
}
