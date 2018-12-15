package betterwithmods.module.recipes.miniblocks.orientations;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.CENTER_OFFSET;
import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.getCorner;
import static betterwithmods.module.recipes.miniblocks.orientations.OrientationUtils.isMax;

public enum StairOrientation implements IOrientation<StairOrientation> {


    SOUTH_DOWN("south_down"),
    WEST_DOWN("west_down"),
    NORTH_DOWN("north_down"),
    EAST_DOWN("east_down"),

    SOUTH_VERT("south_vert"),
    WEST_VERT("west_vert"),
    NORTH_VERT("north_vert"),
    EAST_VERT("east_vert"),

    SOUTH_UP("south_up"),
    WEST_UP("west_up"),
    NORTH_UP("north_up"),
    EAST_UP("east_up");


    public static final StairOrientation[] VALUES = values();


    public static final IOrientationPlacer<StairOrientation> PLACER = (placer, face, stack, hit) -> getFromVec(placer,hit, face);


    private final String name;
    private final AxisAlignedBB bounds;
    private final List<AxisAlignedBB> collison;


    StairOrientation(String name, AxisAlignedBB... collision) {
        this.name = name;
        this.bounds = Block.FULL_BLOCK_AABB;

        this.collison = Lists.newArrayList(collision);
        if (this.collison.isEmpty()) {
            this.collison.add(Block.FULL_BLOCK_AABB);
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

    public static StairOrientation getFromVec(EntityLivingBase placer, Vec3d hit, @Nullable EnumFacing facing) {
        double hitXFromCenter = hit.x - CENTER_OFFSET;
        double hitYFromCenter = hit.y - CENTER_OFFSET;
        double hitZFromCenter = hit.z - CENTER_OFFSET;


        switch (facing.getAxis()) {
            case Y:
                int corner = getCorner(hitXFromCenter, hitZFromCenter);
                if (corner != -1) {
                    StairOrientation[] corners = new StairOrientation[]{EAST_VERT, NORTH_VERT, WEST_VERT, SOUTH_VERT};
                    return corners[corner];
                } else if(hitYFromCenter > 0) {
                    if (isMax(hitXFromCenter, hitZFromCenter)) {

                        return SOUTH_VERT;
                    } else {
                        return SOUTH_VERT;
                    }
                } else {
                    if (isMax(hitXFromCenter, hitZFromCenter)) {
                        return SOUTH_VERT;
                    } else {
                        return SOUTH_VERT;
                    }
                }

            case X:
                corner = getCorner(hitYFromCenter, hitZFromCenter);
                if (corner != -1) {
                    StairOrientation[] corners = new StairOrientation[]{SOUTH_DOWN, SOUTH_DOWN, SOUTH_DOWN, SOUTH_DOWN};
                    return corners[corner];
                } else if (hitXFromCenter > 0) {
                    if (isMax(hitYFromCenter, hitZFromCenter)) {
                        return hitYFromCenter > 0 ? WEST_UP : WEST_DOWN;
                    } else {

                    }
                } else {
                    if (isMax(hitYFromCenter, hitZFromCenter)) {
                        return hitYFromCenter > 0 ? EAST_UP : EAST_DOWN;
                    } else {

                    }
                }
            case Z:
                corner = getCorner(hitYFromCenter, hitXFromCenter);
                if (corner != -1) {
                    StairOrientation[] corners = new StairOrientation[]{SOUTH_DOWN, SOUTH_DOWN, SOUTH_DOWN, SOUTH_DOWN};
                    return corners[corner];
                } else if (hitZFromCenter > 0) {
                    if (isMax(hitXFromCenter, hitYFromCenter)) {

                    } else {
                        return hitYFromCenter > 0 ? NORTH_UP : NORTH_DOWN;
                    }
                } else {
                    if (isMax(hitXFromCenter, hitYFromCenter)) {
                        return hitYFromCenter > 0 ? SOUTH_UP : SOUTH_DOWN;
                    } else {
                        return hitYFromCenter > 0 ? SOUTH_UP : SOUTH_DOWN;
                    }
                }
            default:
                return StairOrientation.SOUTH_DOWN;
        }
    }

    @Override
    public StairOrientation[] allValues() {
        return VALUES;
    }
}