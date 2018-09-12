package betterwithmods.network.handler;

import betterwithmods.module.hardcore.world.stumping.HCStumping;
import betterwithmods.network.messages.MessagePlaced;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlacedHandler extends BWMessageHandler<MessagePlaced> {
    @Override
    public void handleMessage(MessagePlaced message, MessageContext context) {
        if (message.pos != null) {
            HCStumping.syncPlaced(message.pos);
        }
    }
}
