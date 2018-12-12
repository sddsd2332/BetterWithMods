package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.getCorner;

public enum CornerOrientation implements IOrientation {

    DOWN_NORTH("down_north", new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D)),
    DOWN_SOUTH("down_south", new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D)),
    DOWN_EAST("down_east", new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D)),
    DOWN_WEST("down_west", new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D)),
    UP_NORTH("up_north", new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D)),
    UP_SOUTH("up_south", new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D)),
    UP_EAST("up_east", new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D)),
    UP_WEST("up_west", new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D));

    public static final CornerOrientation[] VALUES = values();

    public static final IOrientationPlacer<CornerOrientation> PLACER = (placer, face, stack, hit) -> getFromVec(hit,face);

    private final String name;
    private final AxisAlignedBB bounds;

    CornerOrientation(String name, AxisAlignedBB bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    public static CornerOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return CornerOrientation.VALUES[facing.getIndex()];
        return CornerOrientation.DOWN_EAST;
    }

    public static CornerOrientation getFromVec(Vec3d hit, EnumFacing facing) {
        float hitXFromCenter = (float) (hit.x - 0.5F);
        float hitYFromCenter = (float) (hit.y - 0.5F);
        float hitZFromCenter = (float) (hit.z - 0.5F);
        switch (facing.getAxis()) {
            case Y:
                int corner = getCorner(hitXFromCenter, hitZFromCenter, 0);
                if (corner != -1) {
                    int[] corners = hitYFromCenter > 0 ? new int[]{2, 3, 1, 0} : new int[]{6, 7, 5, 4};
                    return CornerOrientation.VALUES[corners[corner]];
                }
            case X:
                corner = getCorner(hitYFromCenter, hitZFromCenter, 0);
                if (corner != -1) {
                    int[] corners = hitXFromCenter > 0 ? new int[]{4, 5, 1, 0} : new int[]{6, 7, 3, 2};
                    return CornerOrientation.VALUES[corners[corner]];
                }
            case Z:
                corner = getCorner(hitYFromCenter, hitXFromCenter, 0);
                if (corner != -1) {
                    int[] corners = hitZFromCenter > 0 ? new int[]{7, 5, 1, 3} : new int[]{6, 4, 0, 2};
                    return CornerOrientation.VALUES[corners[corner]];
                }
            default:
                return fromFace(facing.getOpposite());
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public AxisAlignedBB getBounds() {
        return bounds;
    }


    @Override
    public CornerOrientation next() {
        return VALUES[(this.ordinal() + 1) % (VALUES.length)];
    }
}

