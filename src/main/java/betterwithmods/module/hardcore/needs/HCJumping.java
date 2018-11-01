package betterwithmods.module.hardcore.needs;

import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.util.PlayerUtils;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 5/3/17.
 */
@Mod.EventBusSubscriber
public class HCJumping extends Feature {
    public HCJumping() {
        this.enabled = false;
    }

    @SubscribeEvent
    public static void onBlockPlace(PlayerInteractEvent.RightClickBlock e) {
        if (!PlayerUtils.isSurvival(e.getEntityPlayer()) || e.getEntityPlayer().isInWater() || e.getEntityPlayer().isOnLadder())
            return;
        if (e.getItemStack().getItem() instanceof ItemBlock && !e.getEntityPlayer().onGround) {
            e.setResult(Event.Result.DENY);
            e.setCanceled(true);
        }
    }

    @Override
    public String getDescription() {
        return "Stops the ability to place blocks while in the air. This stops the use of 'Derp Pillars' to escape attacks";
    }


}
