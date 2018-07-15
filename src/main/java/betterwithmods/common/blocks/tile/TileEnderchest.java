package betterwithmods.common.blocks.tile;

import betterwithmods.module.hardcore.beacons.EnderchestCap;
import betterwithmods.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

import static betterwithmods.module.hardcore.beacons.EnderchestCap.ENDERCHEST_CAPABILITY;

public class TileEnderchest extends TileEntityEnderChest {

    private Type type = Type.NONE;

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("type", type.ordinal());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("type"))
            this.type = Type.VALUES[compound.getInteger("type")];
        super.readFromNBT(compound);
    }

    @Nonnull
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        NONE((tile, player) -> CapabilityUtils.getCapability(tile.getWorld(), ENDERCHEST_CAPABILITY, EnumFacing.UP).map(EnderchestCap::getInventory).orElse(null)),
        DIMENSION1((tile, player) -> {
            World world = tile.getWorld();
            return CapabilityUtils.getCapability(world, ENDERCHEST_CAPABILITY, EnumFacing.SOUTH).map(EnderchestCap::getInventory).orElse(null);
        }),
        DIMENSION2((tile, player) -> CapabilityUtils.getCapability(tile.getWorld(), ENDERCHEST_CAPABILITY, EnumFacing.NORTH).map(EnderchestCap::getInventory).orElse(null)),
        GLOBAL((tile, player) -> CapabilityUtils.getCapability(DimensionManager.getWorld(0), ENDERCHEST_CAPABILITY, EnumFacing.DOWN).map(EnderchestCap::getInventory).orElse(null)),
        PRIVATE((tile, player) -> player.getInventoryEnderChest());

        public static final Type[] VALUES = values();

        private final BiFunction<TileEntity, EntityPlayer, InventoryEnderChest> function;

        Type(BiFunction<TileEntity, EntityPlayer, InventoryEnderChest> function) {
            this.function = function;
        }

        public BiFunction<TileEntity, EntityPlayer, InventoryEnderChest> getFunction() {
            return function;
        }
    }


}

