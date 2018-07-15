package betterwithmods.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Random;

public interface IProxy {
    default void preInit(FMLPreInitializationEvent event) {

    }

    default void init(FMLInitializationEvent event) {
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

    default void spawnBlockDustClient(World world, BlockPos pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed, EnumFacing up) {
    }

    default boolean addRunningParticles(IBlockState state, World world, BlockPos pos, Entity entity) {
        return false;
    }

    default void syncGloom(String entityId, int gloom) {
    }

    default void syncPlaced(BlockPos[] pos) {
    }
}
