package betterwithmods.module.gameplay.miniblocks.client;

import betterwithmods.client.baking.IRenderComparable;
import betterwithmods.common.blocks.camo.CamoInfo;
import betterwithmods.module.gameplay.miniblocks.ItemMini;
import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
import betterwithmods.module.gameplay.miniblocks.tiles.TileMini;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class MiniInfo implements IRenderComparable<MiniInfo> {

    public final CamoInfo camo;
    public final BaseOrientation orientation;

    public MiniInfo(IBlockState state, BaseOrientation orientation) {
        this.camo = new CamoInfo(state);
        this.orientation = orientation;
    }

    public MiniInfo(ItemStack stack) {
        this.camo = new CamoInfo(stack);
        this.orientation = ((ItemMini) stack.getItem()).getDefaultOrientation(stack);
    }

    public MiniInfo(TileMini tile) {
        this.camo = new CamoInfo(tile);
        this.orientation = tile.orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniInfo cacheInfo = (MiniInfo) o;
        return super.equals(o) && orientation == cacheInfo.orientation;
    }

    @Override
    public boolean renderEquals(MiniInfo other) {
        return camo.renderEquals(other.camo) && equals(other);
    }

    @Override
    public int renderHashCode() {
        return 31 * camo.renderHashCode() + orientation.hashCode();
    }


    public BaseOrientation getOrientation() {
        if (orientation == null)
            return BaseOrientation.DEFAULT;
        return orientation;
    }

    public IBlockState getState() {
        return camo.getState();
    }
}
