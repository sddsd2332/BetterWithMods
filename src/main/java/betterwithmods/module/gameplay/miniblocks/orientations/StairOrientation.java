package betterwithmods.module.gameplay.miniblocks.orientations;

import betterwithmods.module.gameplay.miniblocks.tiles.TileStair;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
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
    NORTH_UP("north_up", 180, 270, EnumFacing.UP, EnumFacing.NORTH, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D)),
    EAST_UP("east_up", 180, 0, EnumFacing.UP, EnumFacing.EAST, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
    WEST_UP("west_up", 180, 180, EnumFacing.UP, EnumFacing.WEST, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D)),
    SOUTH_UP("south_up", 180, 90, EnumFacing.UP, EnumFacing.SOUTH, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D)),

    NORTH_DOWN("north_down", 0, 270, EnumFacing.DOWN, EnumFacing.NORTH, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D)),
    EAST_DOWN("east_down", 0, 0, EnumFacing.DOWN, EnumFacing.EAST, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D)),
    WEST_DOWN("west_down", 0, 180, EnumFacing.DOWN, EnumFacing.WEST, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D)),
    SOUTH_DOWN("south_down", 0, 90, EnumFacing.DOWN, EnumFacing.SOUTH, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D)),

    SOUTH_WEST("south_west", 90, 90, EnumFacing.SOUTH, EnumFacing.WEST, new AxisAlignedBB(0, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D)),
    NORTH_WEST("north_west", 90, 180, EnumFacing.WEST, EnumFacing.WEST, new AxisAlignedBB(0, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D)),
    NORTH_EAST("north_east", 90, 270, EnumFacing.NORTH, EnumFacing.EAST, new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D)),
    SOUTH_EAST("south_east", 90, 0, EnumFacing.EAST, EnumFacing.EAST, new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D)),

    NORTH_DOWN_INNER_CORNER("north_down_inner_corner", 0, 270, true, EnumFacing.DOWN, EnumFacing.NORTH, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D)),
    SOUTH_DOWN_INNER_CORNER("south_down_inner_corner", 0, 90, true, EnumFacing.DOWN, EnumFacing.SOUTH, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D)),
    WEST_DOWN_INNER_CORNER("west_down_inner_corner", 0, 180, true, EnumFacing.DOWN, EnumFacing.WEST, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D)),
    EAST_DOWN_INNER_CORNER("east_down_inner_corner", 0, 0, true, EnumFacing.DOWN, EnumFacing.EAST, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D)),

    NORTH_UP_INNER_CORNER("north_up_inner_corner", 180, 270, true, EnumFacing.UP, EnumFacing.NORTH, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D)),
    SOUTH_UP_INNER_CORNER("south_up_inner_corner", 180, 90, true, EnumFacing.UP, EnumFacing.SOUTH, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
    WEST_UP_INNER_CORNER("west_up_inner_corner", 180, 180, true, EnumFacing.UP, EnumFacing.WEST, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D)),
    EAST_UP_INNER_CORNER("east_up_inner_corner", 180, 0, true, EnumFacing.UP, EnumFacing.EAST, new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D));


    public static final StairOrientation[] VALUES = values();
    //horizontal index
    //south: 0, west: 1, north: 2, east: 3


    //all index
    //down: 0
    // up: 1
    //NORTH: 1
    //SOUTH: 2
    //WEST: 3
    //EAST: 4


    public static final StairOrientation[] DOWN_LEFT_CORNER = new StairOrientation[]{SOUTH_DOWN_INNER_CORNER, WEST_DOWN_INNER_CORNER, NORTH_DOWN_INNER_CORNER, EAST_DOWN_INNER_CORNER};

    public static final StairOrientation[] DOWN_RIGHT_CORNER = new StairOrientation[]{WEST_DOWN_INNER_CORNER, NORTH_DOWN_INNER_CORNER, EAST_DOWN_INNER_CORNER, SOUTH_DOWN_INNER_CORNER};


    public static final StairOrientation[] UP_LEFT_CORNER = new StairOrientation[]{EAST_UP_INNER_CORNER, SOUTH_UP_INNER_CORNER, WEST_UP_INNER_CORNER, NORTH_UP_INNER_CORNER};

    public static final StairOrientation[] UP_RIGHT_CORNER = new StairOrientation[]{SOUTH_UP_INNER_CORNER, WEST_UP_INNER_CORNER, NORTH_UP_INNER_CORNER, EAST_UP_INNER_CORNER};

    public static final StairOrientation[][] LEFT_CORNERS = new StairOrientation[][]{
            DOWN_LEFT_CORNER,
            UP_LEFT_CORNER,
            UP_LEFT_CORNER,
            UP_LEFT_CORNER,
            UP_LEFT_CORNER,
    };


    public static final StairOrientation[][] RIGHT_CORNERS = new StairOrientation[][]{
            DOWN_RIGHT_CORNER,
            UP_RIGHT_CORNER,
            UP_RIGHT_CORNER,
            UP_RIGHT_CORNER,
            UP_RIGHT_CORNER,
            UP_RIGHT_CORNER,
    };


    public static final StairOrientation[] UP_HORIZONTALS = new StairOrientation[]{SOUTH_UP, WEST_UP, NORTH_UP, EAST_UP,};
    public static final StairOrientation[] DOWN_HORIZONTALS = new StairOrientation[]{SOUTH_DOWN, WEST_DOWN, NORTH_DOWN, EAST_DOWN,};
    public static final StairOrientation[][] FROM_FACING = new StairOrientation[][]{
            UP_HORIZONTALS,
            DOWN_HORIZONTALS
    };

    private final String name;
    private final AxisAlignedBB bounds;
    private final List<AxisAlignedBB> collison;
    private final EnumFacing bottom, facing;
    private final boolean corner;
    private final int x;
    private final int y;

    StairOrientation(String name, int x, int y, EnumFacing bottom, EnumFacing facing, AxisAlignedBB... collision) {
        this(name, x, y, false, bottom, facing, collision);
    }


    StairOrientation(String name, int x, int y, boolean corner, EnumFacing bottom, EnumFacing facing, AxisAlignedBB... collision) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.corner = corner;
        this.bottom = bottom;
        this.facing = facing;
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

    public static StairOrientation getFromPos(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileStair) {
            BaseOrientation o = ((TileStair) tile).getOrientation();
            if (o instanceof StairOrientation) {
                return (StairOrientation) o;
            }
        }
        return null;


    }

    public static StairOrientation fromNeighbors(EntityLivingBase placer, BlockPos pos, Vec3d hit, @Nullable EnumFacing facing) {

        StairOrientation orientation = getFromVec(placer, hit, facing);

        //just horizontals for now
        EnumFacing front = orientation.getFacing();
        EnumFacing rotated = front.rotateY();

        StairOrientation frontNeighbor = getFromPos(placer.getEntityWorld(), pos.offset(front));

        if (frontNeighbor != null) {
            if (frontNeighbor.getBottom() == orientation.getBottom()) {
                int i = frontNeighbor.getFacing().getHorizontalIndex();
                int x = orientation.getBottom().getIndex();
                if (frontNeighbor.getFacing() == rotated) {
                    return LEFT_CORNERS[x][i];
                }
                return RIGHT_CORNERS[x][i];
            }
        }

        StairOrientation backNeighbor = getFromPos(placer.getEntityWorld(), pos.offset(front.getOpposite()));
        if (backNeighbor != null) {
            if (backNeighbor.getBottom() == orientation.getBottom()) {
                if (backNeighbor.getFacing() == rotated) {
                    return orientation;
                }
            }
        }

        return orientation;
    }

    @SuppressWarnings("Duplicates")
    public static StairOrientation getFromVec(EntityLivingBase placer, Vec3d hit, @Nullable EnumFacing facing) {
        double hitXFromCenter = hit.x - CENTER_OFFSET;
        double hitYFromCenter = hit.y - CENTER_OFFSET;
        double hitZFromCenter = hit.z - CENTER_OFFSET;


        switch (facing.getAxis()) {
            case Y:
                int corner = getCorner(hitXFromCenter, hitZFromCenter);
                if (corner != -1) {
                    StairOrientation[] corners = new StairOrientation[]{SOUTH_EAST, NORTH_EAST, NORTH_WEST, SOUTH_WEST};
                    return corners[corner];
                } else {
                    int v = facing == EnumFacing.UP ? 1 : 0;
                    return FROM_FACING[v][placer.getHorizontalFacing().getHorizontalIndex()];
                }
//                if (hitYFromCenter > 0) {
//                    if (isMax(hitXFromCenter, hitZFromCenter)) {
//                        return hitXFromCenter > 0 ? WEST_DOWN : EAST_DOWN;
//                    } else {
//                        return hitZFromCenter > 0 ? NORTH_DOWN : SOUTH_DOWN;
//                    }
//                } else {
//                    if (isMax(hitXFromCenter, hitZFromCenter)) {
//                        return hitXFromCenter > 0 ? EAST_UP : WEST_UP;
//                    } else {
//                        return hitZFromCenter > 0 ? SOUTH_UP : NORTH_UP;
//                    }
//                }
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

    public boolean isCorner() {
        return corner;
    }

    public EnumFacing getBottom() {
        return bottom;
    }

    public EnumFacing getFacing() {
        return facing;
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