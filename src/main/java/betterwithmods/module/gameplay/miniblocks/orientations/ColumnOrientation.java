package betterwithmods.module.gameplay.miniblocks.orientations;

import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public enum ColumnOrientation implements BaseOrientation {
    Y("y", EnumFacing.DOWN, 0, 0, new AxisAlignedBB(2 / 16d, 0.0D, 2 / 16d, 14 / 16d, 1, 14 / 16d)),
    X("x", EnumFacing.WEST, 270, 90, new AxisAlignedBB(0, 2 / 16d, 2 / 16d, 1, 14 / 16d, 14 / 16d)),
    Z("z", EnumFacing.NORTH, 90, 0, new AxisAlignedBB(2 / 16d, 2 / 16d, 0, 14 / 16d, 14 / 16d, 1));

    public static final ColumnOrientation[] VALUES = values();

    private final String name;
    private final EnumFacing facing;
    private final AxisAlignedBB bounds;
    private final int x;
    private final int y;

    ColumnOrientation(String name, EnumFacing facing, int x, int y, AxisAlignedBB bounds) {
        this.name = name;
        this.facing = facing;
        this.x = x;
        this.y = y;
        this.bounds = bounds;
    }


    @SuppressWarnings("Duplicates")
    public static BaseOrientation getFromVec(Vec3d hit, EnumFacing facing) {
        if (facing != null) {
            switch (facing.getAxis()) {
                case X:
                    return X;
                case Y:
                    return Y;
                case Z:
                    return Z;
            }
        }
        return BaseOrientation.DEFAULT;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public AxisAlignedBB getBounds() {
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TRSRTransformation toTransformation() {
        return TRSRTransformation.from(ModelRotation.getModelRotation(x, y));
    }

    @Override
    public BaseOrientation next() {
        return VALUES[(this.ordinal() + 1) % (VALUES.length)];
    }
}

