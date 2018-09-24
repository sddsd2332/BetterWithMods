package betterwithmods.network.handler;

import betterwithmods.library.network.MessageHandler;
import betterwithmods.module.hardcore.needs.HCGloom;
import betterwithmods.network.messages.MessageGloom;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGloomHandler extends MessageHandler<MessageGloom> {
    @Override
    public void handleMessage(MessageGloom message, MessageContext context) {
        if (message.uuid != null) {
            HCGloom.syncGloom(message.uuid, message.gloom);
        }
    }
}
