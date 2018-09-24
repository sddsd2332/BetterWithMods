package betterwithmods.network;

import betterwithmods.lib.ModLib;
import betterwithmods.library.network.NetworkHandler;
import betterwithmods.network.handler.*;
import betterwithmods.network.messages.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class BWMNetwork extends NetworkHandler {

    public static BWMNetwork INSTANCE = new BWMNetwork();

    private BWMNetwork() {
        super(ModLib.MODID);
    }


    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        registerMessage(MessageHungerShakeHandler.class, MessageHungerShake.class, Side.CLIENT);
        registerMessage(MessageHarnessHandler.class, MessageHarness.class, Side.CLIENT);
        registerMessage(MessageRotateHandler.class, MessageRotate.class, Side.CLIENT);
        registerMessage(MessageGloomHandler.class, MessageGloom.class, Side.CLIENT);
        registerMessage(MessagePlacedHandler.class, MessagePlaced.class, Side.CLIENT);
        registerMessage(MessageCustomDustHandler.class, MessageCustomDust.class, Side.CLIENT);
    }

    @Override
    public String getDescription() {
        return null;
    }
}
