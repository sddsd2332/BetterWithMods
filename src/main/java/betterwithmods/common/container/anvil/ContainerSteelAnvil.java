package betterwithmods.common.container.anvil;

import betterwithmods.client.gui.GuiSteelAnvil;
import betterwithmods.common.tile.TileSteelAnvil;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.common.container.SlotTransformation;
import betterwithmods.library.mod.BetterWithLib;
import betterwithmods.library.utils.GuiUtils;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ContainerSteelAnvil extends ContainerTile<TileSteelAnvil> {

    public static final ResourceLocation SLOTS_RESULT = new ResourceLocation(BetterWithLib.MODID, "result");


    protected InventoryAnvilCrafting craftingMatrix;
    private InventoryAnvilResult craftingResults;

    public ContainerSteelAnvil(TileSteelAnvil tile, EntityPlayer player) {
        super(tile, player);

        this.craftingMatrix = new InventoryAnvilCrafting(this, tile.inventory);
        this.craftingResults = new InventoryAnvilResult(this, tile.getResult(), craftingMatrix);

        GuiUtils.createPlayerSlots(player, this, 8, 102, 8, 160);
        GuiUtils.createContainerSlots(SLOTS_RESULT, this, this.craftingResults, 1, 1, 0, 124, 44, (inventory, i, x, y) -> new SlotAnvilCrafting(player, craftingMatrix, craftingResults, i, x, y));
        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY, this, craftingMatrix, 16, 4, 0, 12, 17, ((inventory, i, x, y) -> new Slot((InventoryAnvilCrafting) inventory, i, x, y)));


        addSlotTransformations(new SlotTransformation(SLOTS_RESULT, GuiUtils.SLOTS_FULL_PLAYER_INVENTORY) {
            @Override
            public void onPostTransfer(Slot slot, ItemStack start, ItemStack end) {
                slot.onSlotChange(start, end);
            }
        });
        onCraftMatrixChanged(craftingMatrix);
    }


    @Override
    public GuiContainer createGui() {
        return new GuiSteelAnvil(this);
    }


    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        this.craftingResults.setInventorySlotContents(0, RecipeRegistry.ANVIL.findMatchingResult((InventoryCrafting) matrix, getTile().getWorld()));
    }

}
