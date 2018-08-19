package betterwithmods.module.general;

import betterwithmods.BWMod;
import betterwithmods.common.BWMItems;
import betterwithmods.module.Feature;
import betterwithmods.module.general.player.PlayerInfo;
import betterwithmods.util.InvUtils;
import betterwithmods.util.TooltipLib;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Locale;

@Mod.EventBusSubscriber
public class Documentation extends Feature {

    @SubscribeEvent
    public static void onCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        if (!event.player.world.isRemote && !event.crafting.isEmpty() && event.crafting.getItem() != BWMItems.MANUAL) {
            ResourceLocation name = event.crafting.getItem().getRegistryName();
            if (name != null && name.toString().toLowerCase(Locale.ROOT).contains(BWMod.MODID)) {
                PlayerInfo save = PlayerInfo.getPlayerInfo(event.player);
                if (save != null && !save.givenManual) {
                    save.givenManual = true;
                    InvUtils.spawnStack(event.player.world, event.player.getPosition(), new ItemStack(BWMItems.MANUAL), 10);
                    event.player.sendMessage(TooltipLib.getMessageComponent("give_manual"));
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Gives the Player a BWM Manual the first time they craft an item from BWM";
    }


}
