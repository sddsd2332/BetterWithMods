package betterwithmods.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.Optional;

public class EntityUtils {

    public static void removeAI(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        entity.tasks.taskEntries.removeIf(entityAITaskEntry -> clazz.isAssignableFrom(entityAITaskEntry.action.getClass()));
    }

    public static boolean hasAI(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        return entity.tasks.taskEntries.stream().anyMatch(entityAITaskEntry -> clazz.isAssignableFrom(entityAITaskEntry.action.getClass()));
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityAIBase> Optional<T> findFirst(EntityLiving entity, Class<T> clazz) {
        return entity.tasks.taskEntries.stream().filter( t -> clazz.isAssignableFrom(t.getClass())).map( t -> (T)t.action).findFirst();
    }

    public static void copyEntityInfo(EntityLivingBase copyFrom, EntityLivingBase copyTo) {
        copyTo.setHealth(copyFrom.getHealth());
        copyTo.setPositionAndRotation(copyFrom.posX, copyFrom.posY, copyFrom.posZ, copyFrom.rotationYaw, copyFrom.rotationPitch);
        copyTo.setRotationYawHead(copyFrom.getRotationYawHead());
    }

}
