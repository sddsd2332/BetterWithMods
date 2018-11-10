package betterwithmods.network.messages;

import betterwithmods.library.common.network.Message;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;

public class MessageStructureReply extends Message {

    public EnumStructure structure;
    public ChunkPos chunk;
    public boolean contained;

    public MessageStructureReply(EnumStructure structure, ChunkPos chunk, boolean contained) {
        this.structure = structure;
        this.chunk = chunk;
        this.contained = contained;
    }

    public MessageStructureReply() {
        chunk = new ChunkPos(0, 0);
    }


    @Override
    public void toBytes(ByteBuf buf) {
        writeData(buf, structure.ordinal());
        writeData(buf, chunk);
        writeData(buf, contained);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int i = readData(buf, int.class);
        structure = EnumStructure.VALUES[i];
        chunk = readData(buf, ChunkPos.class);
        contained = readData(buf, boolean.class);
    }


    public enum EnumStructure {
        NETHER_FORTRESS(DimensionType.NETHER);

        private DimensionType dimension;

        EnumStructure(DimensionType dimension) {
            this.dimension = dimension;
        }

        public DimensionType getDimension() {
            return dimension;
        }

        public static EnumStructure[] VALUES = values();

    }

}
