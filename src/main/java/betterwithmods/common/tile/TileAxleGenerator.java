package betterwithmods.common.tile;

import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.mechanical.BlockAxleGenerator;
import betterwithmods.library.common.block.IBlockActive;
import betterwithmods.library.common.tile.TileBasic;
import betterwithmods.module.internal.SoundRegistry;
import betterwithmods.library.utils.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class TileAxleGenerator extends TileBasic implements ITickable, IMechanicalPower {

    public static int generatorRenderDistance;

    protected final float runningSpeed = 0.4F;
    public float currentRotation = 0.0F;
    public float previousRotation = 0.0F;
    public float waterMod = 1;
    //Every generator will take up a single block with no extended bounding box
    protected byte power = 0;
    protected boolean isValid;

    protected int tick;

    public abstract void calculatePower();

    public abstract void verifyIntegrity();

    public abstract int getRadius();

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("CurrentRotation"))
            currentRotation = tag.getFloat("CurrentRotation");
        if (tag.hasKey("RotationSpeed"))
            previousRotation = tag.getFloat("RotationSpeed");
        if (tag.hasKey("power"))
            power = tag.getByte("power");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setByte("power", power);
        t.setFloat("CurrentRotation", currentRotation);
        t.setFloat("RotationSpeed", previousRotation);
        return t;
    }

    public float getCurrentRotation() {
        return this.currentRotation;
    }

    public float getPrevRotation() {
        return this.previousRotation;
    }

    @Override
    public void update() {
        tick++;
        if (tick % 20 == 0 && getBlockWorld().getBlockState(pos).getBlock() instanceof BlockAxleGenerator) {
            verifyIntegrity();
            calculatePower();
            tick = 0;
        }

        if (isValid()) {
            if (power != 0) {
                this.previousRotation = this.power * runningSpeed * waterMod;
                this.currentRotation += (this.power * this.power) * runningSpeed * waterMod;
                this.currentRotation %= 360;
                if (this.getBlockWorld().rand.nextInt(100) == 0)
                    this.getBlockWorld().playSound(null, pos, SoundRegistry.BLOCK_WOOD_CREAK, SoundCategory.BLOCKS, 0.5F, getBlockWorld().rand.nextFloat() * 0.25F + 0.25F);
            } else {
                previousRotation = 0;
                currentRotation = 0;
            }
        }
    }

    public void setPower(byte power) {
        this.power = power;
        world.setBlockState(pos, world.getBlockState(pos).withProperty(IBlockActive.ACTIVE, power > 0));
    }

    public boolean isValid() {
        return isValid;
    }


    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }


    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return generatorRenderDistance * generatorRenderDistance;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityMechanicalPower.MECHANICAL_POWER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER) {
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return power;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 0;
    }

    public EnumFacing getOrientation() {
        if (world.getBlockState(pos).getBlock() instanceof BlockAxleGenerator) {
            EnumFacing.Axis axis = world.getBlockState(pos).getValue(DirUtils.AXIS);
            switch (axis) {
                case X:
                    return EnumFacing.EAST;
                case Z:
                    return EnumFacing.SOUTH;
                default:
                    return EnumFacing.UP;
            }
        }
        return EnumFacing.UP;
    }

    @Override
    public World getBlockWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getBlockPos() {
        return super.getPos();
    }

    @Override
    public Block getBlock() {
        return getBlockType();
    }


    //Extend the bounding box if the TESR is bigger than the occupying block.
    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        IBlockState state = getBlockWorld().getBlockState(pos);
        AxisAlignedBB box = Block.FULL_BLOCK_AABB.offset(pos);
        if (!(state.getBlock() instanceof BlockAxleGenerator))
            return box;

        EnumFacing facing = getOrientation();
        EnumFacing.Axis axis = facing.getAxis();
        if (axis == EnumFacing.Axis.Z) {
            return box.grow(getRadius(), getRadius(), 0);
        } else if (axis == EnumFacing.Axis.X) {
            return box.grow(0, getRadius(), getRadius());
        } else {
            return box.grow(getRadius());
        }
    }
}
