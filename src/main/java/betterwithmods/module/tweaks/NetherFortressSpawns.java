package betterwithmods.module.tweaks;

import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.network.BWMNetwork;
import betterwithmods.network.messages.MessageStructureReply;
import betterwithmods.network.messages.MessageStructureRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.monster.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.Map;


public class NetherFortressSpawns extends Feature {

    public static transient final Map<ChunkPos, Boolean> FORTRESSES = Maps.newConcurrentMap();
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

        if (hasNetherFortress(world, event.getPos())) {
            event.getList().clear();
            event.getList().addAll(Lists.newArrayList(SPAWNS));
        }
    }

    public static MapGenStructure getNetherFortress(World world) {
        if (world instanceof WorldServer) {
            if (world.provider.getDimensionType() == DimensionType.NETHER) {
                IChunkProvider provider = world.getChunkProvider();
                if (provider instanceof ChunkProviderServer) {
                    IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                    if (generator instanceof ChunkGeneratorHell) {
                        ChunkGeneratorHell hell = (ChunkGeneratorHell) generator;
                        return hell.genNetherBridge;
                    }
                }

            }
        }
        return null;
    }

    public static boolean hasNetherFortress(World world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        if (FORTRESSES.containsKey(chunkPos))
            return FORTRESSES.get(chunkPos);

        if (world.isRemote) {

            BWMNetwork.INSTANCE.sendToServer(new MessageStructureRequest(MessageStructureReply.EnumStructure.NETHER_FORTRESS, pos));
            return false;
        }

        MapGenStructure netherFortress = getNetherFortress(world);
        if (netherFortress == null)
            return false;
        boolean value = isStructureInChunk(netherFortress, pos);
//        BWMNetwork.INSTANCE.sendToAllAround(new MessageStructureReply(MessageStructureReply.EnumStructure.NETHER_FORTRESS, chunkPos, value), world, pos, 10);
        FORTRESSES.put(chunkPos, value);
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

    @Override
    public String getDescription() {
        return "Improvements to the spawn requirements for NetherFortress Mob Spawning. Change it so all NetherFortress Mobs can spawn on any block in a pos that contains a piece of Nether Fortress";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }


}
