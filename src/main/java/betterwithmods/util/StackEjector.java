package betterwithmods.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

public class StackEjector {

    private final World world;
    private final ItemStack stack;
    private Vec3d position, motion;
    private int pickupDelay;

    public StackEjector(World world, ItemStack stack, Vec3d position, Vec3d motion) {
        this.world = world;
        this.stack = stack;
        this.position = position;
        this.motion = motion;
    }

    public StackEjector(World world, ItemStack stack, Vec3d position) {
        this.world = world;
        this.stack = stack;
        this.position = position;
    }

    public void ejectStack() {
        if (world.isRemote)
            return;
        if (position == null)
            return;
        EntityItem item = new EntityItem(world, position.x, position.y, position.z, stack);
        item.setPickupDelay(pickupDelay);
        if (motion != null) {
            item.motionX = motion.x;
            item.motionY = motion.y;
            item.motionZ = motion.z;
        }
        world.spawnEntity(item);
    }

    public void setPickupDelay(int pickupDelay) {
        this.pickupDelay = pickupDelay;
    }


}
