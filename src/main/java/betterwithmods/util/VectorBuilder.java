package betterwithmods.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;


import java.util.Random;

public class VectorBuilder {

    private Random random = new Random();

    private double x, y, z;

    public VectorBuilder set(Vec3i pos) {
        return set(pos.getX(), pos.getY(), pos.getZ());
    }

    public VectorBuilder set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }


    public VectorBuilder offset(double offset) {
        return this.offset(offset, offset, offset);
    }

    public VectorBuilder offset(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public VectorBuilder rand(double multiplier) {
        return rand(multiplier, multiplier, multiplier);
    }

    public VectorBuilder rand(double multiplierX, double multiplierY, double multiplierZ) {
        this.x += random.nextDouble() * multiplierX;
        this.y += random.nextDouble() * multiplierY;
        this.z += random.nextDouble() * multiplierZ;
        return this;
    }


    public VectorBuilder setGaussian(double multiplierX, double multiplierY, double multiplierZ) {
        this.x = random.nextGaussian() * multiplierX;
        this.y = random.nextGaussian() * multiplierY;
        this.z = random.nextGaussian() * multiplierZ;
        return this;
    }

    public VectorBuilder setGaussian(double multiplier) {
        return setGaussian(multiplier,multiplier,multiplier);
    }

    public VectorBuilder reset() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    public Vec3d build() {
        Vec3d vec = new Vec3d(x, y, z);
        reset();
        return vec;
    }


}
