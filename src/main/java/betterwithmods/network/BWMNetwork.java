package betterwithmods.network;

import betterwithmods.BetterWithMods;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.GuiHandler;
import betterwithmods.library.common.network.NetworkHandler;
import betterwithmods.network.handler.*;
import betterwithmods.network.messages.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class BWMNetwork extends NetworkHandler {

    public static BWMNetwork INSTANCE = new BWMNetwork();
    public static GuiHandler GUI_HANDLER = new GuiHandler();


    private BWMNetwork() {
        super(ModLib.MODID);
    }


    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(BetterWithMods.instance, GUI_HANDLER);

        registerMessage(MessageHungerShakeHandler.class, MessageHungerShake.class, Side.CLIENT);
        registerMessage(MessageHarnessHandler.class, MessageHarness.class, Side.CLIENT);
        registerMessage(MessageRotateHandler.class, MessageRotate.class, Side.CLIENT);
        registerMessage(MessageGloomHandler.class, MessageGloom.class, Side.CLIENT);
        registerMessage(MessagePlacedHandler.class, MessagePlaced.class, Side.CLIENT);

        registerMessage(MessageStructureRequestHandler.class, MessageStructureRequest.class, Side.SERVER);
        registerMessage(MessageStructureReplyHandler.class, MessageStructureReply.class, Side.CLIENT);
    }

    @Override
    public String getDescription() {
        return null;
    }
}
