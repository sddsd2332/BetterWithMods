package betterwithmods.module.tweaks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.monster.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class NetherFortressSpawns extends Feature {

    private static final Object2BooleanMap<ChunkPos> NETHERFORTRESSES = new Object2BooleanOpenHashMap<>();

    @Override
    public String getDescription() {
        return "Improvements to the spawn requirements for NetherFortress Mob Spawning. Change it so all NetherFortress Mobs can spawn on any block in a chunk that contains a piece of Nether Fortress";
    }

    public static final Biome.SpawnListEntry[] SPAWNS = new Biome.SpawnListEntry[]{
            new Biome.SpawnListEntry(EntityBlaze.class, 10, 2, 3),
            new Biome.SpawnListEntry(EntityPigZombie.class, 5, 4, 4),
            new Biome.SpawnListEntry(EntityWitherSkeleton.class, 8, 5, 5),
            new Biome.SpawnListEntry(EntitySkeleton.class, 2, 5, 5),
            new Biome.SpawnListEntry(EntityMagmaCube.class, 1, 4, 4)
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
                    if (hasNetherFortress(hell.genNetherBridge, event.getPos())) {
                        event.getList().clear();
                        event.getList().addAll(Lists.newArrayList(SPAWNS));
                    }
                }
            }
        }
    }

    public static boolean hasNetherFortress(MapGenStructure structure, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        if (NETHERFORTRESSES.containsKey(chunkPos))
            return NETHERFORTRESSES.get(chunkPos);
        boolean value = isStructureInChunk(structure, pos);
        NETHERFORTRESSES.put(chunkPos, value);
        return value;
    }

    public static StructureBoundingBox roundToChunk(StructureBoundingBox box) {
        return new StructureBoundingBox((box.minX >> 4) * 16, 0, (box.minZ >> 4) * 16, (box.maxX >> 4) * 16, 128, (box.maxZ >> 4) * 16);
    }

    public static boolean isStructureInChunk(MapGenStructure structure, BlockPos pos) {
        ObjectIterator objectiterator = structure.structureMap.values().iterator();
        label31:

        while (objectiterator.hasNext()) {
            StructureStart structurestart = (StructureStart) objectiterator.next();
            StructureBoundingBox box1 = roundToChunk(structurestart.getBoundingBox());
            if (structurestart.isSizeableStructure() && box1.isVecInside(pos)) {
                Iterator<StructureComponent> iterator = structurestart.getComponents().iterator();

                while (true) {
                    if (!iterator.hasNext()) {
                        continue label31;
                    }

                    StructureComponent structurecomponent = iterator.next();

                    if (roundToChunk(structurecomponent.getBoundingBox()).isVecInside(pos)) {
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

}
