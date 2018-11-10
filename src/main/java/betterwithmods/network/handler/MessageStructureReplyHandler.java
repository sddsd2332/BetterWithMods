package betterwithmods.network.handler;

import betterwithmods.library.common.network.MessageHandler;
import betterwithmods.module.tweaks.NetherFortressSpawns;
import betterwithmods.network.messages.MessageStructureReply;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStructureReplyHandler extends MessageHandler<MessageStructureReply> {

    @Override
    public void handleMessage(MessageStructureReply message, MessageContext context) {
        NetherFortressSpawns.FORTRESSES.put(message.chunk, message.contained);
    }
}
