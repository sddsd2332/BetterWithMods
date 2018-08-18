package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

@Mod.EventBusSubscriber
public class VisibleStorms extends Feature {
    private static boolean DUST_STORMS;
    private static boolean SAND_STORMS;
    private static int DUST_PARTICLES;
    private static int AIR_PARTICLES;
    private static float currentRed, currentGreen, currentBlue;
    private static float currentDistance, currentDistanceScale;
    private static float desiredRed, desiredGreen, desiredBlue;
    private static float desiredDistance, desiredDistanceScale;

    @SideOnly(Side.CLIENT)
    private static void renderFog(int fogMode, float farPlaneDistance, float farPlaneDistanceScale) {
        if (fogMode < 0) {
            GlStateManager.setFogStart(0.0F);
            GlStateManager.setFogEnd(farPlaneDistance);
        } else {
            GlStateManager.setFogEnd(farPlaneDistance);
            GlStateManager.setFogStart(farPlaneDistance * farPlaneDistanceScale);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent tickEvent) {
        EntityPlayer entity = tickEvent.player;
        if (entity == null)
            return;
        World world = entity.world;
        if (world == null || !world.isRemote)
            return;
        if (DUST_STORMS) {
            ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;

            Random random = world.rand;
            BlockPos pos = entity.getPosition();
            int radius = 16; //blocks
            for (int i = 0; i < DUST_PARTICLES; i++) {
                BlockPos posGround = pos.add(random.nextInt(radius * 2 + 1) - radius, random.nextInt(radius * 2 + 1) - radius, random.nextInt(radius * 2 + 1) - radius);
                if (!shouldStorm(world, posGround))
                    continue;
                posGround = world.getHeight(posGround).down(); //Constant access whaaaat???

                IBlockState stateGround = world.getBlockState(posGround);
                Particle particleGround = particleManager.spawnEffectParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), posGround.getX() + random.nextDouble(), posGround.getY() + 1.2, posGround.getZ() + random.nextDouble(), -0.5 - random.nextDouble() * 0.6, 0.0, 0.0, Block.getStateId(stateGround));
            }

            for (int i = 0; i < AIR_PARTICLES; i++) {
                BlockPos posAir = pos.add(random.nextInt(radius * 2 + 1) - radius, random.nextInt(radius * 2 + 1) - radius, random.nextInt(radius * 2 + 1) - radius);
                if (world.canSeeSky(posAir) && shouldStorm(world, posAir)) {
                    Particle particleAir = particleManager.spawnEffectParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), posAir.getX() + random.nextDouble(), posAir.getY() + random.nextDouble(), posAir.getZ() + random.nextDouble(), -0.5 - random.nextDouble() * 0.6, 0.0, 0.0);
                    if (particleAir != null)
                        particleAir.setRBGColorF(1.0f, 1.0f, 1.0f);
                }
            }
        }

        if (SAND_STORMS) {
            float epsilon = 0.001f;
            if (Math.abs(currentDistance - desiredDistance) > epsilon)
                currentDistance += (desiredDistance - currentDistance) * 0.2; //TODO: We can do better.
            if (Math.abs(currentDistanceScale - desiredDistanceScale) > epsilon)
                currentDistanceScale += (desiredDistanceScale - currentDistanceScale) * 0.2; //TODO: We can do better.
            if (Math.abs(currentRed - desiredRed) > epsilon)
                currentRed += (desiredRed - currentRed) * 0.2;
            if (Math.abs(currentGreen - desiredGreen) > epsilon)
                currentGreen += (desiredGreen - currentGreen) * 0.2;
            if (Math.abs(currentBlue - desiredBlue) > epsilon)
                currentBlue += (desiredBlue - currentBlue) * 0.2;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void fogDistance(EntityViewRenderEvent.RenderFogEvent fogEvent) {
        if (!SAND_STORMS)
            return;
        Entity entity = fogEvent.getEntity();
        World world = entity.world;
        BlockPos pos = entity.getPosition();

        if (world.isRaining()) {
            desiredDistance = 0;
            desiredDistanceScale = 0;
            int totalweight = 0;
            BlockPos[] probes = new BlockPos[]{pos, pos.add(1, 0, 0), pos.add(0, 0, 1), pos.add(-1, 0, 0), pos.add(0, 0, -1)};
            for (BlockPos probepos : probes) {
                boolean aboveground = world.canSeeSky(probepos);
                if (isDesert(world, probepos) && aboveground) {
                    desiredDistance += fogEvent.getFarPlaneDistance() / 3f;
                    desiredDistanceScale += -1.0f;
                    totalweight += 1;
                } else if (aboveground) {
                    desiredDistance += fogEvent.getFarPlaneDistance();
                    desiredDistanceScale += 0.75f;
                    totalweight += 1;
                }
            }
            desiredDistance /= totalweight;
            desiredDistanceScale /= totalweight;
        } else {
            desiredDistance = fogEvent.getFarPlaneDistance();
            desiredDistanceScale = 0.75F;
        }

        if (Math.abs(fogEvent.getFarPlaneDistance() - currentDistance) > 0.001f)
            renderFog(fogEvent.getFogMode(), currentDistance, currentDistanceScale);

        //renderFog(fogEvent.getFogMode(),fogEvent.getFarPlaneDistance(),-1.0f);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void fogColor(EntityViewRenderEvent.FogColors fogEvent) {
        if (!SAND_STORMS)
            return;
        Entity entity = fogEvent.getEntity();
        World world = entity.world;
        BlockPos pos = entity.getPosition();
        Color desiredcolor = new Color(
                Math.min(fogEvent.getRed(), 1.0f),
                Math.min(fogEvent.getGreen(), 1.0f),
                Math.min(fogEvent.getBlue(), 1.0f)
        );

        if (world.isRaining()) {
            float red = 0;
            float green = 0;
            float blue = 0;
            int totalweight = 0;
            BlockPos[] probes = new BlockPos[]{pos, pos.add(1, 0, 0), pos.add(0, 0, 1), pos.add(-1, 0, 0), pos.add(0, 0, -1)};
            for (BlockPos probepos : probes) {
                boolean aboveground = world.canSeeSky(probepos);
                if (isDesert(world, probepos)) {
                    Biome biome = world.getBiome(probepos);
                    MapColor mapcolor = biome.topBlock.getMapColor(world, probepos);
                    Color color = new Color(mapcolor.colorValue);
                    red += 2 * (color.getRed() / 255.0f);
                    green += 2 * (color.getGreen() / 255.0f);
                    blue += 2 * (color.getBlue() / 255.0f);
                    totalweight += 2;
                } else if (aboveground) {
                    red += fogEvent.getRed();
                    green += fogEvent.getGreen();
                    blue += fogEvent.getBlue();
                    totalweight += 1;
                }
            }
            desiredcolor = new Color(Math.min(red / totalweight, 1.0f), Math.min(green / totalweight, 1.0f), Math.min(blue / totalweight, 1.0f));
            fogEvent.setRed(currentRed / 255.0f);
            fogEvent.setGreen(currentGreen / 255.0f);
            fogEvent.setBlue(currentBlue / 255.0f);
        }

        desiredRed = desiredcolor.getRed();
        desiredGreen = desiredcolor.getGreen();
        desiredBlue = desiredcolor.getBlue();
    }

    private static boolean shouldStorm(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos);

        return world.isRaining() && !biome.canRain() && !biome.isSnowyBiome();
    }

    private static boolean isDesert(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos);

        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY);
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        DUST_STORMS = loadProperty("Dust Storms", true).setComment("Storms are clearly visible in dry biomes.").get();
        SAND_STORMS = loadProperty("Sand Storms", true).setComment("Adds a fog change during storms in deserts.").get();
        DUST_PARTICLES = loadProperty("Dust Particle Count", 2).setComment("How many dust particles should be created, too many may contribute to lag.").get();
        AIR_PARTICLES = loadProperty("Air Particle Count", 3).setComment("How many air particles should be created, too many may contribute to lag.").get();
    }


    @Override
    public String getDescription() {
        return "Add Sandstorms visual effects when it is raining in desert biomes. This helps the player know why a windmill will still break when there is no actual rain.";
    }
}
