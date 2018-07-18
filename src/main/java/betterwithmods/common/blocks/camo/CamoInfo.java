package betterwithmods.common.blocks.camo;

import betterwithmods.client.baking.IRenderComparable;
import betterwithmods.common.tile.TileCamo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public class CamoInfo implements IRenderComparable<CamoInfo> {
    public final IBlockState state;

    public CamoInfo(IBlockState state) {
        this.state = state;
    }

    public CamoInfo(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("texture")) {
            this.state = NBTUtil.readBlockState(tag.getCompoundTag("texture"));
        } else {
            this.state = Blocks.AIR.getDefaultState();
        }
    }

    public CamoInfo(TileCamo tile) {
        this.state = tile.getState();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CamoInfo cacheInfo = (CamoInfo) o;
        return !state.equals(cacheInfo.state);
    }

    @Override
    public boolean renderEquals(CamoInfo other) {
        return equals(other);
    }

    @Override
    public int renderHashCode() {
        return state.hashCode();
    }


    public IBlockState getState() {
        if (state == null)
            return Blocks.AIR.getDefaultState();
        return state;
    }
}