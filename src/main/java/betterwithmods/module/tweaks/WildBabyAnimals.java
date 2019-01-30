package betterwithmods.module.tweaks;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class WildBabyAnimals extends Feature {

    private static Random random = new Random();
    private static int chance;

    @SubscribeEvent
    public void onMobSpawned(EntityJoinWorldEvent event) {
       if(event.getEntity() instanceof EntityAgeable && !(event.getEntity() instanceof EntityMob)) {
           EntityAgeable entity = (EntityAgeable) event.getEntity();

           if(entity.ticksExisted <= 1 && random.nextInt(chance) == 1) {
               entity.setGrowingAge(-24000);
           }

       }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        chance = loadProperty("Chance of an animal spawning as a baby", 15).setComment("1/x chance of mob spawning as a baby.").get();
    }

    @Override
    public String getDescription() {
        return "Mobs will sometimes spawn as babies, if applicable";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
