package betterwithmods.module.gameplay.miniblocks.orientations;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public enum PedestalOrientation implements BaseOrientation {
    DOWN("down", EnumFacing.DOWN, 180, 0, new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    UP("up", EnumFacing.DOWN, 0, 0, new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    NORTH("north", EnumFacing.NORTH, 90, 0, new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    SOUTH("south", EnumFacing.SOUTH, 270, 0, new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    WEST("west", EnumFacing.WEST, 270, 90, new AxisAlignedBB(0, 0, 0, 1, 1, 1)),
    EAST("east", EnumFacing.EAST, 270, 270, new AxisAlignedBB(0, 0, 0, 1, 1, 1));

    public static final PedestalOrientation[] VALUES = values();

    private final String name;
    private final EnumFacing facing;
    private final AxisAlignedBB bounds;
    private final int x;
    private final int y;

    PedestalOrientation(String name, EnumFacing facing, int x, int y, AxisAlignedBB bounds) {
        this.name = name;
        this.facing = facing;
        this.x = x;
        this.y = y;
        this.bounds = bounds;
    }

    public static BaseOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return PedestalOrientation.VALUES[facing.getIndex()];
        return BaseOrientation.DEFAULT;
    }

    public static BaseOrientation getFromVec(Vec3d hit, EnumFacing facing) {
        return fromFace(facing);
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

    @Override
    public BlockFaceShape getFaceShape(EnumFacing facing) {
        return BlockFaceShape.SOLID;
    }
}

