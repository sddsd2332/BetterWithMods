package betterwithmods.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by michaelepps on 4/3/18.
 */
@SuppressWarnings("Convert2MethodRef")
public class MessageDataHandler<DataType> {

    private static final List<MessageDataHandler<?>> handlers = Lists.newArrayList();

    static {
        addHandler(byte.class, ByteBuf::readByte, (buf, data) -> buf.writeByte(data));
        addHandler(short.class, ByteBuf::readShort, (buf, data) -> buf.writeShort(data));
        addHandler(int.class, ByteBuf::readInt, ByteBuf::writeInt);
        addHandler(long.class, ByteBuf::readLong, ByteBuf::writeLong);
        addHandler(double.class, ByteBuf::readDouble, ByteBuf::writeDouble);
        addHandler(float.class, ByteBuf::readFloat, ByteBuf::writeFloat);
        addHandler(boolean.class, ByteBuf::readBoolean, ByteBuf::writeBoolean);
        addHandler(char.class, ByteBuf::readChar, (buf, data) -> buf.writeChar(data));

        addHandler(String.class, ByteBufUtils::readUTF8String, ByteBufUtils::writeUTF8String);
        addHandler(NBTTagCompound.class, ByteBufUtils::readTag, ByteBufUtils::writeTag);
        addHandler(ItemStack.class, ByteBufUtils::readItemStack, ByteBufUtils::writeItemStack);
        addHandler(BlockPos.class, buf -> BlockPos.fromLong(buf.readLong()), (buf, data) -> buf.writeLong(data.toLong()));
        addHandler(ChunkPos.class, buf -> new ChunkPos(buf.readInt(), buf.readInt()), (buf, data) -> {
            buf.writeInt(data.x);
            buf.writeInt(data.z);
        });
    }

    private Function<ByteBuf, DataType> reader;
    private BiConsumer<ByteBuf, DataType> writer;
    private Class<DataType> type;

    private MessageDataHandler(Class<DataType> type, Function<ByteBuf, DataType> reader, BiConsumer<ByteBuf, DataType> writer) {
        this.reader = reader;
        this.writer = writer;
        this.type = type;
    }

    @ParametersAreNonnullByDefault
    public static <DataType> void addHandler(Class<DataType> type, Function<ByteBuf, DataType> reader, BiConsumer<ByteBuf, DataType> writer) {
        handlers.add(new MessageDataHandler<>(type, reader, writer));
    }

    public static MessageDataHandler getHandler(@Nonnull Class type) {
        for (MessageDataHandler<?> handler : handlers) {
            if (handler.typeMatches(type)) {
                return handler;
            }
        }

        //Just error here because at this point something
        //has gone very wrong and the packet will crash either way
        throw new RuntimeException("Cannot read packet data! Unsupported data type!");
    }

    public DataType read(@Nonnull ByteBuf buf) {
        return reader.apply(buf);
    }

    public void write(@Nonnull ByteBuf buf, @Nonnull DataType data) {
        writer.accept(buf, data);
    }

    private boolean typeMatches(Class<?> clazz) {
        if (Primitives.isWrapperType(clazz)) {
            clazz = Primitives.unwrap(clazz);
        }
        return clazz.equals(type) || type.isAssignableFrom(clazz);
    }
}