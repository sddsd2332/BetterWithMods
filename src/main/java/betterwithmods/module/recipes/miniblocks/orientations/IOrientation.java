package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public interface IOrientation<O extends IOrientation<O>> extends IStringSerializable {

    default int ordinal() {
        return 0;
    }

    default AxisAlignedBB getBounds() {
        return Block.FULL_BLOCK_AABB;
    }

    default O next() {
        O[] values = allValues();
        return values[(this.ordinal() + 1) % (values.length)];
    }

    default BlockFaceShape getFaceShape(EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    O[] allValues();

}
