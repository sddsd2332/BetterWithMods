package betterwithmods.module.recipes.miniblocks.tiles;

import betterwithmods.common.tile.TileDynamic;
import betterwithmods.module.recipes.miniblocks.PropertyOrientation;
import betterwithmods.module.recipes.miniblocks.orientations.IOrientation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileOrientation<O extends IOrientation<O> & Comparable<O>> extends TileDynamic {
    private O orientation;
    @Nonnull
    private final PropertyOrientation<O> property;

    public TileOrientation(@Nonnull PropertyOrientation<O> property) {
        this.property = property;
    }

    @Override
    public void onPlacedBy(EntityLivingBase placer, @Nullable EnumFacing face, ItemStack stack, float hitX, float hitY, float hitZ) {
        super.onPlacedBy(placer, face, stack, hitX, hitY, hitZ);
        if (face != null) {
            this.orientation = property.getPlacer().onPlaced(placer, face, stack, new Vec3d(hitX, hitY, hitZ));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (orientation != null) {
            compound.setString("orientation", orientation.getName());
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        String o = value(compound, "orientation", null);
        if (o != null) {
            orientation = property.parseValue(o).orNull();
        }
        super.readFromNBT(compound);
    }

    public O getOrientation() {
        return orientation;
    }

    public void setOrientation(O orientation) {
        this.orientation = orientation;
    }
}
