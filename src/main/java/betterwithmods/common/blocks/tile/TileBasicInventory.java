package betterwithmods.common.blocks.tile;

import betterwithmods.util.CapabilityUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 9/4/16.
 */
public abstract class TileBasicInventory extends TileBasic {

    public Predicate<EnumFacing> hasCapability = facing -> true;
    public SimpleStackHandler inventory = createItemStackHandler();

    public abstract int getInventorySize();

    public SimpleStackHandler createItemStackHandler() {
        return new SimpleStackHandler(getInventorySize(), this);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        if(capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == null || hasCapability.test(facing)))
            return true;
        return  super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (hasCapability(capability,facing) && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        return super.getCapability(capability, facing);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.merge(inventory.serializeNBT());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (inventory == null)
            inventory = createItemStackHandler();
        inventory.deserializeNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onBreak() {
        CapabilityUtils.getInventory(this, EnumFacing.UP).ifPresent(inv -> InvUtils.ejectInventoryContents(world, pos, inv));
    }

}
