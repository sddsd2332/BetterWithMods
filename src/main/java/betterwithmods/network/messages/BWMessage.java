package betterwithmods.network.messages;

import betterwithmods.network.MessageDataHandler;
import com.sun.istack.internal.NotNull;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BWMessage implements IMessage {
    protected static <DataType> DataType readData(ByteBuf buf, Class type) {
        return (DataType) MessageDataHandler.getHandler(type).read(buf);
    }

    protected static <DataType> void writeData(@NotNull ByteBuf buf, DataType data) {
        MessageDataHandler.getHandler(data.getClass()).write(buf, data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

}
