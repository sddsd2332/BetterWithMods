package betterwithmods.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

import javax.annotation.Nonnull;

public class FakeCrafter extends InventoryCrafting {
    public FakeCrafter(int x, int y) {
        super(fakeContainer.instance, x, y);
    }

    private static class fakeContainer extends Container {
        public static final fakeContainer instance = new fakeContainer();

        @Override
        public boolean canInteractWith(@Nonnull EntityPlayer player) {
            return false;
        }
    }
}
