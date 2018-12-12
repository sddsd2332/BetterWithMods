package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public enum ColumnOrientation implements IOrientation {
    Y("y", EnumFacing.DOWN, new AxisAlignedBB(2 / 16d, 0.0D, 2 / 16d, 14 / 16d, 1, 14 / 16d)),
    X("x", EnumFacing.WEST, new AxisAlignedBB(0, 2 / 16d, 2 / 16d, 1, 14 / 16d, 14 / 16d)),
    Z("z", EnumFacing.NORTH, new AxisAlignedBB(2 / 16d, 2 / 16d, 0, 14 / 16d, 14 / 16d, 1));

    public static IOrientationPlacer<ColumnOrientation> PLACER = (placer, face, stack, hit) -> getFromVec(hit,face);

    public static final ColumnOrientation[] VALUES = values();

    private final String name;
    private final EnumFacing facing;
    private final AxisAlignedBB bounds;

    ColumnOrientation(String name, EnumFacing facing, AxisAlignedBB bounds) {
        this.name = name;
        this.facing = facing;
        this.bounds = bounds;
    }

    @SuppressWarnings("Duplicates")
    public static ColumnOrientation getFromVec(Vec3d hit, EnumFacing facing) {
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
        return ColumnOrientation.X;
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

    @Override
    public ColumnOrientation next() {
        return VALUES[(this.ordinal() + 1) % (VALUES.length)];
    }
}

