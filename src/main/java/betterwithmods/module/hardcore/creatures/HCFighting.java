package betterwithmods.module.hardcore.creatures;

import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.util.PlayerUtils;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class HCFighting extends Feature {


    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent event) {
        Entity attacker = event.getAttacker();
        if (attacker instanceof EntityPlayer) {
            if (!PlayerUtils.isSurvival((EntityPlayer) attacker))
                return;
            boolean attack = false;
            for (EnumHand hand : EnumHand.values()) {
                ItemStack stack = ((EntityPlayer) attacker).getHeldItem(hand);
                Multimap<String, AttributeModifier> attributes = stack.getItem().getAttributeModifiers(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, stack);
                if (!attributes.isEmpty() && attributes.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()) != null) {
                    attack = true;
                    break;
                }
            }
            if (!attack) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Disable knockback when no weapon is used";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
