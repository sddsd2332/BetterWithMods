package betterwithmods.common.items.itemblocks;

import betterwithmods.common.blocks.BlockLamp;
import betterwithmods.common.tile.TileLamp;
import betterwithmods.library.utils.colors.ColorUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockLamp extends ItemBlock {
    public ItemBlockLamp(Block block) {
        super(block);
    }

    private void setLampNBT(World world, BlockPos pos, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileLamp) {
            ((TileLamp) tile).fromItemStack(stack);
        }
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
        if (!world.setBlockState(pos, newState, 11)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            setTileEntityNBT(world, player, pos, stack);
            setLampNBT(world, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
        }

        return true;
    }


    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        int color = BlockLamp.getStackColor(stack);
        ITextComponent colorComponent = new TextComponentTranslation("betterwithmods:hex_color.name", Integer.toHexString(color));

        EnumDyeColor dye = ColorUtils.fromColor(color);
        if(dye != null) {
            colorComponent = new TextComponentTranslation(dye.getTranslationKey() + ".name");
        }
        ITextComponent base = new TextComponentTranslation(this.getTranslationKey(stack) + ".name");
        return colorComponent.appendText(" ").appendSibling(base).getFormattedText();
    }



}
