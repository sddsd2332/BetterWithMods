package betterwithmods.module.tweaks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class EquipmentDrop extends Feature {

    @SubscribeEvent
    public static void setDropChance(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) e.getEntity();
            if (entity instanceof EntityZombie) {
                for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                    entity.setDropChance(slot, 1);
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Zombies have a 100% chance to drop any equipment";
    }
}
