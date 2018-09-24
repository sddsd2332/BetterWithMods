package betterwithmods.network.messages;

import betterwithmods.library.network.Message;
import io.netty.buffer.ByteBuf;

public class MessageRotate extends Message {
    public int entity;
    public float yaw, pitch;

    public MessageRotate() {
    }

    public MessageRotate(int entity, float yaw, float pitch) {
        this.entity = entity;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entity = readData(buf, int.class);
        this.yaw = readData(buf, float.class);
        this.pitch = readData(buf, float.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeData(buf, entity);
        writeData(buf, yaw);
        writeData(buf, pitch);
    }
}
