package betterwithmods.module.gameplay.miniblocks.orientations;

import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static betterwithmods.module.gameplay.miniblocks.orientations.OrientationUtils.inCenter;
import static betterwithmods.module.gameplay.miniblocks.orientations.OrientationUtils.isMax;

public enum ColumnOrientation implements BaseOrientation {
    DOWN("down", EnumFacing.DOWN, 0, 0, new AxisAlignedBB(2 / 16d, 0.0D, 2 / 16d, 14 / 16d, 1, 14 / 16d));

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

    public static BaseOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return ColumnOrientation.VALUES[facing.getIndex()];
        return BaseOrientation.DEFAULT;
    }

    public static BaseOrientation getFromVec(Vec3d hit, EnumFacing facing) {
        if (true)
            return DOWN;

        float hitXFromCenter = (float) (hit.x - 0.5F);
        float hitYFromCenter = (float) (hit.y - 0.5F);
        float hitZFromCenter = (float) (hit.z - 0.5F);
        switch (facing.getAxis()) {
            case Y:
                if (inCenter(hitXFromCenter, hitZFromCenter, 0.25f)) {
                    return fromFace(facing);
                } else if (isMax(hitXFromCenter, hitZFromCenter)) {
                    return hitXFromCenter < 0 ? fromFace(EnumFacing.EAST) : fromFace(EnumFacing.WEST);
                } else {
                    return hitZFromCenter < 0 ? fromFace(EnumFacing.SOUTH) : fromFace(EnumFacing.NORTH);
                }
            case X:
                if (inCenter(hitYFromCenter, hitZFromCenter, 0.25f)) {
                    return fromFace(facing);
                } else if (isMax(hitYFromCenter, hitZFromCenter)) {
                    return hitYFromCenter < 0 ? fromFace(EnumFacing.UP) : fromFace(EnumFacing.DOWN);

                } else {
                    return hitZFromCenter < 0 ? fromFace(EnumFacing.SOUTH) : fromFace(EnumFacing.NORTH);
                }
            case Z:
                if (inCenter(hitYFromCenter, hitXFromCenter, 0.25f)) {
                    return fromFace(facing);
                } else if (isMax(hitYFromCenter, hitXFromCenter)) {
                    return hitYFromCenter < 0 ? fromFace(EnumFacing.UP) : fromFace(EnumFacing.DOWN);
                } else {
                    return hitXFromCenter < 0 ? fromFace(EnumFacing.EAST) : fromFace(EnumFacing.WEST);
                }
            default:
                return fromFace(facing);
        }
    }

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

