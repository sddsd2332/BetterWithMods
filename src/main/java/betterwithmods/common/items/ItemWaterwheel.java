package betterwithmods.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
    public String tooltip() {
        return I18n.format("bwm.tooltip.waterwheel.name");
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
    }
}
