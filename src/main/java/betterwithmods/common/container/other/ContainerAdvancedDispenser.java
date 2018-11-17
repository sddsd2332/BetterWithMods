package betterwithmods.common.container.other;

import betterwithmods.client.gui.GuiBlockDispenser;
import betterwithmods.common.tile.TileAdvancedDispenser;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.ContainerBase;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.common.container.property.IntProperty;
import betterwithmods.library.utils.GuiUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ContainerAdvancedDispenser extends ContainerTile<TileAdvancedDispenser> {

    public static final ResourceLocation SLOT = new ResourceLocation(ModLib.MODID, "selected_slot");


    public ContainerAdvancedDispenser(TileAdvancedDispenser tile, EntityPlayer player) {
        super(tile, player);
        addIntProperty(SLOT, tile::getNextIndex);
        addProperty(SLOT, new SlotProperty(this, tile::getNextIndex));
        GuiUtils.createPlayerSlots(player, this, 8, 102, 8, 160);
        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY, this, tile.inventory, 16, 4, 0, 53, 17);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiContainer createGui() {
        return new GuiBlockDispenser(this);
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int x, int dragType, ClickType type, EntityPlayer player) {
        if (x < 16) {
            setPropertyValue(SLOT, 0);
        }
        return super.slotClick(x, dragType, type, player);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, getTile().getNextIndex());
    }

    private static class SlotProperty extends IntProperty {

        SlotProperty(ContainerBase container, Supplier<Integer> supplier) {
            super(container, supplier);
        }

        @Override
        public void updateValue(Integer value) {
            super.updateValue(value);
            if (container instanceof ContainerAdvancedDispenser) {
                ((ContainerAdvancedDispenser) container).getTile().setNextIndex(this.getValue());
            }
        }
    }

}
