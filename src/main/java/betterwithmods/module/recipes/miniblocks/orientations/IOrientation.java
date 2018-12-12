package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public interface IOrientation extends IStringSerializable {
    IOrientation DEFAULT = new IOrientation() {
        @Override
        public String getName() {
            return "default";
        }

        @Override
        public AxisAlignedBB getBounds() {
            return Block.FULL_BLOCK_AABB;
        }
    };

    default int ordinal() {
        return 0;
    }

    default AxisAlignedBB getBounds() {
        return Block.FULL_BLOCK_AABB;
    }

    default IOrientation next() {
        return DEFAULT;
    }

    default BlockFaceShape getFaceShape(EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

}
