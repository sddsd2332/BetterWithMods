package betterwithmods.module.tweaks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import com.google.common.collect.Lists;
import net.minecraft.entity.monster.*;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class NetherFortressSpawns extends Feature {

    @Override
    public String getDescription() {
        return "Improvements to the spawn requirements for NetherFortress Mob Spawning. Change it so all NetherFortress Mobs can spawn on any block in a chunk that contains a piece of Nether Fortress";
    }

    public static final Biome.SpawnListEntry[] SPAWNS = new Biome.SpawnListEntry[]{
            new Biome.SpawnListEntry(EntityBlaze.class, 10, 2, 3),
            new Biome.SpawnListEntry(EntityPigZombie.class, 5, 4, 4),
            new Biome.SpawnListEntry(EntityWitherSkeleton.class, 8, 5, 5),
            new Biome.SpawnListEntry(EntitySkeleton.class, 2, 5, 5),
            new Biome.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4)
    };

    @SubscribeEvent
    public static void getPotentialSpawns(WorldEvent.PotentialSpawns event) {
        World world = event.getWorld();
        if (world.isRemote)
            return;
        if (event.getWorld().provider.getDimensionType() == DimensionType.NETHER) {

            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                if (generator instanceof ChunkGeneratorHell) {
                    ChunkGeneratorHell hell = (ChunkGeneratorHell) generator;
                    Chunk chunk = world.getChunk(event.getPos());

                    if (hell.genNetherBridge.canSpawnStructureAtCoords(chunk.x, chunk.z)) {
                        event.getList().clear();
                        event.getList().addAll(Lists.newArrayList(SPAWNS));
                    }

                }
            }
        }
    }
}
