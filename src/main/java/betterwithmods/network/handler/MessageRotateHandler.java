package betterwithmods.network.handler;

import betterwithmods.network.messages.MessageRotate;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRotateHandler extends BWMessageHandler<MessageRotate> {
    @Override
    public void handleMessage(MessageRotate message, MessageContext context) {
        rotateEntity(message.entity, message.yaw, message.pitch);
    }

    public void rotateEntity(int entityId, float yaw, float pitch) {
        Entity entity = PlayerHelper.getEntityById(entityId);
        if (entity != null) {
            entity.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, yaw, pitch);
        }
    }
}
