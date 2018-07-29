package betterwithmods.common.items.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemWaterwheel extends ItemAxleBase {

    public ItemWaterwheel(Block block) {
        super(block);
        this.radius = 2;
    }

    @Override
    public boolean isAxis(EnumFacing.Axis axis) {
        return axis.isHorizontal();
    }

    @Override
    public void render(World world, Block block, BlockPos pos, ItemStack stack, EntityPlayer player, EnumFacing side, RayTraceResult target, double partial) {

    }
}
