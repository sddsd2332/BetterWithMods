package betterwithmods.common.container.other;

import betterwithmods.client.gui.GuiPulley;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.tile.TilePulley;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.common.container.SlotItemHandlerFiltered;
import betterwithmods.library.utils.GuiUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ContainerPulley extends ContainerTile<TilePulley> {

    public ContainerPulley(TilePulley tile, EntityPlayer player) {
        super(tile, player);

        addBooleanProperty(GuiUtils.PROPERTY_SHOW_PROGRESS, () -> tile.getProgress() > 0);
        addIntProperty(GuiUtils.PROPERTY_PROGRESS, tile::getProgress);
        addIntProperty(GuiUtils.PROPERTY_MAX_PROGRESS, tile::getMax);

        Ingredient rope = Ingredient.fromStacks(new ItemStack(BWMBlocks.ROPE));
        GuiUtils.createPlayerSlots(player, this, 8, 93, 8, 151);
        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY, this, tile.inventory, 4, 1, 0, 53, 52, (inv, i, x, y) -> new SlotItemHandlerFiltered(inv, i, x, y, rope));
    }

    @Override
    public GuiContainer createGui() {
        return new GuiPulley(this);
    }
}
