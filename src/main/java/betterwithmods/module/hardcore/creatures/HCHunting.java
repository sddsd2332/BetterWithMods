package betterwithmods.module.hardcore.creatures;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.entity.ai.ShooterSpiderWeb;
import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/22/17.
 */


public class HCHunting extends Feature {

    private static final Predicate<ItemStack> isMeat = stack -> BWMOreDictionary.isOre(stack, "listAllmeat");
    public static boolean spidersShootWebs;

    public static List<Class> zombiesAttack, spiderAttack;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityCreature) {
            EntityCreature entity = (EntityCreature) evt.getEntity();
            if (entity instanceof EntityZombie) {
                for (Class clazz : zombiesAttack) {
                    ((EntityZombie) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, clazz, true));
                }
            }
            if (entity instanceof EntitySpider) {
                for (Class clazz : spiderAttack) {
                    ((EntitySpider) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, clazz, false));
                }
                if (spidersShootWebs) {
                    ((EntitySpider) entity).tasks.addTask(3, new ShooterSpiderWeb((EntitySpider) entity, 200, 15.0F));
                }
            }
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        spidersShootWebs = loadProperty("Spiders Shoot Web", true).get();


        String[] zombieStrings = loadProperty("Mobs Zombies Attack", new String[]{"net.minecraft.entity.passive.EntityCow", "net.minecraft.entity.passive.EntitySheep", "net.minecraft.entity.passive.EntityPig", "net.minecraft.entity.passive.EntityLlama"}).setComment("List of entity classes which zombies will attack").get();
        String[] spiderStrings = loadProperty("Mobs Spider Attack", new String[]{"net.minecraft.entity.passive.EntityChicken", "net.minecraft.entity.passive.EntityRabbit",}).setComment("List of entity classes which spiders will attack").get();
        zombiesAttack = Arrays.stream(zombieStrings).map(clazz -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException ignore) {
            }
            return null;
        }).collect(Collectors.toList());
        spiderAttack = Arrays.stream(spiderStrings).map(clazz -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException ignore) {
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public String getDescription() {
        return "Makes it so Mobs hunt other animals too. Zombies attack herd animals, Spiders eat Chickens, Wolves will eat anything";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
