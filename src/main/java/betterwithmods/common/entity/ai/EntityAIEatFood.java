package betterwithmods.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Optional;

/**
 * Created by primetoxinz on 4/22/17.
 */
public class EntityAIEatFood extends EntityAIBase {
    private final EntityCreature entity;
    private final Ingredient validItem;
    private EntityItem targetItem;
    private int cooldown;

    public EntityAIEatFood(EntityCreature creature, Ingredient validItem) {
        this.entity = creature;
        this.validItem = validItem;
    }

    public Optional<EntityItem> getTargetItem(List<EntityItem> items) {
        if (items.isEmpty())
            return Optional.empty();
        EntityItem target = null;
        for (EntityItem item : items) {
            if (validItem.test(item.getItem())) {
                target = item;
                break;
            }
        }
        return Optional.ofNullable(target);
    }

    @Override
    public void resetTask() {
        targetItem = null;
    }

    @Override
    public boolean shouldExecute() {
        if(cooldown > 0)
            return false;
        BlockPos entityPos = entity.getPosition();
        if (targetItem == null) {
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(2, 2,2));
            targetItem = getTargetItem(entityItems).orElse(null);
        }

        if (targetItem != null) {
            BlockPos targetPos = targetItem.getPosition();
            if (entityPos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= 2D) {
                processItemEating();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
       return shouldExecute();
    }

    private void processItemEating() {
        if(targetItem != null) {
            ItemStack foodStack = targetItem.getItem().splitStack(1);
            entity.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, (entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.2F + 1.0F);
            foodStack.shrink(1);
            cooldown = 500;
        }
    }
}
