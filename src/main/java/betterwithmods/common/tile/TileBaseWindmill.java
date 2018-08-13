package betterwithmods.common.tile;

import betterwithmods.api.IColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.DimensionType;

import javax.annotation.Nonnull;

public abstract class TileBaseWindmill extends TileAxleGenerator implements IColor {
    protected int[] colors;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < colors.length; i++) {
            if (tag.hasKey("Color_" + i))
                colors[i] = tag.getInteger("Color_" + i);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        for (int i = 0; i < colors.length; i++) {
            t.setInteger("Color_" + i, colors[i]);
        }
        t.setByte("DyeIndex", this.dyeIndex);
        return t;
    }

    @Override
    public boolean dye(EnumDyeColor color) {
        boolean dyed = false;
        if (colors[dyeIndex] != color.getMetadata()) {
            colors[dyeIndex] = color.getMetadata();
            dyed = true;
            IBlockState state = getBlockWorld().getBlockState(this.pos);
            this.getBlockWorld().notifyBlockUpdate(this.pos, state, state, 2);
            this.markDirty();
        }
        dyeIndex++;
        if (dyeIndex > (colors.length - 1))
            dyeIndex = 0;
        return dyed;
    }

    public int[] getColors() {
        return colors;
    }

    public int getBladeColor(int blade) {
        return colors[blade];
    }

    @Override
    public int getColor(int index) {
        return colors[index];
    }

    @Override
    public void calculatePower() {
        byte power = 0;
        if (isValid()) {
            if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
                if (world.isThundering())
                    power = 3;
                else if (world.isRaining())
                    power = 2;
                else
                    power = 1;
            } else {
                power = 1;
            }
        }

        if (power != this.power) {
            setPower(power);
        }
    }


    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

}
