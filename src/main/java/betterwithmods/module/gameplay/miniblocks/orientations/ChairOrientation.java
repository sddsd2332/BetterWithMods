package betterwithmods.module.gameplay.miniblocks.orientations;

import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public enum ChairOrientation implements BaseOrientation {

    NORTH("north", EnumFacing.NORTH, 0, 0),
    SOUTH("south", EnumFacing.SOUTH, 0, 90),
    EAST("east", EnumFacing.EAST, 0, 180),
    WEST("west", EnumFacing.WEST, 0, 270);

    public static final ChairOrientation[] VALUES = values();
    private final String name;
    private final EnumFacing facing;
    private final int x;
    private final int y;

    ChairOrientation(String name, EnumFacing facing, int x, int y) {
        this.name = name;
        this.facing = facing;
        this.x = x;
        this.y = y;
    }

    public static BaseOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return ChairOrientation.VALUES[facing.getHorizontalIndex()];
        return NORTH;
    }

    public static BaseOrientation getFromVec(EntityLivingBase player, Vec3d hit, EnumFacing facing) {
        return fromFace(player.getHorizontalFacing());
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
        return BOX;
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

    private static final AxisAlignedBB BOX = new AxisAlignedBB(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);
}

