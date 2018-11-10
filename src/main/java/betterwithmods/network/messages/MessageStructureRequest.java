package betterwithmods.network.messages;

import betterwithmods.library.common.network.Message;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class MessageStructureRequest extends Message {
    public MessageStructureReply.EnumStructure structure;
    public BlockPos pos;

    public MessageStructureRequest(MessageStructureReply.EnumStructure structure, BlockPos pos) {
        this.structure = structure;
        this.pos = pos;
    }

    public MessageStructureRequest() {
        pos = new BlockPos(0, 0, 0);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeData(buf, structure.ordinal());
        writeData(buf, pos);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int i = readData(buf, int.class);
        structure = MessageStructureReply.EnumStructure.VALUES[i];
        pos = readData(buf, BlockPos.class);
    }

}
