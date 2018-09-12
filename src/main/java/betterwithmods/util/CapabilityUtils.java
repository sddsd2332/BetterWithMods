package betterwithmods.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CapabilityUtils {

    public static <T> Optional<T> getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, EnumFacing facing) {
        if (provider != null)
            return Optional.ofNullable(provider.getCapability(capability, facing));
        return Optional.empty();
    }

    public static Optional<IItemHandler> getInventory(@Nonnull ICapabilityProvider provider, EnumFacing facing) {
        return getCapability(provider, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
    }

    @Nonnull
    public static IItemHandler getEntityInventory(@Nonnull EntityLivingBase entity) {
        return getInventory(entity, null).get();
    }
}
