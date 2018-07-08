package betterwithmods.module.gameplay.miniblocks.orientations;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static betterwithmods.module.gameplay.miniblocks.orientations.OrientationUtils.*;

public enum StairOrientation implements BaseOrientation {
    NORTH_UP("north_up", 180, 270, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D)),
    SOUTH_UP("south_up", 180, 0, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
    WEST_UP("west_up", 180, 180, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D)),
    EAST_UP("east_up", 180, 90, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D)),
    NORTH_DOWN("north_down", 0, 270, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D)),
    SOUTH_DOWN("south_down", 0, 0, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D)),
    WEST_DOWN("west_down", 0, 180, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D)),
    EAST_DOWN("east_down", 0, 90, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D)),
    SOUTH_WEST("south_west", 90, 90, new AxisAlignedBB(0, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D)),
    NORTH_WEST("north_west", 90, 180, new AxisAlignedBB(0, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D)),
    NORTH_EAST("north_east", 90, 270, new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D)),
    SOUTH_EAST("south_east", 90, 0, new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D));


    public static final StairOrientation[] VALUES = values();

    private final String name;
    private final AxisAlignedBB bounds;
    private final List<AxisAlignedBB> collison;
    private final int x;
    private final int y;

    StairOrientation(String name, int x, int y, AxisAlignedBB... collision) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.bounds = Block.FULL_BLOCK_AABB;
        this.collison = Lists.newArrayList(collision);
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
    public static StairOrientation getFromVecAndNeighbors(Vec3d hit, @Nullable EnumFacing facing, World world) {
        double hitXFromCenter = hit.x - CENTER_OFFSET;
        double hitYFromCenter = hit.y - CENTER_OFFSET;
        double hitZFromCenter = hit.z - CENTER_OFFSET;

        BlockPos pos = new BlockPos(hit.x, hit.y, hit.z);

        switch (facing.getAxis()) {
            case Y:
                int corner = getCorner(hitXFromCenter, hitZFromCenter);
                if (corner != -1) {
                    StairOrientation[] corners = new StairOrientation[]{SOUTH_EAST, NORTH_EAST, NORTH_WEST, SOUTH_WEST};
                    return corners[corner];
                } else if (hitYFromCenter > 0) {
                    if (isMax(hitXFromCenter, hitZFromCenter)) {
                        return hitXFromCenter > 0 ? WEST_DOWN : SOUTH_DOWN;
                    } else {
                        return hitZFromCenter > 0 ? NORTH_DOWN : EAST_DOWN;
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
                    StairOrientation[] corners = new StairOrientation[]{EAST_UP, NORTH_UP, NORTH_DOWN, EAST_DOWN};
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
                    StairOrientation[] corners = new StairOrientation[]{SOUTH_UP, WEST_UP, WEST_DOWN, SOUTH_DOWN};
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
                return StairOrientation.NORTH_DOWN;
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

    public List<AxisAlignedBB> getCollison() {
        return collison;
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