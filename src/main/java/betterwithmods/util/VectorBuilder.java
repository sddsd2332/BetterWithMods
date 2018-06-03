package betterwithmods.util;

import net.minecraft.util.math.Vec3i;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class VectorBuilder {

    private Random random = new Random();

    private float x, y, z;

    public VectorBuilder set(Vec3i pos) {
        return set(pos.getX(), pos.getY(), pos.getZ());
    }

    public VectorBuilder set(double x, double y, double z) {
        return set((float) x, (float) y, (float) z);
    }

    public VectorBuilder set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }


    public VectorBuilder offset(float offset) {
        return this.offset(offset, offset, offset);
    }

    public VectorBuilder offset(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public VectorBuilder rand(float multiplier) {
        return rand(multiplier, multiplier, multiplier);
    }

    public VectorBuilder rand(float multiplierX, float multiplierY, float multiplierZ) {
        this.x += random.nextFloat() * multiplierX;
        this.y += random.nextFloat() * multiplierY;
        this.z += random.nextFloat() * multiplierZ;
        return this;
    }


    public VectorBuilder setGaussian(float multiplierX, float multiplierY, float multiplierZ) {
        this.x = (float) (random.nextGaussian() * multiplierX);
        this.y = (float) (random.nextGaussian() * multiplierY);
        this.z = (float) (random.nextGaussian() * multiplierZ);
        return this;
    }

    public VectorBuilder setGaussian(float multiplier) {
        return setGaussian(multiplier,multiplier,multiplier);
    }

    public VectorBuilder reset() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    public Vector3f build() {
        Vector3f vec = new Vector3f(x, y, z);
        reset();
        return vec;
    }


}
