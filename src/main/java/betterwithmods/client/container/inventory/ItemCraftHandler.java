package betterwithmods.client.container.inventory;

import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.tile.TileSteelAnvil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.ItemStackHandler;

public class ItemCraftHandler extends ItemStackHandler {

    private final TileSteelAnvil te;
    public InventoryCrafting crafting;

    public ItemCraftHandler(int size, TileSteelAnvil te) {
        super(size);
        this.te = te;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (crafting != null)
            te.setResult(BWRegistry.ANVIL.findMatchingResult(this.crafting, te.getWorld()));
        super.onContentsChanged(slot);
    }
}