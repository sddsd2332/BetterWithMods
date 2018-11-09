package betterwithmods.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntitySitMount extends Entity {

    private final double offset;

    public EntitySitMount(World worldIn) {
        this(worldIn, 0);
    }

    public EntitySitMount(World worldIn, double offset) {
        super(worldIn);
        this.offset = offset;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public double getMountedYOffset() {
        return offset;
    }

    @Override
    protected void entityInit() {
        setNoGravity(true);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {}

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) { }

    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        setDead();
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        setDead();
    }

}
