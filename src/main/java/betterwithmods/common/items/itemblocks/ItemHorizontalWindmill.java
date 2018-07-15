package betterwithmods.common.items.itemblocks;

import betterwithmods.client.ClientEventHandler;
import betterwithmods.client.tesr.TESRWindmill;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemHorizontalWindmill extends ItemAxleBase {

    public ItemHorizontalWindmill(Block block) {
        super(block);
        this.radius = 6;
    }

    @Override
    public boolean isAxis(EnumFacing.Axis axis) {
        return axis.isHorizontal();
    }

    @Override
    public String tooltip() {
        return I18n.format("bwm.tooltip.horizontal_windmill.name");
    }


    @Override
    public void render(World world, Block block, BlockPos pos, ItemStack stack, EntityPlayer player, EnumFacing side, RayTraceResult target, double partial) {

        double[] deltas = ClientEventHandler.getPlayerPositionDelta(player, partial);
        double dx = deltas[0];
        double dy = deltas[1];
        double dz = deltas[2];

        GlStateManager.disableBlend();
        TESRWindmill.renderWindmill(side.getAxis() == EnumFacing.Axis.X ? 90 : 180, 0, pos.getX() - dx, pos.getY() - dy, pos.getZ() - dz, new int[]{0, 0, 0, 0});
        GlStateManager.enableBlend();
    }
}
