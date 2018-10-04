package betterwithmods.module.tweaks;

import betterwithmods.common.BWMDamageSource;
import betterwithmods.common.BWMItems;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.util.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 5/13/17.
 */
public class HeadDrops extends Feature {
    private static int sawHeadDropChance, battleAxeHeadDropChance;

    @Override
    public void onInit(FMLInitializationEvent event) {
        sawHeadDropChance = loadProperty("Saw Drop Chance",3).setComment("Chance for extra drops from Mobs dying on a Saw. 0 disables it entirely").get();
        battleAxeHeadDropChance = loadProperty("BattleAxe Drop Chance", 3).setComment("Chance for extra drops from Mobs dying from a BattleAxe. 0 disables it entirely").get();
    }

    @Override
    public String getDescription() {
        return "Heads and Skulls can drop from death by Saw or BattleAxe";
    }

    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        if (isChoppingBlock(event.getSource()))
            addHead(event, sawHeadDropChance);
        if (isBattleAxe(event.getEntityLiving()))
            addHead(event, battleAxeHeadDropChance);
    }

    private static boolean isChoppingBlock(DamageSource source) {
        return source.equals(BWMDamageSource.getChoppingBlockDamage());
    }

    private static boolean isBattleAxe(EntityLivingBase entity) {
        DamageSource source = entity.getLastDamageSource();
        if (source != null && source.getImmediateSource() != null) {
            Entity e = source.getImmediateSource();
            if (e instanceof EntityLivingBase) {
                ItemStack held = ((EntityLivingBase) e).getHeldItemMainhand();
                return !held.isEmpty() && held.isItemEqual(new ItemStack(BWMItems.STEEL_BATTLEAXE));
            }
        }
        return false;
    }

    public static void addDrop(LivingDropsEvent evt, ItemStack drop) {
        EntityItem item = new EntityItem(evt.getEntityLiving().getEntityWorld(), evt.getEntityLiving().posX, evt.getEntityLiving().posY, evt.getEntityLiving().posZ, drop);
        item.setDefaultPickupDelay();
        evt.getDrops().add(item);
    }

    public static void addHead(LivingDropsEvent evt, int chance) {
        if (chance > 0 && evt.getEntity().getEntityWorld().rand.nextInt(chance) != 0)
            return;
        if (evt.getEntityLiving() instanceof EntitySkeleton)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 0));
        else if (evt.getEntityLiving() instanceof EntityWitherSkeleton)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 1));
        else if (evt.getEntityLiving() instanceof EntityZombie)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 2));
        else if (evt.getEntityLiving() instanceof EntityCreeper)
            addDrop(evt, new ItemStack(Items.SKULL, 1, 4));
        else if (evt.getEntityLiving() instanceof EntityPlayer) {
            addDrop(evt, PlayerUtils.getPlayerHead((EntityPlayer) evt.getEntityLiving()));
        }
    }

}
