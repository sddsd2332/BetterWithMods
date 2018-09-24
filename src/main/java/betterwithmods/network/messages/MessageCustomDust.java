package betterwithmods.network.messages;

import betterwithmods.library.network.Message;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MessageCustomDust extends Message {
    private World world;
    public BlockPos pos;
    public float x, y, z;
    public int numParticles, dim;
    public float particleSpeed;

    public MessageCustomDust(World world, BlockPos pos, double x, double y, double z, int numParticles, float particleSpeed) {
        this.world = world;
        this.pos = pos;
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
        this.numParticles = numParticles;
        this.particleSpeed = particleSpeed;
    }

    public MessageCustomDust() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = readData(buf, int.class);
        pos = readData(buf, BlockPos.class);
        x = readData(buf, float.class);
        y = readData(buf, float.class);
        z = readData(buf, float.class);
        numParticles = readData(buf, int.class);
        particleSpeed = readData(buf, float.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeData(buf, world.provider.getDimension());
        writeData(buf, pos);
        writeData(buf, x);
        writeData(buf, y);
        writeData(buf, z);
        writeData(buf, numParticles);
        writeData(buf, particleSpeed);
    }
}
