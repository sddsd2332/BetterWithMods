package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.inCenter;
import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.isMax;

public enum SidingOrientation implements IOrientation {
    UP("up", EnumFacing.UP, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D)),
    DOWN("down", EnumFacing.DOWN, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
    NORTH("north", EnumFacing.NORTH, new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D)),
    SOUTH("south", EnumFacing.SOUTH, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D)),
    WEST("west", EnumFacing.WEST, new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)),
    EAST("east", EnumFacing.EAST, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D));

    public static final SidingOrientation[] VALUES = values();

    private final String name;
    private final EnumFacing facing;
    private final AxisAlignedBB bounds;

    SidingOrientation(String name, EnumFacing facing, AxisAlignedBB bounds) {
        this.name = name;
        this.facing = facing;
        this.bounds = bounds;
    }

    public static SidingOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return SidingOrientation.VALUES[facing.getIndex()];
        return SidingOrientation.UP;
    }

    public static SidingOrientation getFromVec(Vec3d hit, EnumFacing facing) {
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
    public BlockFaceShape getFaceShape(EnumFacing facing) {
        return facing == getFacing() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
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
    public IOrientation next() {
        return VALUES[(this.ordinal() + 1) % (VALUES.length)];
    }

    public static IOrientationPlacer<SidingOrientation> PLACER = (placer, face, stack, hit) -> getFromVec(hit, face);


    public IOrientation getDefault() {
        return UP;
    }

}

