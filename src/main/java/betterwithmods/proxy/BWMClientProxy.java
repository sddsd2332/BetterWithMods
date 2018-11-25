package betterwithmods.proxy;

import betterwithmods.BetterWithMods;
import betterwithmods.client.ColorHandlers;
import betterwithmods.client.render.*;
import betterwithmods.client.tesr.*;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockPlanter;
import betterwithmods.common.entity.*;
import betterwithmods.common.tile.*;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.resourceproxy.ResourcePackProxy;
import betterwithmods.library.common.modularity.impl.ModuleLoader;
import betterwithmods.library.common.modularity.impl.proxy.ClientProxy;
import betterwithmods.module.conversion.beacons.TileBeacon;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BWMClientProxy extends ClientProxy {

    @SideOnly(Side.CLIENT)
    public static final BannerTextures.Cache WINDMILLS = new BannerTextures.Cache("betterwithmods:W", new ResourceLocation(ModLib.MODID, "textures/blocks/windmills/banner.png"), "betterwithmods:textures/blocks/windmills/");
    @SideOnly(Side.CLIENT)
    private static final ResourcePackProxy RESOURCE_PACK_PROXY = ResourcePackProxy.registerResourceProxy(() -> BetterWithMods.LOGGER, ModLib.MODID);

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        super.onPreInitClient(event);

        ClientRegistry.bindTileEntitySpecialRenderer(TileWindmillHorizontal.class, new TESRWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindmillVertical.class, new TESRVerticalWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWaterwheel.class, new TESRWaterwheel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFilteredHopper.class, new TESRFilteredHopper());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCauldron.class, new TESRCookingPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucible.class, new TESRCookingPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBeacon.class, new TESRBeacon());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBucket.class, new TESRBucket());
        ClientRegistry.bindTileEntitySpecialRenderer(TileInfernalEnchanter.class, new TESRInfernalEnchanter());

    }

    @Override
    public void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, RenderDynamite::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityUrn.class, RenderUrn::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningCharge.class, RenderMiningCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtendingRope.class, RenderExtendingRope::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShearedCreeper.class, RenderShearedCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBroadheadArrow.class, RenderBroadheadArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiderWeb.class, RenderWeb::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityJungleSpider.class, RenderJungleSpider::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySitMount.class, RenderInvisible::new);
    }

    @Override
    public void onInitClient(FMLInitializationEvent event) {
        super.onInitClient(event);
        final BlockColors blockColors = mc().getBlockColors();
        final ItemColors itemColors = mc().getItemColors();

        blockColors.registerBlockColorHandler(ColorHandlers.BLOCK_GRASS, BlockPlanter.getBlock(BlockPlanter.Type.GRASS));
        blockColors.registerBlockColorHandler(ColorHandlers.BLOCK_FOLIAGE, BWMBlocks.VINE_TRAP);
        blockColors.registerBlockColorHandler(ColorHandlers.BLOCK_BLOOD_LEAF, BWMBlocks.BLOOD_LEAVES);
        blockColors.registerBlockColorHandler(ColorHandlers.BLOCK_GRASS, BWMBlocks.DIRT_SLAB);

        itemColors.registerItemColorHandler(ColorHandlers.ITEM_GRASS, BlockPlanter.getBlock(BlockPlanter.Type.GRASS));
        itemColors.registerItemColorHandler(ColorHandlers.ITEM_FOLIAGE, BWMBlocks.VINE_TRAP);
        itemColors.registerItemColorHandler(ColorHandlers.ITEM_BLOOD_LEAF, BWMBlocks.BLOOD_LEAVES);
        itemColors.registerItemColorHandler(ColorHandlers.ITEM_GRASS, BWMBlocks.DIRT_SLAB);
    }

    @Override
    public void setLoader(ModuleLoader loader) {
        super.setLoader(loader);
        this.loader.setResourcePackProxy(RESOURCE_PACK_PROXY);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitch(TextureStitchEvent event) {
        event.getMap().registerSprite(TESRCookingPot.CAULDRON_CONTENTS);
    }
}
