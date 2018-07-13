package betterwithmods.event;

import betterwithmods.common.BWRegistry;
import betterwithmods.common.BWSounds;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class PenaltyEventHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            //Whether the player can jump.
            if (!BWRegistry.PENALTY_HANDLERS.canJump(player)) {
                event.getEntityLiving().motionX = 0;
                event.getEntityLiving().motionY = 0;
                event.getEntityLiving().motionZ = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!PlayerHelper.isSurvival(player))
            return;

        //Handle whether the player can sprint
        if (!BWRegistry.PENALTY_HANDLERS.canSprint(player)) {
            player.setSprinting(false);
        }

        //Swimming
        if (player.isInWater() && !BWRegistry.PENALTY_HANDLERS.canSwim(player)) {
            if (!PlayerHelper.isNearBottom(player)) {
                player.motionY -= 0.04;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!PlayerHelper.isSurvival(player)) {
                PlayerHelper.removeModifier(player, SharedMonsterAttributes.MOVEMENT_SPEED, PlayerHelper.PENALTY_SPEED_UUID);
                return;
            }

            //Speed
            double speed = BWRegistry.PENALTY_HANDLERS.getSpeedModifier(player);
            if (speed != 0) {
                PlayerHelper.changeSpeed(player, "Penalty Speed Modifier", speed, PlayerHelper.PENALTY_SPEED_UUID);
            }
            //Pain
            if (BWRegistry.PENALTY_HANDLERS.inPain(player)) {
                if (PlayerHelper.isMoving(player) && player.world.getWorldTime() % 60 == 0) {
                    player.playSound(BWSounds.OOF, 0.75f, 1f);
                }
            }

        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerHelper.isSurvival(player)) {
                if (!BWRegistry.PENALTY_HANDLERS.canAttack(player)) {
                    player.playSound(BWSounds.OOF, 0.75f, 1f);
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
