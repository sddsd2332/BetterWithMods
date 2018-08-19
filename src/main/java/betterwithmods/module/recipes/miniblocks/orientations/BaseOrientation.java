package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface BaseOrientation extends IStringSerializable {
    BaseOrientation DEFAULT = new BaseOrientation() {
        @SideOnly(Side.CLIENT)
        @Override
        public TRSRTransformation toTransformation() {
            return TRSRTransformation.from(ModelRotation.X0_Y0);
        }

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

    @SideOnly(Side.CLIENT)
    TRSRTransformation toTransformation();

    default BaseOrientation next() {
        return DEFAULT;
    }

    default BlockFaceShape getFaceShape(EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

}
