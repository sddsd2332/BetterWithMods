package betterwithmods.module.hardcore.world;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.TooltipUtils;
import betterwithmods.util.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static betterwithmods.lib.TooltipLib.BED_TOO_RESTLESS;

/**
 * Created by primetoxinz on 4/20/17.
 */


public class HCBeds extends Feature {
    public static final EntityPlayer.SleepResult TOO_RESTLESS = EnumHelper.addEnum(EntityPlayer.SleepResult.class, "TOO_RESTLESS", new Class[0]);

    public static boolean stillSetSpawn;

    /**
     * Disable Beds
     */
    @SubscribeEvent
    public void onSleepInBed(PlayerSleepInBedEvent event) {
        if (PlayerUtils.isSurvival(event.getEntityPlayer())) {
            event.getEntityPlayer().sendStatusMessage(TooltipUtils.getMessageComponent(ModLib.MODID, BED_TOO_RESTLESS), true);
            event.setResult(TOO_RESTLESS);
            if (stillSetSpawn) {
                event.getEntityPlayer().setSpawnPoint(event.getPos(), true);
            }
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        stillSetSpawn = loadProperty("Still Set Spawn", false).get();
    }

    @Override
    public String getDescription() {
        return "Disables the ability to sleep in a bed and can no longer set spawn";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

}
