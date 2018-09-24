package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.library.modularity.impl.Feature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 6/25/17.
 */

@Mod.EventBusSubscriber
public class MysteryMeat extends Feature {
    @Override
    public String getDescription() {
        return "You don't want to know where it comes from...";
    }

    @SubscribeEvent
    public static void dropMysteryMeat(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer || event.getEntityLiving() instanceof EntityVillager || event.getEntityLiving() instanceof AbstractIllager) {
            addDrop(event, new ItemStack(BWMItems.MYSTERY_MEAT, 1 + event.getEntityLiving().world.rand.nextInt(2) + event.getLootingLevel()));
        }
    }

    public static void addDrop(LivingDropsEvent evt, ItemStack drop) {
        EntityItem item = new EntityItem(evt.getEntityLiving().getEntityWorld(), evt.getEntityLiving().posX, evt.getEntityLiving().posY, evt.getEntityLiving().posZ, drop);
        item.setDefaultPickupDelay();
        evt.getDrops().add(item);
    }

}
