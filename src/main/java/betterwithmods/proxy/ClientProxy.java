package betterwithmods.proxy;

import betterwithmods.BWMod;
import betterwithmods.api.client.IColorable;
import betterwithmods.client.*;
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
import betterwithmods.manual.api.ManualAPI;
import betterwithmods.manual.api.prefab.manual.TextureTabIconRenderer;
import betterwithmods.manual.common.DirectoryDefaultProvider;
import betterwithmods.manual.common.api.ManualDefinitionImpl;
import betterwithmods.module.gameplay.animal_restraint.AnimalRestraint;
import betterwithmods.module.gameplay.animal_restraint.Harness;
import betterwithmods.module.hardcore.crafting.HCFurnace;
import betterwithmods.module.hardcore.creatures.EntityTentacle;
import betterwithmods.module.hardcore.needs.HCGloom;
import betterwithmods.module.hardcore.world.stumping.HCStumping;
import betterwithmods.module.hardcore.world.stumping.PlacedCapability;
import betterwithmods.util.ReflectionLib;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        ModelLoader.setCustomStateMapper(BWMBlocks.STOKED_FLAME, new BWStateMapper(BWMBlocks.STOKED_FLAME.getRegistryName()));
        BWMod.MODULE_LOADER.registerModels(event);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMod.MODULE_LOADER.preInitClient(event);
        registerRenderInformation();
        initRenderers();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        List<IResourcePack> packs = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionLib.DEFAULT_RESOURCE_PACKS);
        BWMod.MODULE_LOADER.initClient(event);
        ManualAPI.addProvider(new DirectoryDefaultProvider(new ResourceLocation(BWMod.MODID, "documentation/docs/")));
        ManualDefinitionImpl.INSTANCE.addDefaultProviders();
        ManualAPI.addTab(new TextureTabIconRenderer(new ResourceLocation(BWMod.MODID, "textures/gui/manual_home.png")), "bwm.manual.home", "%LANGUAGE%/index.md");
        registerColors();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        BWMod.MODULE_LOADER.postInitClient(event);
    }

    private void registerRenderInformation() {

        OBJLoader.INSTANCE.addDomain(BWMod.MODID);
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

        final BlockColors col = Minecraft.getMinecraft().getBlockColors();
        final ItemColors itCol = Minecraft.getMinecraft().getItemColors();

        col.registerBlockColorHandler(ColorHandlers.BlockPlanterColor, BlockPlanter.BLOCKS.get(BlockPlanter.EnumType.GRASS));
        col.registerBlockColorHandler(ColorHandlers.BlockFoliageColor, BWMBlocks.VINE_TRAP);
        col.registerBlockColorHandler(ColorHandlers.BlockBloodLeafColor, BWMBlocks.BLOOD_LEAVES);
        col.registerBlockColorHandler(ColorHandlers.BlockGrassColor, BWMBlocks.DIRT_SLAB);

        itCol.registerItemColorHandler(ColorHandlers.ItemPlanterColor, BlockPlanter.BLOCKS.get(BlockPlanter.EnumType.GRASS));
        itCol.registerItemColorHandler(ColorHandlers.ItemFoliageColor, BWMBlocks.VINE_TRAP);
        itCol.registerItemColorHandler(ColorHandlers.ItemBloodLeafColor, BWMBlocks.BLOOD_LEAVES);
        itCol.registerItemColorHandler(ColorHandlers.ItemGrassColor, BWMBlocks.DIRT_SLAB);
        BWMItems.getItems().forEach(item -> registerColor(itCol, item));
        col.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D), BWMBlocks.DIRT_SLAB);
        itCol.registerItemColorHandler((stack, tintIndex) -> {
            IBlockState iblockstate = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return col.colorMultiplier(iblockstate, null, null, tintIndex);
        }, BWMBlocks.DIRT_SLAB);
    }

    private void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, manager -> new RenderSnowball<>(manager, BWMItems.DYNAMITE, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityUrn.class, RenderUrn::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningCharge.class, RenderMiningCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtendingRope.class, RenderExtendingRope::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShearedCreeper.class, RenderShearedCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCow.class, RenderCowHarness::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPig.class, RenderPigHarness::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySheep.class, RenderSheepHarness::new);
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
    public void addResourceOverride(String space, String domain, String dir, String file, String ext) {
        resourceProxy.addResource(space, domain, dir, file, ext);
    }

    @Override
    public void syncHarness(int entityId, ItemStack harness) {
        Entity entity = getEntityByID(entityId);
        if (entity != null) {
            Harness cap = AnimalRestraint.getHarnessCapability(entity);
            if (cap != null) {
                cap.setHarness(harness);
            }
        }
    }

    @Override
    public void rotateEntity(int entityId, float yaw, float pitch) {
        Entity entity = getEntityByID(entityId);
        if (entity != null) {
            entity.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, yaw, pitch);
        }
    }

    private Entity getEntityByID(int id) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return null;
        return world.getEntityByID(id);
    }

    private EntityPlayer getPlayerById(String id) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return null;
        return world.getPlayerEntityByUUID(UUID.fromString(id));
    }

    @Override
    public void syncGloom(String entityId, int gloom) {
        EntityPlayer e = getPlayerById(entityId);
        if (e != null) {
            HCGloom.Gloom g = HCGloom.getGloom(e);
            if (g != null) {
                g.setGloom(gloom);
            }
        }
    }

    @Override
    public void syncPlaced(BlockPos[] pos) {
        World world = Minecraft.getMinecraft().world;
        if (world == null)
            return;
        PlacedCapability capability = HCStumping.getCapability(world);
        if (capability != null) {
            capability.addAll(pos);
        }
    }

    @Override
    public void spawnBlockDustClient(World world, BlockPos pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed, EnumFacing facing) {
        if (pos == null)
            return;
        TextureAtlasSprite sprite;
        int tintIndex = -1;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BWMBlock) {
            tintIndex = ((BWMBlock) state.getBlock()).getParticleTintIndex();
        }

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof IStateParticleBakedModel) {
            state = state.getBlock().getExtendedState(state.getActualState(world, pos), world, pos);
            sprite = ((IStateParticleBakedModel) model).getParticleTexture(state, facing);
        } else {
            sprite = model.getParticleTexture();
        }

        ParticleManager manager = Minecraft.getMinecraft().effectRenderer;

        for (int i = 0; i < numberOfParticles; i++) {
            double xSpeed = rand.nextGaussian() * particleSpeed;
            double ySpeed = rand.nextGaussian() * particleSpeed;
            double zSpeed = rand.nextGaussian() * particleSpeed;

            try {
                Particle particle = new BWParticleDigging(world, posX, posY, posZ, xSpeed, ySpeed, zSpeed, state, pos, sprite, tintIndex);
                manager.addEffect(particle);
            } catch (Throwable var16) {
                BWMod.logger.warn("Could not spawn block particle!");
                return;
            }
        }
    }


    public boolean addRunningParticles(IBlockState state, World world, BlockPos pos, Entity entity) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof IStateParticleBakedModel) {
            state = state.getBlock().getExtendedState(state.getActualState(world, pos), world, pos);
            TextureAtlasSprite sprite = ((IStateParticleBakedModel) model).getParticleTexture(state, EnumFacing.UP);

            double posX = entity.posY + ((double) world.rand.nextFloat() - 0.5D) * (double) entity.width, posY = entity.getEntityBoundingBox().minY + 0.1D, posZ = entity.posZ + ((double) world.rand.nextFloat() - 0.5D) * (double) entity.width;
            double vX = -entity.motionX * 4.0D, vY = 1.5D, vZ = -entity.motionZ * 4.0D;
            Particle particle = new BWParticleDigging(world, posX, posY, posZ, vX, vY, vZ,
                    state, pos, sprite, ((BWMBlock) state.getBlock()).getParticleTintIndex());

            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            return true;
        } else {
            return false;
        }
    }

    public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

        public final Fluid fluid;
        public final ModelResourceLocation location;

        public FluidStateMapper(Fluid fluid) {
            this.fluid = fluid;
            // have each block hold its fluid per nbt? hm
            this.location = new ModelResourceLocation(new ResourceLocation("betterwithmods", "fluid_block"), fluid.getName());
        }

        @Nonnull
        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            return location;
        }

        @Nonnull
        @Override
        public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
            return location;
        }
    }


}
