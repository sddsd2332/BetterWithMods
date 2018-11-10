package betterwithmods.module.hardcore.world;

import betterwithmods.module.hardcore.world.spawn.HCSpawn;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DebugCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "debug";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {

        new Thread(() -> {
            try {
                FileWriter writer = new FileWriter(new File("/home/tyler/hcspawn.csv"));
                writer.write("pos,pos\n");
                World world = sender.getEntityWorld();
                for (int i = 0; i < 2000; i++) {
                    BlockPos pos = HCSpawn.getRandomPoint(world, world.getSpawnPoint(), 0, 2000);
                    ChunkPos c = world.getChunk(pos).getPos();
                    writer.write(String.format("%d,%d\n", c.getXStart(), c.getZStart()));
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


    }
}
