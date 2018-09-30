package betterwithmods.common.tile;

import betterwithmods.api.BWMAPI;
import betterwithmods.api.block.ISoulContainer;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IHopperFilter;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.api.util.IProgressSource;
import betterwithmods.client.model.filters.ModelWithResource;
import betterwithmods.common.advancements.BWAdvancements;
import betterwithmods.common.blocks.mechanical.mech_machine.BlockMechMachine;
import betterwithmods.common.registry.hopper.filters.HopperFilter;
import betterwithmods.common.registry.hopper.recipes.HopperRecipe;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TileFilteredHopper extends TileVisibleInventory implements IMechanicalPower, IProgressSource {

    public final SimpleStackHandler filter;
    private final int STACK_SIZE = 8;
    private final int maxExperienceCount = 1000;
    public IHopperFilter hopperFilter = HopperFilter.NONE;
    public int soulsRetained;
    public byte power;
    private int ejectCounter, ejectXPCounter;
    private int experienceCount;
    private ISoulContainer prevContainer;

    public TileFilteredHopper() {
        this.ejectCounter = 0;
        this.experienceCount = 0;
        this.ejectXPCounter = 10;
        this.soulsRetained = 0;
        this.occupiedSlots = 0;
        this.hasCapability = facing -> facing == EnumFacing.DOWN || facing == EnumFacing.UP;
        this.filter = new FilterHandler(1, this);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("EjectCounter"))
            this.ejectCounter = tag.getInteger("EjectCounter");
        if (tag.hasKey("XPCount"))
            this.experienceCount = tag.getInteger("XPCount");
        if (tag.hasKey("Souls"))
            this.soulsRetained = tag.getInteger("Souls");
        this.power = tag.getByte("power");
        if (tag.hasKey("Item"))
            this.filter.setStackInSlot(0, new ItemStack(tag.getCompoundTag("Item")));
        validateInventory();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        t.setInteger("EjectCounter", this.ejectCounter);
        t.setInteger("XPCount", this.experienceCount);
        t.setInteger("Souls", this.soulsRetained);
        t.setByte("power", power);
        if (!filter.getStackInSlot(0).isEmpty()) {
            NBTTagCompound itemTag = new NBTTagCompound();
            filter.getStackInSlot(0).writeToNBT(itemTag);
            t.setTag("Item", itemTag);
        }
        return t;
    }

    public boolean isPowered() {
        return power > 0;
    }

    public boolean isXPFull() {
        return experienceCount >= maxExperienceCount;
    }

    public void insert(Entity entity) {
        if (!InventoryUtils.isFull(inventory) && entity instanceof EntityItem) {
            EntityItem item = (EntityItem) entity;
            if (item.isDead)
                return;
            HopperRecipe recipe = RecipeRegistry.FILTERED_HOPPER.findRecipe(this, item.getItem()).orElse(null);
            if (recipe != null) {
                if (recipe.craftRecipe(item, world, pos, this)) {
                    this.getBlockWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getBlockWorld().rand.nextFloat() - getBlockWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            } else if (canFilterProcessItem(item.getItem())) {
                if (InventoryUtils.insertFromWorld(inventory, item, 0, 18, false))
                    this.getBlockWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getBlockWorld().rand.nextFloat() - getBlockWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                hopperFilter.onInsert(world, pos, this, entity);
            }
        }
    }

    private void extract() {
        Optional<IItemHandler> inv = InventoryUtils.getItemHandler(world, pos.down(), EnumFacing.UP);
        if (ejectCounter > 2) {
            int slot = InventoryUtils.getRandomOccupiedStackInRange(inventory, 0, 17);
            if (slot != -1) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (inv.isPresent()) {
                    if (InventoryUtils.canInsert(inv.get(), stack, STACK_SIZE)) {
                        ItemStack insert = InventoryUtils.insert(inv.get(), stack, STACK_SIZE, false);
                        stack.shrink(STACK_SIZE - insert.getCount());
                    }
                } else if (canDropIntoBlock(pos.down())) {
                    InventoryUtils.spawnStack(world, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, stack.splitStack(STACK_SIZE), 10);
                }
            }
            ejectCounter = 0;
        } else {
            ejectCounter++;
        }
        if (ejectXPCounter > 2) {
            if (canDropIntoBlock(pos.down())) {
                if (experienceCount > 19) {
                    experienceCount -= 20;
                    spawnEntityXPOrb(20);
                }
            }
            ejectXPCounter = 0;
        } else {
            ejectXPCounter++;
        }
    }

    private boolean canDropIntoBlock(BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isReplaceable();
    }

    @Override
    public void update() {
        if (!this.world.isRemote) {
            byte power = (byte) calculateInput();
            if (this.power != power) {
                this.power = power;
            }
            if (getBlock() != null)
                getBlock().setActive(world, pos, isActive());
            if (isPowered()) {
                extract();
            }
        }

    }

    public boolean isActive() {
        return power > 0;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return this.getBlockWorld().getTileEntity(this.pos) == this && player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.getBlockWorld() != null) {
            validateInventory();
        }
    }

    private void validateInventory() {
        boolean stateChanged = false;
        ItemStack filter = getFilterStack();
        IHopperFilter newFilter = RecipeRegistry.HOPPER_FILTERS.getFilter(filter);
        if (this.hopperFilter != newFilter) {
            this.hopperFilter = newFilter;
            stateChanged = true;
        }
        byte slotsOccupied = (byte) InventoryUtils.getOccupiedStacks(inventory, 0, 17);
        if (slotsOccupied != this.occupiedSlots) {
            this.occupiedSlots = slotsOccupied;
            stateChanged = true;
        }
        if (getBlockWorld() != null && stateChanged) {
            IBlockState state = getBlockWorld().getBlockState(pos);
            getBlockWorld().notifyBlockUpdate(pos, state, state, 3);
        }

    }

    public IHopperFilter getHopperFilter() {
        return hopperFilter;
    }

    private boolean canFilterProcessItem(ItemStack stack) {
        validateInventory();
        return hopperFilter.allow(stack);
    }

    private void spawnEntityXPOrb(int value) {
        double xOff = this.getBlockWorld().rand.nextDouble() * 0.1D + 0.45D;
        double yOff = -0.5D;
        double zOff = this.getBlockWorld().rand.nextDouble() * 0.1D + 0.45D;
        EntityXPOrb orb = new EntityXPOrb(this.getBlockWorld(), this.pos.getX() + xOff, this.pos.getY() + yOff, this.pos.getZ() + zOff, value);
        orb.motionX = 0.0D;
        orb.motionY = 0.0D;
        orb.motionZ = 0.0D;
        this.getBlockWorld().spawnEntity(orb);
    }

    @Nullable
    public ISoulContainer getSoulContainer() {
        Block block = world.getBlockState(pos.down()).getBlock();
        if (block instanceof ISoulContainer) {
            return (ISoulContainer) block;
        }
        return null;
    }

    public void decreaseSoulCount(int numSouls) {
        this.soulsRetained = Math.max(soulsRetained - numSouls, 0);
        markDirty();
    }

    public void increaseSoulCount(int numSouls) {
        this.soulsRetained += numSouls;
        ISoulContainer container = getSoulContainer();

        if (this.soulsRetained > 7 && !isPowered()) {
            overpower();
            return;
        }

        if (container != null && container.getMaxSouls() != 0) {
            if (prevContainer != container)
                soulsRetained = numSouls;
            if (soulsRetained >= container.getMaxSouls()) {
                soulsRetained -= container.getMaxSouls();
                container.onFull(world, pos.down());
            }
        }
        prevContainer = container;
        markDirty();
    }

    @Override
    public void overpower() {
        getBlock().overpower(getBlockWorld(), getBlockPos());
        if (this.soulsRetained > 7) {
            this.getBlockWorld().playSound(null, this.pos, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.BLOCKS, 1.0F, getBlockWorld().rand.nextFloat() * 0.1F + 0.8F);
            if (WorldUtils.spawnGhast(world, pos)) {
                BWAdvancements.triggerNearby(world, new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(10.0D, 5.0D, 10.0D), BWAdvancements.SPAWN_HOPPER_FRIEND);
            }
        }

    }

    @Override
    public int getInventorySize() {
        return 18;
    }

    @Override
    public SimpleStackHandler createItemStackHandler() {
        return new HopperHandler(getInventorySize(), this);
    }

    @Override
    public String getName() {
        return "inv.filtered_hopper.entityName";
    }

    @Override
    public int getMaxVisibleSlots() {
        return 18;
    }

    public ModelWithResource getModel() {
        if (getFilterStack().isEmpty())
            return null;
        return getHopperFilter().getModelOverride(getFilterStack());
    }

    public ItemStack getFilterStack() {
        return filter.getStackInSlot(0);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return -1;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        if (facing.getAxis().isHorizontal())
            return BWMAPI.IMPLEMENTATION.getPowerOutput(world, pos.offset(facing), facing.getOpposite());
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityMechanicalPower.MECHANICAL_POWER)
            return CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
        return super.getCapability(capability, facing);
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
    public BlockMechMachine getBlock() {
        if (getBlockType() instanceof BlockMechMachine)
            return (BlockMechMachine) getBlockType();
        return null;
    }

    @Override
    public void onBreak() {
        super.onBreak();
        InventoryUtils.ejectInventoryContents(world, pos, filter);
    }

    public int getExperienceCount() {
        return experienceCount;
    }

    public void setExperienceCount(int experienceCount) {
        this.experienceCount = experienceCount;
    }

    public int getMaxExperienceCount() {
        return maxExperienceCount;
    }

    @Override
    public int getMax() {
        return 1;
    }

    @Override
    public int getProgress() {
        return Math.min(power, 1);
    }

    private class FilterHandler extends SimpleStackHandler {

        public FilterHandler(int size, TileEntity tile) {
            super(size, tile);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!RecipeRegistry.HOPPER_FILTERS.isFilter(stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }
    }

    private class HopperHandler extends SimpleStackHandler {
        final TileFilteredHopper hopper;

        public HopperHandler(int size, TileFilteredHopper hopper) {
            super(size, hopper);
            this.hopper = hopper;
        }

        @Override
        public void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            world.markBlockRangeForRenderUpdate(pos, pos);
            getBlockWorld().notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!hopper.canFilterProcessItem(stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot == 18 ? 1 : super.getSlotLimit(slot);
        }
    }


}
