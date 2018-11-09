//package betterwithmods.PROXY;
//
//import betterwithmods.BetterWithMods;
//import betterwithmods.api.client.IColorable;
//import betterwithmods.client.ClientEventHandler;
//import betterwithmods.client.ColorHandlers;
//import betterwithmods.client.ResourcePackProxy;
//import betterwithmods.client.baking.BarkModel;
//import betterwithmods.client.model.baking.RenderUtils;
//import betterwithmods.client.baking.*;
//import betterwithmods.client.tesr.*;
//import betterwithmods.common.BWMBlocks;
//import betterwithmods.common.BWMItems;
//import betterwithmods.common.blocks.BlockPlanter;
//import betterwithmods.common.entity.*;
//import betterwithmods.common.tile.*;
//import betterwithmods.lib.ModLib;
//import betterwithmods.library.lib.ReflectionLib;
//import betterwithmods.module.hardcore.beacons.TileBeacon;
//import betterwithmods.module.hardcore.crafting.HCFurnace;
//import betterwithmods.module.hardcore.creatures.EntityTentacle;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.color.BlockColors;
//import net.minecraft.client.renderer.color.ItemColors;
//import net.minecraft.client.renderer.entity.RenderSnowball;
//import net.minecraft.client.resources.IResourcePack;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemBlock;
//import net.minecraft.tileentity.TileEntityFurnace;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.ColorizerGrass;
//import net.minecraft.world.biome.BiomeColorHelper;
//import net.minecraftforge.client.event.ModelBakeEvent;
//import net.minecraftforge.client.event.ModelRegistryEvent;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.fml.client.registry.ClientRegistry;
//import net.minecraftforge.fml.client.registry.RenderingRegistry;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.relauncher.ReflectionHelper;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.List;
//
//@SuppressWarnings("unused")
//@Mod.EventBusSubscriber(modid = ModLib.MODID, value = Side.CLIENT)
//public class ClientProxy implements IProxy {
//
//
//
//    @SubscribeEvent
//    public static void registerModels(ModelRegistryEvent event) {
//        BWMItems.getItems().forEach(BWMItems::setInventoryModel);
//    }
//
//    @Override
//    public void onPreInit(FMLPreInitializationEvent event) {
//        BetterWithMods.MODULE_LOADER.onPreInitClient(event);
//        registerRenderInformation();
//        initRenderers();
//        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
//    }
//
//    @Override
//    public void onInit(FMLInitializationEvent event) {
//        List<IResourcePack> packs = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionLib.DEFAULT_RESOURCE_PACKS);
//        registerColors();
//        BetterWithMods.MODULE_LOADER.onInitClient(event);
//    }
//
//    @Override
//    public void onPostInit(FMLPostInitializationEvent event) {
//        BetterWithMods.MODULE_LOADER.onPostInitClient(event);
//    }
//
//
//
//
//    @Override
//    public String getName() {
//        return "ClientProxy";
//    }
//
//
//}
