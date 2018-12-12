package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.*;

public enum MouldingOrientation implements IOrientation {


    NORTH_UP("north_up", new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D)),
    SOUTH_UP("south_up", new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D)),
    WEST_UP("west_up", new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D)),
    EAST_UP("east_up", new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D)),
    NORTH_DOWN("north_down", new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D)),
    SOUTH_DOWN("south_down", new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
    WEST_DOWN("west_down", new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D)),
    EAST_DOWN("east_down", new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D)),
    SOUTH_WEST("south_west", new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D)),
    NORTH_WEST("north_west", new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D)),
    NORTH_EAST("north_east", new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D)),
    SOUTH_EAST("south_east", new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D));

    public static final MouldingOrientation[] VALUES = values();


    private final String name;
    private final AxisAlignedBB bounds;

    MouldingOrientation(String name, AxisAlignedBB bounds) {
        this.name = name;
        this.bounds = bounds;
    }



           /*
    0 west down
    1 north down
    2 east down
    3 south down
    4 west up
    5 north up
    6 east up
    7 south up
    8 north west
    9 north east
    10 south west
    11 south east
     */

    @SuppressWarnings("Duplicates")
    public static MouldingOrientation getFromVec(Vec3d hit, EnumFacing facing) {
        double hitXFromCenter = hit.x - CENTER_OFFSET;
        double hitYFromCenter = hit.y - CENTER_OFFSET;
        double hitZFromCenter = hit.z - CENTER_OFFSET;

        switch (facing.getAxis()) {
            case Y:
                int corner = getCorner(hitXFromCenter, hitZFromCenter);
                if (corner != -1) {
                    MouldingOrientation[] corners = new MouldingOrientation[]{SOUTH_EAST, NORTH_EAST, NORTH_WEST, SOUTH_WEST};
                    return corners[corner];
                } else if (hitYFromCenter > 0) {
                    if (isMax(hitXFromCenter, hitZFromCenter)) {
                        return hitXFromCenter > 0 ? SOUTH_DOWN : WEST_DOWN;
                    } else {
                        return hitZFromCenter > 0 ? EAST_DOWN : NORTH_DOWN;
                    }
                } else {
                    if (isMax(hitXFromCenter, hitZFromCenter)) {
                        return hitXFromCenter > 0 ? SOUTH_UP : WEST_UP;
                    } else {
                        return hitZFromCenter > 0 ? EAST_UP : NORTH_UP;
                    }
                }
            case X:
                corner = getCorner(hitYFromCenter, hitZFromCenter);
                if (corner != -1) {
                    MouldingOrientation[] corners = new MouldingOrientation[]{EAST_UP, NORTH_UP, NORTH_DOWN, EAST_DOWN};
                    return corners[corner];
                } else if (hitXFromCenter > 0) {
                    if (isMax(hitYFromCenter, hitZFromCenter)) {
                        return hitYFromCenter > 0 ? WEST_UP : WEST_DOWN;
                    } else {
                        return hitYFromCenter > 0 ? SOUTH_WEST : NORTH_WEST;
                    }
                } else {
                    if (isMax(hitYFromCenter, hitZFromCenter)) {
                        return hitYFromCenter > 0 ? SOUTH_UP : SOUTH_DOWN;
                    } else {
                        return hitZFromCenter > 0 ? SOUTH_EAST : NORTH_EAST;
                    }
                }
            case Z:
                corner = getCorner(hitYFromCenter, hitXFromCenter);
                if (corner != -1) {
                    MouldingOrientation[] corners = new MouldingOrientation[]{SOUTH_UP, WEST_UP, WEST_DOWN, SOUTH_DOWN};
                    return corners[corner];
                } else if (hitZFromCenter > 0) {
                    if (isMax(hitXFromCenter, hitYFromCenter)) {
                        return hitXFromCenter > 0 ? NORTH_EAST : NORTH_WEST;
                    } else {
                        return hitYFromCenter > 0 ? NORTH_UP : NORTH_DOWN;
                    }
                } else {
                    if (isMax(hitXFromCenter, hitYFromCenter)) {
                        return hitXFromCenter > 0 ? SOUTH_EAST : SOUTH_WEST;
                    } else {
                        return hitXFromCenter > 0 ? EAST_UP : EAST_DOWN;
                    }
                }
            default:
                return MouldingOrientation.NORTH_DOWN;
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

    public static IOrientationPlacer<MouldingOrientation> PLACER = (placer, face, stack, hit) -> getFromVec(hit, face);
}

