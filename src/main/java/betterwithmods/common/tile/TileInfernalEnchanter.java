package betterwithmods.common.tile;

import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockInfernalEnchanter;
import betterwithmods.library.common.inventory.FilteredStackHandler;
import betterwithmods.library.common.inventory.SimpleStackHandler;
import betterwithmods.library.common.tile.TileBasicInventory;
import betterwithmods.module.internal.AdvancementRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by primetoxinz on 9/11/16.
 */
public class TileInfernalEnchanter extends TileBasicInventory implements ITickable {
    private final static int RADIUS = 8;
    private int bookcaseCount;
    private boolean active;

    private static float getPower(World world, BlockPos pos) {
        float power = ForgeHooks.getEnchantPower(world, pos);
        if (power > 0) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (!world.getBlockState(pos.offset(facing)).getMaterial().isSolid())
                    return power;
            }
        }
        return 0;
    }

    @Override
    public void update() {
        if (getWorld().getTotalWorldTime() % 20 == 0) {
            bookcaseCount = 0;
            for (int x = -RADIUS; x <= RADIUS; x++) {
                for (int y = -RADIUS; y <= RADIUS; y++) {
                    for (int z = -RADIUS; z <= RADIUS; z++) {
                        BlockPos current = pos.add(x, y, z);
                        float power = getPower(world, current);
                        if (power > 0) {
                            bookcaseCount += power;
                        }
                    }
                }
            }
        }

        if (getWorld().getTotalWorldTime() % 5 == 0) {
            List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).grow(8));
            boolean players = !playerList.isEmpty();
            if (active != players) {
                active = players;
                if (active)
                    world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.75f, 1);
                else {
                    world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.1f, 1);
                }
            }
            IBlockState state = world.getBlockState(pos);
            if (active) {
                world.setBlockState(pos, state.withProperty(BlockInfernalEnchanter.ACTIVE, true));
                int x = pos.getX(), y = pos.getY(), z = pos.getZ();
                getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .125, y + .9, z + .125, 0, 0, 0);
                getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .875, y + .9, z + .125, 0, 0, 0);
                getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .875, y + .9, z + .875, 0, 0, 0);
                getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .125, y + .9, z + .875, 0, 0, 0);

                playerList.stream().filter(p -> p instanceof EntityPlayerMP).map(p -> (EntityPlayerMP) p).forEach(player -> AdvancementRegistry.CONSTRUCT_LIBRARY.trigger(player, getBookcaseCount()));
            } else {
                world.setBlockState(pos, state.withProperty(BlockInfernalEnchanter.ACTIVE, false));

            }
        }
    }

    public int getBookcaseCount() {
        return bookcaseCount;
    }

    @Override
    public int getInventorySize() {
        return 2;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("bookcaseCount", bookcaseCount);
        compound.setBoolean("active", active);
        return super.writeToNBT(compound);

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        bookcaseCount = compound.getInteger("bookcaseCount");
        active = compound.getBoolean("active");
        super.readFromNBT(compound);
    }

    public String getName() {
        return "bwm.infernalenchanter";
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public SimpleStackHandler createItemStackHandler() {
        return new InfernalEnchanterHandler(this);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public ItemStack getActiveItem() {
        return this.inventory.getStackInSlot(1);
    }

    private class InfernalEnchanterHandler extends FilteredStackHandler {
        InfernalEnchanterHandler(TileEntity tile) {
            super(2, tile, Ingredient.fromItem(BWMItems.ARCANE_SCROLL));
        }
    }
}
