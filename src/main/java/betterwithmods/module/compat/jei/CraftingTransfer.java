package betterwithmods.module.compat.jei;

import betterwithmods.library.common.container.ContainerBase;
import betterwithmods.library.utils.GuiUtils;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.List;

public class CraftingTransfer<C extends ContainerBase> implements IRecipeTransferInfo<C> {

    private String UID;
    private Class<C> clazz;

    public CraftingTransfer(String UID, Class<C> clazz) {
        this.UID = UID;
        this.clazz = clazz;
    }

    @Override
    public Class<C> getContainerClass() {
        return clazz;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return UID;
    }

    @Override
    public boolean canHandle(@Nonnull C container) {
        return true;
    }

    @Nonnull
    @Override
    public List<Slot> getRecipeSlots(@Nonnull C container) {
        return container.getSlotRange(GuiUtils.SLOTS_CONTAINER_INVENTORY).getSlots(container);
    }


    @Nonnull
    @Override
    public List<Slot> getInventorySlots(@Nonnull C container) {
        return container.getSlotRange(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY).getSlots(container);
    }
}
