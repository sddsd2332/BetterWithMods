package betterwithmods.network.handler;

import betterwithmods.client.gui.GuiHunger;
import betterwithmods.library.common.network.MessageHandler;
import betterwithmods.network.messages.MessageHungerShake;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHungerShakeHandler extends MessageHandler<MessageHungerShake> {
    @Override
    public void handleMessage(MessageHungerShake message, MessageContext context) {
        GuiHunger.INSTANCE.shake();
    }
}
