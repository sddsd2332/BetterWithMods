package betterwithmods.module.general;

import betterwithmods.common.BWMItems;
import betterwithmods.lib.ModLib;
import betterwithmods.lib.TooltipLib;
import betterwithmods.manual.api.ManualAPI;
import betterwithmods.manual.api.prefab.manual.TextureTabIconRenderer;
import betterwithmods.manual.common.DirectoryDefaultProvider;
import betterwithmods.manual.common.api.ManualDefinitionImpl;
import betterwithmods.manual.custom.StatePathProvider;
import betterwithmods.module.Feature;
import betterwithmods.module.general.player.PlayerInfo;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class Documentation extends Feature {

    private static boolean giveAfterCraft;

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        giveAfterCraft = loadProperty("Give Manual", true).setComment("Give the player a manual after they craft a BWM item").get();
    }

    @Override
    public void onInitClient(FMLInitializationEvent event) {
        ManualAPI.addProvider(new DirectoryDefaultProvider(new ResourceLocation(ModLib.MODID, "documentation/docs/")));
        ManualDefinitionImpl.INSTANCE.addDefaultProviders();
        ManualAPI.addProvider(new StatePathProvider());
        ManualAPI.addTab(new TextureTabIconRenderer(new ResourceLocation(ModLib.MODID, "textures/gui/manual_home.png")), "bwm.manual.home", "%LANGUAGE%/index.md");
    }

    @SubscribeEvent
    public static void onCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        if (!giveAfterCraft)
            return;
        if (!event.player.world.isRemote && !event.crafting.isEmpty() && event.crafting.getItem() != BWMItems.MANUAL) {
            ResourceLocation name = event.crafting.getItem().getRegistryName();
            if (name != null && name.toString().toLowerCase(Locale.ROOT).contains(ModLib.MODID)) {
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
