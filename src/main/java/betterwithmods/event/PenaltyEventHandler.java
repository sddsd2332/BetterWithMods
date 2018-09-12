package betterwithmods.event;

import betterwithmods.BWMod;
import betterwithmods.common.BWMRegistry;
import betterwithmods.common.BWMSounds;
import betterwithmods.util.player.PlayerHelper;
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

@Mod.EventBusSubscriber(modid = BWMod.MODID)
public class PenaltyEventHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        //This has to run on clientside and serverside
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            //Whether the player can jump.
            if (!BWMRegistry.PENALTY_HANDLERS.canJump(player)) {
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

        if (!PlayerHelper.isSurvival(player))
            return;

        //Handle whether the player can sprint
        if (!BWMRegistry.PENALTY_HANDLERS.canSprint(player)) {
            player.setSprinting(false);
        }

        //Swimming
        if (player.isInWater() && !BWMRegistry.PENALTY_HANDLERS.canSwim(player)) {
            if (!PlayerHelper.isNearBottom(player)) {
                player.motionY -= 0.04;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            World world = player.world;

            if (!PlayerHelper.isSurvival(player) || player.isRiding()) {
                //Remove the modifier when gamemode changes.
                PlayerHelper.removeModifier(player, SharedMonsterAttributes.MOVEMENT_SPEED, PlayerHelper.PENALTY_SPEED_UUID);
                return;
            }
            //Speed
            double speed = BWMRegistry.PENALTY_HANDLERS.getSpeedModifier(player);
            if (speed != 0) {
                PlayerHelper.changeSpeed(player, "Penalty Speed Modifier", speed, PlayerHelper.PENALTY_SPEED_UUID);
            }

            //Pain

            if (!world.isRemote && BWMRegistry.PENALTY_HANDLERS.inPain(player)) {
                long time = world.getWorldTime();
                if (PlayerHelper.isMoving(player) && time % (5*20) == 0) {
                    world.playSound(null, player.getPosition(), BWMSounds.OOF, SoundCategory.BLOCKS, 0.75f, 1f);
                }
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerHelper.isSurvival(player)) {
                if (!BWMRegistry.PENALTY_HANDLERS.canAttack(player)) {
                    player.playSound(BWMSounds.OOF, 0.75f, 1f);
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
