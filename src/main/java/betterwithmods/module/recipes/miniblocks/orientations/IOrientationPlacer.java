package betterwithmods.module.recipes.miniblocks.orientations;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IOrientationPlacer<O extends IOrientation<O>> {
    O onPlaced(EntityLivingBase placer, @Nonnull EnumFacing face, ItemStack stack, Vec3d hit);
}

