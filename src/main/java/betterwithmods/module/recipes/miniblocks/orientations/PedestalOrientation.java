package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public enum PedestalOrientation implements IOrientation<PedestalOrientation> {
    DOWN("down", new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    UP("up", new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    NORTH("north", new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    SOUTH("south", new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    WEST("west", new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    EAST("east", new AxisAlignedBB(0, 0, 0, 1, 1, 1));

    public static final PedestalOrientation[] VALUES = values();

    public static final IOrientationPlacer<PedestalOrientation> PLACER = (placer, face, stack, hit) -> {
       return fromFace(placer.isSneaking() ? face.getOpposite() : face);
    };

    private final String name;
    private final AxisAlignedBB bounds;

    PedestalOrientation(String name, AxisAlignedBB bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    public static PedestalOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return PedestalOrientation.VALUES[facing.getIndex()];
        return PedestalOrientation.DOWN;
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
    public BlockFaceShape getFaceShape(EnumFacing facing) {
        return BlockFaceShape.SOLID;
    }

    @Override
    public PedestalOrientation[] allValues() {
        return VALUES;
    }
}

