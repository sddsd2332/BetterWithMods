package betterwithmods.module.general.moreheads.common;

import betterwithmods.library.common.tile.TileBasic;
import betterwithmods.library.utils.DirUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileHead extends TileBasic {

    private HeadType type;
    private int rotation;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (type != null) {
            compound.setString("type", type.getName());
        }
        compound.setInteger("rotation", rotation);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        type = HeadType.getValue(value(compound, "type", ""));
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

    public float getRotation() {
        IBlockState state = world.getBlockState(getPos());
        EnumFacing facing = state.getValue(DirUtils.FACING);
        switch (facing) {
            case UP:
                return (int) ((float) (rotation * 360) / 16.0F);
            case NORTH:
                return 0;
            case SOUTH:
                return 180;
            case WEST:
                return 270;
            case EAST:
                return 90;
        }
        return 0;
    }
}

