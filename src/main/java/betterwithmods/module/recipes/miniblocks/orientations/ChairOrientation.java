package betterwithmods.module.recipes.miniblocks.orientations;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public enum ChairOrientation implements IOrientation {

    SOUTH("south", EnumFacing.SOUTH),
    WEST("west", EnumFacing.WEST),
    NORTH("north", EnumFacing.NORTH),
    EAST("east", EnumFacing.EAST);


    public static final ChairOrientation[] VALUES = values();
    private static final AxisAlignedBB BOX = new AxisAlignedBB(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);
    private final String name;
    private final EnumFacing facing;

    ChairOrientation(String name, EnumFacing facing) {
        this.name = name;
        this.facing = facing;
    }

    public static ChairOrientation fromFace(EnumFacing facing) {
        if (facing != null)
            return ChairOrientation.VALUES[facing.getHorizontalIndex()];
        return NORTH;
    }

    public static ChairOrientation getFromVec(EntityLivingBase player, Vec3d hit, EnumFacing facing) {
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

    @Override
    public IOrientation next() {
        return VALUES[(this.ordinal() + 1) % (VALUES.length)];
    }

    public static IOrientationPlacer<ChairOrientation> PLACER = (placer, face, stack, hit) -> getFromVec(placer,hit,face);
}

