package betterwithmods.module.general.moreheads.common;

import betterwithmods.module.general.moreheads.client.TEISRHead;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemHead extends ItemBlock {

    private Block head;

    public ItemHead(Block block) {
        super(block);
        this.head = block;
        this.setHasSubtypes(true);
        setTileEntityItemStackRenderer(new TEISRHead());
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.DOWN)
            return EnumActionResult.FAIL;


        if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
            facing = EnumFacing.UP;
            pos = pos.down();
        }
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        boolean replaceable = block.isReplaceable(worldIn, pos);

        if (!replaceable) {
            if (!worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.isSideSolid(pos, facing, true)) {
                return EnumActionResult.FAIL;
            }
            pos = pos.offset(facing);
        }


        ItemStack itemstack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, itemstack) && head.canPlaceBlockAt(worldIn, pos)) {
            if (worldIn.isRemote) {
                return EnumActionResult.SUCCESS;
            } else {
                worldIn.setBlockState(pos, head.getDefaultState(), 11);


                if (itemstack.hasTagCompound()) {
                    NBTTagCompound tag = itemstack.getTagCompound();
                    if (tag != null) {
                        TileEntity tile = worldIn.getTileEntity(pos);
                        if (tile instanceof TileHead) {
                            HeadType type = HeadType.VALUES[tag.getInteger("type")];
                            ((TileHead) tile).setType(type);
                            tile.markDirty();
                        }

                    }
                }

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}
