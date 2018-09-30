package betterwithmods.module.internal;

import betterwithmods.client.baking.BarkModel;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMItems;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.RequiredFeature;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
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


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //TODO migrate
        BWMItems.getItems().forEach(event.getRegistry()::register);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BWMItems.getItems().forEach(BWMItems::setInventoryModel);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onPostBake(ModelBakeEvent event) {
        BarkModel.BARK = new BarkModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "item/bark")));
        event.getModelRegistry().putObject(BarkModel.LOCATION, BarkModel.BARK);
    }

    @Override
    public int priority() {
        return 99;
    }
}
