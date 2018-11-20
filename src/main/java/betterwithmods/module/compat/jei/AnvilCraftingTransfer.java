package betterwithmods.module.compat.jei;

import betterwithmods.common.container.anvil.ContainerSteelAnvil;
import betterwithmods.library.common.container.ISlotRange;
import betterwithmods.library.utils.GuiUtils;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AnvilCraftingTransfer implements IRecipeTransferInfo<ContainerSteelAnvil> {

    @Nonnull
    @Override
    public Class<ContainerSteelAnvil> getContainerClass() {
        return ContainerSteelAnvil.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    public boolean canHandle(@Nonnull ContainerSteelAnvil container) {
        return true;
    }

    @Nonnull
    @Override
    public List<Slot> getRecipeSlots(@Nonnull ContainerSteelAnvil container) {
        ISlotRange range = container.getSlotRange(GuiUtils.SLOTS_CONTAINER_INVENTORY);
        Predicate<Slot> filter = i -> {
            int slot = (i.slotNumber - range.getStart());
            return slot < 12 && (slot % 4) != 0;
        };
        return range.getSlots(container).stream().filter(filter).collect(Collectors.toList());
    }


    @Nonnull
    @Override
    public List<Slot> getInventorySlots(@Nonnull ContainerSteelAnvil container) {
        return container.getSlotRange(GuiUtils.SLOTS_FULL_PLAYER_INVENTORY).getSlots(container);
    }
}
