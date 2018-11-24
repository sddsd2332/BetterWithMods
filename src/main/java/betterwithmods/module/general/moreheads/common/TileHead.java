package betterwithmods.module.general.moreheads.common;

import betterwithmods.library.common.tile.TileBasic;
import net.minecraft.nbt.NBTTagCompound;

public class TileHead extends TileBasic {

    private HeadType type;
    private int rotation;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("type", type.ordinal());
        compound.setInteger("rotation", rotation);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        type = HeadType.VALUES[value(compound, "type", 0)];
        rotation = value(compound, "rotation", 0);
        super.readFromNBT(compound);
    }

    public void setType(HeadType type) {
        this.type = type;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public HeadType getType() {
        return type;
    }

    public int getRotation() {
        return rotation;
    }
}

