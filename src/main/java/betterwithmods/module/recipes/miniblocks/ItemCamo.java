package betterwithmods.module.recipes.miniblocks;

import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.common.tile.TileCamo;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemCamo extends ItemBlock {

    public ItemCamo(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    public static IBlockState getState(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemCamo) {
            NBTTagCompound tag = stack.getSubCompound("texture");
            if (tag != null) {
                return NBTUtil.readBlockState(tag);
            }
        }
        return null;
    }

    public static boolean matches(ItemStack a, ItemStack b) {
        if ((a == null || b == null))
            return false;
        if (!(a.getItem() instanceof ItemCamo && b.getItem() instanceof ItemCamo))
            return false;

        ItemCamo miniA = (ItemCamo) a.getItem(), miniB = (ItemCamo) b.getItem();
        if (miniA.getBlock() != miniB.getBlock())
            return false;
        IBlockState stateA = getState(a), stateB = getState(b);
        return stateA != null & stateB != null && stateA.equals(stateB);
    }

    public static boolean placeBlockAt(ItemCamo item, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 11)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == item.block) {
            setTileEntityNBT(world, player, pos, stack);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileCamo)
                setNBT((TileCamo) tile, world, stack);
            ((BlockCamo) item.block).onBlockPlacedBy(world, pos, state, player, stack, side, hitX, hitY, hitZ);
            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
        }
        return true;
    }

    public static void setNBT(TileCamo tileentity, World worldIn, ItemStack stackIn) {
        NBTTagCompound data = stackIn.getSubCompound("texture");

        if (data != null) {
            if (tileentity != null) {
                if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt()) {
                    return;
                }

                NBTTagCompound tileNBT = tileentity.writeToNBT(new NBTTagCompound());
                NBTTagCompound newNBT = tileNBT.copy();
                tileNBT.merge(data);
                if (!tileNBT.equals(newNBT)) {
                    tileentity.readFromNBT(tileNBT);
                    tileentity.markDirty();
                }
            }
        }
    }

    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
        return placeBlockAt(this, stack, player, world, pos, side, hitX, hitY, hitZ, newState);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound("texture");

        //TODO .name
        ITextComponent type = new TextComponentTranslation("betterwithmods.unknown_mini.name");
        ITextComponent base = new TextComponentTranslation(this.getTranslationKey(stack) + ".name");

        if (tag != null) {
            IBlockState state = NBTUtil.readBlockState(tag);
            ItemStack block = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
            if (block.getItem() instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) block.getItem();
                type = new TextComponentString(itemBlock.getItemStackDisplayName(block));
            }
        }

        return type.appendSibling(base).getFormattedText();
    }
}
