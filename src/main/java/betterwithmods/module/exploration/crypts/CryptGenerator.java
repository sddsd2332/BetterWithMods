package betterwithmods.module.exploration.crypts;

import betterwithmods.lib.ModLib;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class CryptGenerator implements IWorldGenerator {

    private EndRoomGenerator endRoomGenerator;
    private PlacementSettings placementSettings;
    private BlockPos endEntrancePos;

    private static final StoneProcessor stoneProcessor = new StoneProcessor();

    public static final int CHUNK_SIZE = 16;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimensionType() != DimensionType.OVERWORLD)
            return;

        int x = (chunkX * CHUNK_SIZE) + 8 + random.nextInt(16);
        int z = (chunkZ * CHUNK_SIZE) + 8 + random.nextInt(16);

        final BlockPos basePos = new BlockPos(x, 100, z);
        placementSettings = new PlacementSettings().setRotation(Rotation.values()[random.nextInt(Rotation.values().length)]);

        if (world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) world;
            endRoomGenerator = new EndRoomGenerator(getTemplate(worldServer, "crypt_end"));

            endRoomGenerator.generate(random, worldServer, basePos, placementSettings);
        }
    }

    private Template getTemplate(WorldServer worldServer, String templateName) {
        return worldServer.getStructureTemplateManager().getTemplate(worldServer.getMinecraftServer(), new ResourceLocation(ModLib.MODID, templateName));
    }
}
