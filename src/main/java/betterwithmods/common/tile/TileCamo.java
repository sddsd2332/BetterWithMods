package betterwithmods.common.tile;

import betterwithmods.common.blocks.camo.BlockCamo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCamo extends TileBasic {

    public IBlockState state;


    public TileCamo() {
    }

    public static ItemStack fromParentState(Block mini, IBlockState state, int count) {
        ItemStack stack = new ItemStack(mini, count);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound texture = new NBTTagCompound();
        NBTUtil.writeBlockState(texture, state);
        tag.setTag("texture", texture);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        if (state != null) {
            NBTTagCompound texture = new NBTTagCompound();
            NBTUtil.writeBlockState(texture, state);
            tag.setTag("texture", texture);
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("texture")) {
            try {
                state = NBTUtil.readBlockState(compound.getCompoundTag("texture"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        super.readFromNBT(compound);
    }

    @Override
    public void onPlacedBy(EntityLivingBase placer, @Nullable EnumFacing face, ItemStack stack, float hitX, float hitY, float hitZ) {
        loadFromStack(stack);
    }

    public void loadFromStack(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("texture");
        if (tag != null) {
            state = NBTUtil.readBlockState(tag);
        }
    }

    public void markBlockForRenderUpdate() {
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    public void markBlockForUpdate() {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    public IBlockState getState() {
        if (state == null)
            return Blocks.AIR.getDefaultState();
        return state;
    }

    public void setState(IBlockState state) {
        this.state = state;
    }

    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target, IBlockState state) {
        if (this.state != null && state.getBlock() instanceof BlockCamo) {
            return fromParentState(state.getBlock(), this.state, 1);
        }
        return ItemStack.EMPTY;
    }


}
