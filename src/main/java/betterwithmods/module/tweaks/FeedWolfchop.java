package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

import static betterwithmods.util.WorldUtils.addDrop;


public class FeedWolfchop extends Feature {
    @SubscribeEvent
    public void feedDog(PlayerInteractEvent.EntityInteractSpecific event) {
        ItemStack stack = event.getItemStack();
        if (event.getTarget() instanceof EntityWolf && stack.getItem() == BWMItems.WOLF_CHOP) {
            Random rand = event.getWorld().rand;
            EntityWolf wolf = (EntityWolf) event.getTarget();
            if (!wolf.isAngry() && stack.getCount() > 0) {
                if (wolf.isTamed())
                    wolf.setTamed(false);
                stack.shrink(1);
                wolf.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                wolf.playSound(SoundEvents.ENTITY_WOLF_GROWL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                wolf.setAttackTarget(event.getEntityPlayer());
            }

        }
    }

    @SubscribeEvent
    public void dropItem(LivingDropsEvent event) {
        World world = event.getEntityLiving().getEntityWorld();
        if (event.getEntityLiving() instanceof EntityWolf) {
            addDrop(event, new ItemStack(BWMItems.WOLF_CHOP, world.rand.nextInt(2)));
        }
    }

    @Override
    public String getDescription() {
        return "Feeding a Wolf a Wolf chop? Might not be the best idea";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
