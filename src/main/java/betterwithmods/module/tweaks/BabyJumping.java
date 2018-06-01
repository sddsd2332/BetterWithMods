package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import betterwithmods.util.ReflectionLib;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BabyJumping extends Feature {

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if(entity instanceof EntityLiving) {
            if (entity.isChild())
                return;

            double motionY = ReflectionHelper.getPrivateValue(EntityLivingBase.class, entity, ReflectionLib.ENTITY_JUMP_MOTION);

            if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
                motionY += (double) ((float) (entity.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
            }
            entity.motionY -= motionY;
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Make it so babies can no longer jump. Adds some possiblities for automation";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
