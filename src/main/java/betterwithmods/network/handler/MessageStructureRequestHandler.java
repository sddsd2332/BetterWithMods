package betterwithmods.network.handler;

import betterwithmods.module.tweaks.NetherFortressSpawns;
import betterwithmods.network.messages.MessageStructureReply;
import betterwithmods.network.messages.MessageStructureRequest;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStructureRequestHandler implements IMessageHandler<MessageStructureRequest, MessageStructureReply> {

    @Override
    public MessageStructureReply onMessage(MessageStructureRequest message, MessageContext ctx) {
        ChunkPos chunk = new ChunkPos(message.pos);
        boolean contained = NetherFortressSpawns.hasNetherFortress(ctx.getServerHandler().player.server.getWorld(message.structure.getDimension().getId()), message.pos);
        return new MessageStructureReply(message.structure, chunk, contained);
    }
}
