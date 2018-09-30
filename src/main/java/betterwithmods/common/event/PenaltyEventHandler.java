package betterwithmods.common.event;

import betterwithmods.lib.ModLib;
import betterwithmods.module.internal.MiscRegistry;
import betterwithmods.module.internal.SoundRegistry;
import betterwithmods.util.player.PlayerUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class PenaltyEventHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        //This has to run on clientside and serverside
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            //Whether the player can jump.
            if (!MiscRegistry.PENALTY_HANDLERS.canJump(player)) {
                event.getEntityLiving().motionX = 0;
                event.getEntityLiving().motionY = 0;
                event.getEntityLiving().motionZ = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

        EntityPlayer player = event.player;
        //Don't run on client side
        if (player.world.isRemote)
            return;

        if (!PlayerUtils.isSurvival(player))
            return;

        //Handle whether the player can sprint
        if (!MiscRegistry.PENALTY_HANDLERS.canSprint(player)) {
            player.setSprinting(false);
        }

        //Swimming
        if (player.isInWater() && !MiscRegistry.PENALTY_HANDLERS.canSwim(player)) {
            if (!PlayerUtils.isNearBottom(player)) {
                player.motionY -= 0.04;
            }
        }
    }


    private static Object2IntMap<UUID> painTimers = new Object2IntOpenHashMap<>();

    private static boolean inPain(EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        if(uuid == null)
            return false;
        if(painTimers.getOrDefault(uuid,0) > 60) {
            painTimers.put(uuid, 0);
            return true;
        }
        painTimers.put(uuid, painTimers.getOrDefault(uuid, 0) + 1);
        return false;
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            World world = player.world;

            if (!PlayerUtils.isSurvival(player) || player.isRiding()) {
                //Remove the modifier when gamemode changes.
                PlayerUtils.removeModifier(player, SharedMonsterAttributes.MOVEMENT_SPEED, PlayerUtils.PENALTY_SPEED_UUID);
                return;
            }
            //Speed
            double speed = MiscRegistry.PENALTY_HANDLERS.getSpeedModifier(player);
            if (speed != 0) {
                PlayerUtils.changeSpeed(player, "Penalty Speed Modifier", speed, PlayerUtils.PENALTY_SPEED_UUID);
            }

            //Pain
            if (!world.isRemote && MiscRegistry.PENALTY_HANDLERS.inPain(player)) {
                if (PlayerUtils.isMoving(player) && inPain(player)) {
                    world.playSound(null, player.getPosition(), SoundRegistry.ENTITY_PLAYER_OOF, SoundCategory.BLOCKS, 0.75f, 1f);
                }
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerUtils.isSurvival(player)) {
                if (!MiscRegistry.PENALTY_HANDLERS.canAttack(player)) {
                    player.playSound(SoundRegistry.ENTITY_PLAYER_OOF, 0.75f, 1f);
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
