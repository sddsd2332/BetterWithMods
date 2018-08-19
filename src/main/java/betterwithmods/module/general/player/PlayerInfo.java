package betterwithmods.module.general.player;

import betterwithmods.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerInfo implements ICapabilitySerializable<NBTTagCompound> {

    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(PlayerInfo.class)
    public static Capability<PlayerInfo> CAP_PLAYER_INFO = null;
    public boolean givenManual;
    private int ticksSinceDeath;

    public static PlayerInfo getPlayerInfo(EntityPlayer player) {
        return CapabilityUtils.getCapability(player, CAP_PLAYER_INFO, null).orElse(null);
    }

    public int getTicksSinceDeath() {
        return ticksSinceDeath;
    }

    public void setTicksSinceDeath(int ticks) {
        this.ticksSinceDeath = ticks;
    }

    public void incrementTicksSinceDeath(int i) {
        this.ticksSinceDeath += i;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAP_PLAYER_INFO;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CAP_PLAYER_INFO)
            return CAP_PLAYER_INFO.cast(this);
        return null;

    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("givenManual", givenManual);
        tag.setInteger("ticksSinceDeath", ticksSinceDeath);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        givenManual = nbt.getBoolean("givenManual");
        ticksSinceDeath = nbt.getInteger("ticksSinceDeath");
    }


    public static class Storage implements Capability.IStorage<PlayerInfo> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<PlayerInfo> capability, PlayerInfo instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<PlayerInfo> capability, PlayerInfo instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }
}