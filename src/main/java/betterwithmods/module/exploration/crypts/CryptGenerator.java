package betterwithmods.module.exploration.crypts;

import betterwithmods.lib.ModLib;
import betterwithmods.util.WorldUtils;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class CryptGenerator implements IWorldGenerator {

    public static final int CHUNK_SIZE = 16;
    private static final StoneProcessor stoneProcessor = new StoneProcessor();
    private EndRoomGenerator endRoomGenerator;
    private PlacementSettings placementSettings;
    private BlockPos endEntrancePos;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimensionType() != DimensionType.OVERWORLD)
            return;

        int x = (chunkX * CHUNK_SIZE);
        int z = (chunkZ * CHUNK_SIZE);

        final BlockPos basePos = new BlockPos(x, 100, z);
        placementSettings = new PlacementSettings().setRotation(Rotation.values()[random.nextInt(Rotation.values().length)]);

        if (world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) world;
            endRoomGenerator = new EndRoomGenerator(WorldUtils.getTemplate(worldServer, ModLib.MODID, "crypt_end"));

            endRoomGenerator.generate(random, worldServer, basePos, placementSettings);
        }
    }

}
