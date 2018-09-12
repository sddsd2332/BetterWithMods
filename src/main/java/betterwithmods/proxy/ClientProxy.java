package betterwithmods.proxy;

import betterwithmods.BWMod;
import betterwithmods.api.client.IColorable;
import betterwithmods.client.BWParticleDigging;
import betterwithmods.client.ClientEventHandler;
import betterwithmods.client.ColorHandlers;
import betterwithmods.client.ResourceProxy;
import betterwithmods.client.baking.BarkModel;
import betterwithmods.client.baking.IStateParticleBakedModel;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.client.render.*;
import betterwithmods.client.tesr.*;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.common.blocks.BlockPlanter;
import betterwithmods.common.entity.*;
import betterwithmods.common.tile.*;
import betterwithmods.module.hardcore.beacons.TileBeacon;
import betterwithmods.module.hardcore.crafting.HCFurnace;
import betterwithmods.module.hardcore.creatures.EntityTentacle;
import betterwithmods.util.ReflectionLib;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = BWMod.MODID, value = Side.CLIENT)
public class ClientProxy implements IProxy {

    private static ResourceProxy resourceProxy;

    static {
        List<IResourcePack> packs = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionLib.DEFAULT_RESOURCE_PACKS);
        resourceProxy = new ResourceProxy();
        packs.add(resourceProxy);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onPostBake(ModelBakeEvent event) {
        BarkModel.BARK = new BarkModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "item/bark")));
        event.getModelRegistry().putObject(BarkModel.LOCATION, BarkModel.BARK);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        BWMItems.getItems().forEach(BWMItems::setInventoryModel);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMod.MODULE_LOADER.onPreInitClient(event);
        registerRenderInformation();
        initRenderers();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        List<IResourcePack> packs = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionLib.DEFAULT_RESOURCE_PACKS);
        registerColors();
        BWMod.MODULE_LOADER.onInitClient(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        BWMod.MODULE_LOADER.onPostInitClient(event);
    }

    private void registerRenderInformation() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindmillHorizontal.class, new TESRWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindmillVertical.class, new TESRVerticalWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWaterwheel.class, new TESRWaterwheel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFilteredHopper.class, new TESRFilteredHopper());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCauldron.class, new TESRCookingPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucible.class, new TESRCookingPot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBeacon.class, new TESRBeacon());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBucket.class, new TESRBucket());
        if (BWMod.MODULE_LOADER.isFeatureEnabled(HCFurnace.class)) {
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFurnace.class, new TESRFurnaceContent());
        }
    }

    private void registerColor(ItemColors registry, Item item) {
        if (item instanceof IColorable) {
            registry.registerItemColorHandler(((IColorable) item).getColorHandler(), item);
        }
    }

    private void registerColors() {
        final BlockColors BLOCK_COLORS = Minecraft.getMinecraft().getBlockColors();
        final ItemColors ITEM_COLORS = Minecraft.getMinecraft().getItemColors();

        BLOCK_COLORS.registerBlockColorHandler(ColorHandlers.BLOCK_PLANTER, BlockPlanter.BLOCKS.get(BlockPlanter.EnumType.GRASS));
        BLOCK_COLORS.registerBlockColorHandler(ColorHandlers.BLOCK_FOLIAGE, BWMBlocks.VINE_TRAP);
        BLOCK_COLORS.registerBlockColorHandler(ColorHandlers.BLOCK_BLOOD_LEAF, BWMBlocks.BLOOD_LEAVES);
        BLOCK_COLORS.registerBlockColorHandler(ColorHandlers.BLOCK_GRASS, BWMBlocks.DIRT_SLAB);

        ITEM_COLORS.registerItemColorHandler(ColorHandlers.ITEM_PLANTER, BlockPlanter.BLOCKS.get(BlockPlanter.EnumType.GRASS));
        ITEM_COLORS.registerItemColorHandler(ColorHandlers.ITEM_FOLIAGE, BWMBlocks.VINE_TRAP);
        ITEM_COLORS.registerItemColorHandler(ColorHandlers.ITEM_BLOOD_LEAF, BWMBlocks.BLOOD_LEAVES);
        ITEM_COLORS.registerItemColorHandler(ColorHandlers.ITEM_GRASS, BWMBlocks.DIRT_SLAB);
        BWMItems.getItems().forEach(item -> registerColor(ITEM_COLORS, item));
        BLOCK_COLORS.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D), BWMBlocks.DIRT_SLAB);
        ITEM_COLORS.registerItemColorHandler((stack, tintIndex) -> {
            IBlockState iblockstate = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return BLOCK_COLORS.colorMultiplier(iblockstate, null, null, tintIndex);
        }, BWMBlocks.DIRT_SLAB);
    }

    private void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, manager -> new RenderSnowball<>(manager, BWMItems.DYNAMITE, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityUrn.class, RenderUrn::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningCharge.class, RenderMiningCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtendingRope.class, RenderExtendingRope::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShearedCreeper.class, RenderShearedCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBroadheadArrow.class, RenderBroadheadArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiderWeb.class, manager -> new RenderSnowball<>(manager, Item.getItemFromBlock(Blocks.WEB), Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityJungleSpider.class, RenderJungleSpider::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTentacle.class, RenderTentacle::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySitMount.class, RenderInvisible::new);
    }


    @Override
    public void addResourceOverride(String space, String dir, String file, String ext) {
        resourceProxy.addResource(space, dir, file, ext);
    }

    @Override
    public boolean addRunningParticles(IBlockState state, World world, BlockPos pos, Entity entity) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof IStateParticleBakedModel) {
            state = state.getBlock().getExtendedState(state.getActualState(world, pos), world, pos);
            TextureAtlasSprite sprite = ((IStateParticleBakedModel) model).getParticleTexture(state, EnumFacing.UP);

            Particle particle = new BWParticleDigging(world, entity.posX + ((double) world.rand.nextFloat() - 0.5D) * (double) entity.width, entity.getEntityBoundingBox().minY + 0.1D, entity.posZ + ((double) world.rand.nextFloat() - 0.5D) * (double) entity.width, -entity.motionX * 4.0D, 1.5D, -entity.motionZ * 4.0D,
                    state, pos, sprite, ((BWMBlock) state.getBlock()).getParticleTintIndex());
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);

            return true;
        } else {
            return false;
        }
    }

}
