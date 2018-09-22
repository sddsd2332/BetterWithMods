package betterwithmods.module.internal.player;

import betterwithmods.BWMod;
import betterwithmods.lib.ModLib;
import betterwithmods.module.RequiredFeature;
import betterwithmods.module.hardcore.needs.HCNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class PlayerDataHandler extends RequiredFeature {

    //TODO better way to handle the team.
    public static final String TEAM = "BWMTeam";

    private static final ResourceLocation PLAYER_INFO = new ResourceLocation(ModLib.MODID, "player_info");

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        PlayerInfo o = PlayerInfo.getPlayerInfo(event.getOriginal());
        PlayerInfo n = PlayerInfo.getPlayerInfo(event.getEntityPlayer());
        if (o != null && n != null) {
            n.deserializeNBT(o.serializeNBT());
        }
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer && !event.getCapabilities().containsKey(PLAYER_INFO)) {
            event.addCapability(PLAYER_INFO, new PlayerInfo());
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(PlayerInfo.class, new PlayerInfo.Storage(), PlayerInfo::new);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        Scoreboard scoreboard = event.getServer().getEntityWorld().getScoreboard();
        if (scoreboard.getTeam(TEAM) == null)
            scoreboard.createTeam(TEAM);
        scoreboard.getTeam(TEAM).setNameTagVisibility(BWMod.MODULE_LOADER.isFeatureEnabled(HCNames.class) ? Team.EnumVisible.NEVER : Team.EnumVisible.ALWAYS);
    }

    @Override
    public String getDescription() {
        return "Internal handler for PlayerInfo capability";
    }

}
