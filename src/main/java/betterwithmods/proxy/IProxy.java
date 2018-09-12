package betterwithmods.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
    default void onPreInit(FMLPreInitializationEvent event) {

    }

    default void onInit(FMLInitializationEvent event) {
    }

    default void postInit(FMLPostInitializationEvent event) {
    }

    default void addResourceOverride(String space, String dir, String file, String ext) {
    }

    default void addResourceOverride(String space, String domain, String dir, String file, String ext) {
    }

    default void syncHarness(int entityId, ItemStack harness) {
    }

    default void rotateEntity(int entityId, float yaw, float pitch) {
    }


    default boolean addRunningParticles(IBlockState state, World world, BlockPos pos, Entity entity) {
        return false;
    }

}
