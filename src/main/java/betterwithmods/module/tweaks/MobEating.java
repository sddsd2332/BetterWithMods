package betterwithmods.module.tweaks;

import betterwithmods.common.entity.ai.eat.EntityAIMonsterEat;
import betterwithmods.module.Feature;
import betterwithmods.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;

public class MobEating extends Feature {
    private static final int radius = 4;

    @Override
    public String getDescription() {
        return "Mobs are hungry too, if they see some food on the ground that they like they might just eat!";
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void addEntityAI(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntitySpider) {
            if (!WorldUtils.hasAi((EntityLiving) entity, EntityAIMonsterEat.class))
                ((EntitySpider) entity).tasks.addTask(0, new EntityAIMonsterEat((EntityCreature) entity, new OreIngredient("meatChicken"), radius));
        } else if (entity instanceof EntityZombie) {
            if (!WorldUtils.hasAi((EntityLiving) entity, EntityAIMonsterEat.class))
                ((EntityZombie) entity).tasks.addTask(0, new EntityAIMonsterEat((EntityCreature) entity, new OreIngredient("listAllmeat"), radius));
        }
    }
}


