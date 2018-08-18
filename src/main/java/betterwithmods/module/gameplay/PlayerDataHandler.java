package betterwithmods.module.gameplay;

import betterwithmods.BWMod;
import betterwithmods.module.Feature;
import betterwithmods.module.hardcore.needs.HCNames;
import betterwithmods.util.CapabilityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayerDataHandler extends Feature {
    //TODO better way to handle the team.
    public static final String TEAM = "BWMTeam";
    private static final ResourceLocation PLAYER_INFO = new ResourceLocation(BWMod.MODID, "player_info");
    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(PlayerInfo.class)
    public static Capability<PlayerInfo> CAP_PLAYER_INFO = null;

    public static PlayerInfo getPlayerInfo(EntityPlayer player) {
        return CapabilityUtils.getCapability(player, CAP_PLAYER_INFO, null).orElse(null);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(PlayerDataHandler.PlayerInfo.class, new PlayerDataHandler.CapabilityPlayerInfo(), PlayerDataHandler.PlayerInfo::new);
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        PlayerInfo o = getPlayerInfo(event.getOriginal());
        PlayerInfo n = getPlayerInfo(event.getEntityPlayer());
        if (o != null && n != null) {
            n.deserializeNBT(o.serializeNBT());
        }
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer && !event.getCapabilities().containsKey(PLAYER_INFO)) {
            event.addCapability(PLAYER_INFO, new PlayerInfo());
        }
    }

    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        Scoreboard scoreboard = event.getServer().getEntityWorld().getScoreboard();
        if (scoreboard.getTeam(TEAM) == null)
            scoreboard.createTeam(TEAM);
        scoreboard.getTeam(TEAM).setNameTagVisibility(BWMod.MODULE_LOADER.isFeatureEnabled(HCNames.class) ? Team.EnumVisible.NEVER : Team.EnumVisible.ALWAYS);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    public static class CapabilityPlayerInfo implements Capability.IStorage<PlayerInfo> {
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

    //TODO make this extensible.
    public static class PlayerInfo implements ICapabilitySerializable<NBTTagCompound> {
        public boolean givenManual;
        private int ticksSinceDeath;

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
    }
}
