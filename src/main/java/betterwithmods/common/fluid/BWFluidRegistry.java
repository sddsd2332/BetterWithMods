package betterwithmods.common.fluid;

import betterwithmods.lib.ModLib;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BWFluidRegistry {
    public static final Fluid MILK = new Fluid("milk", new ResourceLocation(ModLib.MODID, "blocks/milk_still"), new ResourceLocation(ModLib.MODID, "blocks/milk_flowing"));

    public static void registerFluids() {
        FluidRegistry.registerFluid(MILK);
    }
}
