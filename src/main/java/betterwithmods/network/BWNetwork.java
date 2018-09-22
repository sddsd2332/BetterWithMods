package betterwithmods.network;

import betterwithmods.lib.ModLib;
import betterwithmods.network.handler.*;
import betterwithmods.network.messages.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BWNetwork {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModLib.MODID);
    private static int i = 0;

    public static void registerNetworking() {
        registerMessage(MessageHungerShakeHandler.class, MessageHungerShake.class, Side.CLIENT);
        registerMessage(MessageHarnessHandler.class, MessageHarness.class, Side.CLIENT);
        registerMessage(MessageRotateHandler.class, MessageRotate.class, Side.CLIENT);
        registerMessage(MessageGloomHandler.class, MessageGloom.class, Side.CLIENT);
        registerMessage(MessagePlacedHandler.class, MessagePlaced.class, Side.CLIENT);
        registerMessage(MessageCustomDustHandler.class,MessageCustomDust.class, Side.CLIENT);
    }

    public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        INSTANCE.registerMessage(messageHandler, requestMessageType, i++, side);
    }


    public static void sendPacket(Entity player, Packet<?> packet) {
        if (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).connection != null) {
            ((EntityPlayerMP) player).connection.sendPacket(packet);
        }
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAllAround(IMessage message, World world, BlockPos pos) {
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 128));
    }

}
