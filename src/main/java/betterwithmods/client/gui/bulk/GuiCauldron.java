package betterwithmods.client.gui.bulk;

import betterwithmods.common.container.bulk.ContainerCauldron;

public class GuiCauldron extends GuiCookingPot<ContainerCauldron> {
    public GuiCauldron(ContainerCauldron container) {
        super(container);
    }

    @Override
    public String getTitle() {
        return "inv.cauldron.name";
    }
}
