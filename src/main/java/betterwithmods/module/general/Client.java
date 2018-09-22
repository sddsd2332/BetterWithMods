package betterwithmods.module.general;

import betterwithmods.client.ClientEventHandler;
import betterwithmods.client.gui.GuiStatus;
import betterwithmods.common.tile.TileAxleGenerator;
import betterwithmods.module.RequiredFeature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Client extends RequiredFeature {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        TileAxleGenerator.generatorRenderDistance = loadProperty("Generator Render Distance", 256)
                .setComment("Distance away that a Windmill or Waterwheel will render")
                .setCategoryComment(getDescription()).get();

        GuiStatus.offsetY = loadProperty("Y offset", 0).subCategory("status gui").get();
        GuiStatus.offsetX = loadProperty("X offset", 0).subCategory("status gui").setCategoryComment("Set offset for Status HUD for effects such as hunger and gloom").get();

        ClientEventHandler.blockPlacementHighlight = loadProperty("Show Block Placement", true).setComment("Render Block Placement Helper for MiniBlocks, Gearboxes and more").get();
    }


    @Override
    public String getDescription() {
        return "Change settings for the Client";
    }

}
