package betterwithmods.common.items.tools;

import betterwithmods.common.entity.EntityBroadheadArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/18/16
 */
public class ItemBroadheadArrow extends ItemArrow {
    @Nonnull
    @Override
    public EntityArrow createArrow(@Nonnull World worldIn, @Nonnull ItemStack stack, EntityLivingBase shooter) {
        if (!stack.isEmpty() && stack.getItem() == this)
            return new EntityBroadheadArrow(worldIn, shooter);
        return super.createArrow(worldIn, stack, shooter);
    }
}
