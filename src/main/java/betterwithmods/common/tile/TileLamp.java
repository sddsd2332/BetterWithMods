package betterwithmods.common.tile;

import betterwithmods.common.blocks.BlockLamp;
import betterwithmods.library.common.tile.TileBasic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileLamp extends TileBasic {

    private int color;
    private boolean inverted = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        compound.setInteger("color", color);
        compound.setBoolean("inverted", inverted);

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        color = value(compound, "color", 0);
        inverted = value(compound, "inverted", false);
        super.readFromNBT(compound);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public void fromItemStack(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            color = value(tag, "color", 0);
            inverted = value(tag, "inverted", false);
        }
    }

    public ItemStack getItemStack() {
        return BlockLamp.createLamp(getColor(), isInverted());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
