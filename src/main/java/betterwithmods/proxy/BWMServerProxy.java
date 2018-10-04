package betterwithmods.proxy;

import betterwithmods.common.event.FakePlayerHandler;
import betterwithmods.library.common.modularity.impl.proxy.ServerProxy;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class BWMServerProxy extends ServerProxy {
    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        FakePlayerHandler.reset();
    }
}
