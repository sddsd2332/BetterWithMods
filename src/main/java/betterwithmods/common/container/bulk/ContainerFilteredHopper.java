package betterwithmods.common.container.bulk;

import betterwithmods.common.registry.hopper.filters.HopperFilter;
import betterwithmods.common.tile.TileFilteredHopper;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.common.container.SlotTransformation;
import betterwithmods.library.utils.GuiUtils;
import betterwithmods.library.utils.ingredient.PredicateIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ContainerFilteredHopper extends ContainerTile<TileFilteredHopper> {

    private static final ResourceLocation SLOTS_FILTER = new ResourceLocation(ModLib.MODID, "hopper_filter");

    public ContainerFilteredHopper(TileFilteredHopper tile, EntityPlayer player) {
        super(tile, player);

        GuiUtils.createPlayerSlots(player, this, 8, 111, 8, 169);

        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY, this, tile.inventory, 18, 2, 0, 8, 60);
        GuiUtils.createContainerSlots(SLOTS_FILTER, this, tile.filter, 1, 1, 0, 80, 37);
        addSlotTransformations(new SlotTransformation(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY, SLOTS_FILTER, new PredicateIngredient(this::isItemFilter)));
    }

    private boolean isItemFilter(ItemStack stack) {
        return RecipeRegistry.HOPPER_FILTERS.getFilter(stack) != HopperFilter.NONE;
    }

    @Override
    public GuiContainer createGui() {
        return null;
    }
}
