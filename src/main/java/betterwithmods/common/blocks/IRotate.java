package betterwithmods.common.blocks;

import betterwithmods.util.player.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by primetoxinz on 6/24/17.
 */
public interface IRotate {

    default boolean rotates() {
        return false;
    }

    default void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (rotates())
            tooltip.add(I18n.format("tooltip.rotate_with_hand.name"));
    }

    default void nextState(World world, BlockPos pos, IBlockState state) {
    }

    default boolean onBlockActivated(Block block, World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!rotates())
            return false;

        if (PlayerHelper.areHandsEmpty(player) && player.isSneaking()) {
            world.playSound(null, pos, block.getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            nextState(world, pos, state);
            world.notifyNeighborsOfStateChange(pos, block, false);
            world.scheduleBlockUpdate(pos, block, 10, 5);
            return true;
        }
        return false;
    }

}
