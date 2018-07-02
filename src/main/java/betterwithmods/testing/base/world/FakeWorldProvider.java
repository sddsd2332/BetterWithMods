package betterwithmods.testing.base.world;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

import javax.annotation.Nonnull;

public class FakeWorldProvider extends WorldProvider {
    @Nonnull
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }


}
