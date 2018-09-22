package betterwithmods.module.general;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.ConfigHelper;
import betterwithmods.module.RequiredFeature;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class MechanicalPower extends RequiredFeature {

    public static float HAND_CRANK_EXHAUSTION;

    public static Object2BooleanMap<ResourceLocation> OVER_POWER = new Object2BooleanOpenHashMap<>();

    public static boolean doesMachineOverpower(ResourceLocation name) {
        return OVER_POWER.getOrDefault(name, true);
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        HAND_CRANK_EXHAUSTION = loadProperty("Hand Crank Exhaustion", 6.0f)
                .setMin(0.0).setMax(20.0)
                .setComment("How much saturation turning the crank eats. Set to 0.0 to disable.")
                .get();

        setMachineOverpower(BWMBlocks.WOODEN_AXLE.getRegistryName());
        setMachineOverpower(BWMBlocks.WOODEN_GEARBOX.getRegistryName());
        setMachineOverpower(BWMBlocks.MILLSTONE.getRegistryName());
        setMachineOverpower(BWMBlocks.SAW.getRegistryName());
        setMachineOverpower(BWMBlocks.FILTERED_HOPPER.getRegistryName());
        setMachineOverpower(BWMBlocks.TURNTABLE.getRegistryName());
        setMachineOverpower(BWMBlocks.PULLEY.getRegistryName());
        setMachineOverpower(BWMBlocks.HAND_CRANK.getRegistryName());
        setMachineOverpower(BWMBlocks.BELLOWS.getRegistryName());


        config().setCategoryComment(ConfigHelper.joinCategory(getCategory(), "overpower"), "Set whether a machine will break when it receives too much mechanical power (Such as from a Windmill in the rain)");
    }

    public void setMachineOverpower(ResourceLocation name) {
        OVER_POWER.put(name, loadProperty(name.toString(), true).subCategory("overpower").get());
    }

    @Override
    public String getDescription() {
        return "Configurations for Mechanical Power";
    }

}
