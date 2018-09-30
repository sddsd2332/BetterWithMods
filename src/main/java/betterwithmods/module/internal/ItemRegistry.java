package betterwithmods.module.internal;

import betterwithmods.common.BWMItems;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.RequiredFeature;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class ItemRegistry extends RequiredFeature {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        //TODO migrate
        BWMItems.registerItems();

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BWMItems.getItems().forEach(BWMItems::setInventoryModel);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //TODO migrate
        BWMItems.getItems().forEach(event.getRegistry()::register);
    }
}
