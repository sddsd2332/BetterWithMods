package betterwithmods.client.gui.bulk;

import betterwithmods.common.container.bulk.ContainerCrucible;

public class GuiCrucible extends GuiCookingPot<ContainerCrucible> {
    public GuiCrucible(ContainerCrucible container) {
        super(container);
    }

    @Override
    public String getTitle() {
        return "inv.crucible.name";
    }
}
