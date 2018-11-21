package betterwithmods.common.registry.bulk.recipes.builder;

import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.module.internal.SoundRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class MillRecipeBuilder extends BulkRecipeBuilder<MillRecipe> {

    private SoundEvent sound = SoundRegistry.BLOCK_GRIND_NORMAL;
    private int ticks = 200;

    public MillRecipeBuilder ticks(int ticks) {
        this.ticks = ticks;
        return this;
    }

    public MillRecipeBuilder sound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }


    public MillRecipeBuilder sound(String sound) {
        SoundEvent s = null;
        if (sound != null && !sound.isEmpty()) {
            try {
                s = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sound));
            } catch (Throwable ignore) {
            }
        }
        this.sound = s;
        return this;
    }


    @Override
    protected MillRecipe create() {
        MillRecipe recipe = new MillRecipe(inputs, outputs);
        recipe.setTicks(ticks);
        recipe.setSound(sound);

        return new MillRecipe(inputs, outputs);
    }

    @Override
    public void reset() {
        super.reset();
        sound = SoundRegistry.BLOCK_GRIND_NORMAL;
        ticks = 200;
    }
}
