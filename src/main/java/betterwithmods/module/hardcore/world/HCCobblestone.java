package betterwithmods.module.hardcore.world;

import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HCCobblestone extends Feature {
    @SubscribeEvent
    public static void dropCobble(BlockEvent.HarvestDropsEvent evt) {
        if (!evt.isSilkTouching() && !evt.getWorld().isRemote) {
            if (evt.getState().getBlock() == Blocks.STONE) {
                int meta = evt.getState().getBlock().getMetaFromState(evt.getState());
                if (meta == 1 || meta == 3 || meta == 5) {
                    evt.getDrops().clear();
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Makes stone variants drop into cobblestone.";
    }
}
