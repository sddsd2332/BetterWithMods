package betterwithmods.common.entity;

import betterwithmods.common.BWMItems;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.util.FluidUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityDynamite extends Entity implements IProjectile {
    private static final float pi = 3.141593F;

    private int fuse = -1;

    public EntityDynamite(World world) {
        this(world, 0, 0, 0);
    }

    public EntityDynamite(World world, double xPos, double yPos, double zPos) {
        super(world);
        this.setSize(0.25F, 0.4F);
        this.setPosition(xPos, yPos, zPos);
        this.fuse = 0;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
    }

    public EntityDynamite(World world, EntityLivingBase owner, boolean lit) {
        this(world);
        this.setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw, owner.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * pi) * 0.16F;
        this.posY -= 0.1D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * pi) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * pi) * MathHelper.cos(this.rotationPitch / 180.0F * pi) * 0.4F);
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * pi) * MathHelper.cos(this.rotationPitch / 180.0F * pi) * 0.4F);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * pi) * 0.4F);
        this.shoot(this.motionX, this.motionY, this.motionZ, 0.75F, 1.0F);
        this.fuse = lit ? 100 : 0;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if(id == 100) {
            this.fuse = 100;
        }
    }

    @Override
    public void onUpdate() {
        Fluid fluid = FluidUtils.getFluidFromBlock(world, getPosition(), EnumFacing.UP);
        if (fluid != null && fluid.getTemperature() >= FluidRegistry.LAVA.getTemperature()) {
            this.fuse = 1;
        }

        if (world.getBlockState(getPosition()).getBlock() == Blocks.FIRE) {
            this.fuse = 1;
            this.getEntityWorld().playSound(null, new BlockPos(this.posX, this.posY, this.posZ), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }

        if (this.fuse > 0) {
            if (!world.isRemote) {

                //Send up to the client
                world.setEntityState(this, (byte) 100);

                //Play sounds
                if (this.fuse % 20 == 0) {
                    world.playSound(null, getPosition(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }

            }

            //Spawn particles
            float smokeOffset = 0.25F;
            if (fluid != null && fluid == FluidRegistry.WATER) {
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * smokeOffset, this.posY - this.motionY * smokeOffset, this.posZ - this.motionZ * smokeOffset, this.motionX, this.motionY, this.motionZ);
            } else {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX - this.motionX * smokeOffset, this.posY - this.motionY * smokeOffset, this.posZ - this.motionZ * smokeOffset, this.motionX, this.motionY, this.motionZ);
            }

            this.fuse--;

            if (this.fuse <= 0) {
                if (!this.getEntityWorld().isRemote) {
                    explode();
                }
                this.setDead();
            }

        } else {
            if (onGround) {
                if (Math.abs(this.motionX) < 0.01D && Math.abs(this.motionY) < 0.01D && Math.abs(this.motionZ) < 0.01D) {
                    convertToItem();
                }
            }
        }


        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.04D;
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;

        if (this.onGround) {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
            this.motionY *= -0.5D;
        }

        this.extinguish();
    }

    @Override
    public void shoot(double dX, double dY,
                      double dZ, float angle, float f) {
        float sqrt = MathHelper.sqrt(dX * dX + dY * dY + dZ * dZ);
        dX /= sqrt;
        dY /= sqrt;
        dZ /= sqrt;
        dX += this.rand.nextGaussian() * 0.0075D * f;
        dY += this.rand.nextGaussian() * 0.0075D * f;
        dZ += this.rand.nextGaussian() * 0.0075D * f;
        dX *= angle;
        dY *= angle;
        dZ *= angle;
        this.motionX = dX;
        this.motionY = dY;
        this.motionZ = dZ;
        float pitch = MathHelper.sqrt(dX * dX + dZ * dZ);
        this.prevRotationYaw = (this.rotationYaw = (float) (Math.atan2(dX, dZ) * 180.0D / pi));
        this.prevRotationPitch = (this.rotationPitch = (float) (Math.atan2(dY, pitch) * 180.0D / pi));
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound tag) {
        if (tag.hasKey("Fuse"))
            fuse = tag.getInteger("Fuse");
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound tag) {
        if (fuse > 0) {
            tag.setInteger("Fuse", fuse);
        }
    }

    public void explode() {
        float intensity = 1.5F;
        world.createExplosion(null, this.posX, this.posY, this.posZ, intensity, true);
        redneckFishing(getPosition());
    }

    private void redneckFishing(BlockPos center) {
        if (isWaterBlock(center)) {
            for (BlockPos pos : BlockPos.getAllInBox(center.add(-4, -4, -4), center.add(4, 4, 4))) {
                if (isWaterBlock(pos)) {
                    if (this.rand.nextInt(20) == 0)
                        spawnDeadFish(pos);
                }
            }
        }
    }

    @Deprecated
    private boolean isWaterBlock(BlockPos pos) {
        Block block = this.getEntityWorld().getBlockState(pos).getBlock();
        return block instanceof BlockLiquid && this.getEntityWorld().getBlockState(pos).getMaterial() == Material.WATER;
    }

    private void spawnDeadFish(BlockPos pos) {
        LootContext.Builder build = new LootContext.Builder((WorldServer) world);
        List<ItemStack> fish = world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING_FISH).generateLootForPools(world.rand, build.build());
        for (ItemStack stack : fish) {
            InventoryUtils.spawnStack(world, pos, Lists.newArrayList(stack));
        }
    }

    private void convertToItem() {
        if (!world.isRemote)
            InventoryUtils.spawnStack(world, posX, posY, posZ, 20, new ItemStack(BWMItems.DYNAMITE));
        this.setDead();
    }

}
