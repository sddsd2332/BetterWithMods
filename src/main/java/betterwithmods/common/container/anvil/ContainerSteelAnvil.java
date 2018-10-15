package betterwithmods.common.container.anvil;

import betterwithmods.client.gui.GuiSteelAnvil;
import betterwithmods.common.tile.TileSteelAnvil;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.mod.BetterWithLib;
import betterwithmods.library.utils.GuiUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerSteelAnvil extends ContainerTile<TileSteelAnvil> {

    public static final ResourceLocation SLOTS_RESULT = new ResourceLocation(BetterWithLib.MODID, "result");

    private ItemStackHandler results;

    public ContainerSteelAnvil(TileSteelAnvil tile, EntityPlayer player) {
        super(tile, player);

        results = new ItemStackHandler(1);

        GuiUtils.createPlayerSlots(player, this, 8, 102, 8, 160);
        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY, this, tile.inventory, 16, 4, 0, 12, 17);
        GuiUtils.createContainerSlots(SLOTS_RESULT, this, null, 1, 1, 0, 124, 44, (inventory, i, x, y) -> new SlotCrafting( )   );
    }


    @Override
    public GuiContainer createGui() {
        return new GuiSteelAnvil(this);
    }

}