package betterwithmods.common.items;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockPlanter;
import betterwithmods.common.blocks.BlockPlanter.Type;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

public class ItemFertilizer extends Item {
    public ItemFertilizer() {
        super();

    }

    public static boolean processBlock(Block block, World world, BlockPos pos) {
        if (block == Blocks.FARMLAND) {
            world.setBlockState(pos, BWMBlocks.FERTILE_FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, world.getBlockState(pos).getValue(BlockFarmland.MOISTURE)));
            world.playEvent(2005, pos.up(), 0);
            return true;
        } else if (block == BlockPlanter.getBlock(Type.FARMLAND) && block.getMetaFromState(world.getBlockState(pos)) == 1) {
            world.setBlockState(pos, BlockPlanter.getBlock(Type.FERTILE).getDefaultState());
            world.playEvent(2005, pos.up(), 0);
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof IPlantable) {
            Block below = world.getBlockState(pos.down()).getBlock();
            if (processBlock(below, world, pos.down())) {
                if (!player.capabilities.isCreativeMode)
                    stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        } else if (processBlock(block, world, pos)) {
            if (!player.capabilities.isCreativeMode)
                stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
