package betterwithmods.module.tweaks;

import betterwithmods.common.BWMDamageSource;
import betterwithmods.common.BWMItems;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.util.PlayerUtils;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.security.Provider;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Created by primetoxinz on 5/13/17.
 */


public class HeadDrops extends Feature {
    private static double sawHeadDropChance, battleAxeHeadDropChance;

    public static HashMap<ResourceLocation, Supplier<ItemStack>> HEADDROPS = Maps.newHashMap();

    public static void addHeadDrop(ResourceLocation location, Supplier<ItemStack> head) {
        HEADDROPS.put(location, head);
    }

    public static ItemStack getHeadDrop(ResourceLocation location) {
        if (HEADDROPS.containsKey(location))
            return HEADDROPS.get(location).get().copy();
        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        if (isChoppingBlock(event.getSource()))
            addHead(event, sawHeadDropChance);
        if (isBattleAxe(event.getSource()))
            addHead(event, battleAxeHeadDropChance);
    }

    private static boolean isChoppingBlock(DamageSource source) {
        return source.equals(BWMDamageSource.getChoppingBlockDamage());
    }

    private static boolean isBattleAxe(DamageSource source) {
        if (source != null && source.getImmediateSource() != null) {
            Entity e = source.getImmediateSource();
            if (e instanceof EntityLivingBase) {
                ItemStack held = ((EntityLivingBase) e).getHeldItemMainhand();
                return !held.isEmpty() && held.isItemEqual(new ItemStack(BWMItems.STEEL_BATTLEAXE));
            }
        }
        return false;
    }

    public static void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem item = new EntityItem(event.getEntityLiving().getEntityWorld(), event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        item.setDefaultPickupDelay();
        event.getDrops().add(item);
    }

    public static void addHead(LivingDropsEvent event, double chance) {
        if (event.getEntity().getEntityWorld().rand.nextDouble() > chance)
            return;

        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            addDrop(event, PlayerUtils.getPlayerHead((EntityPlayer) event.getEntityLiving()));
        } else {
            ResourceLocation key = EntityList.getKey(entity);
            ItemStack stack = getHeadDrop(key);
            if (!stack.isEmpty()) {
                addDrop(event, stack);
            }
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        sawHeadDropChance = loadProperty("Saw Drop Chance", 0.33).setComment("Chance for extra drops from Mobs dying on a Saw. 0 disables it entirely").get();
        battleAxeHeadDropChance = loadProperty("BattleAxe Drop Chance", 0.33).setComment("Chance for extra drops from Mobs dying from a BattleAxe. 0 disables it entirely").get();

        addHeadDrop(new ResourceLocation("skeleton"), () -> new ItemStack(Items.SKULL));
        addHeadDrop(new ResourceLocation("wither_skeleton"), () -> new ItemStack(Items.SKULL, 1, 1));
        addHeadDrop(new ResourceLocation("zombie"), () -> new ItemStack(Items.SKULL, 1, 2));
        addHeadDrop(new ResourceLocation("creeper"), () -> new ItemStack(Items.SKULL, 1, 4));

    }

    @Override
    public String getDescription() {
        return "Heads and Skulls can drop from death by Saw or BattleAxe";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

}
