package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.recipe.input.IRecipeInputs;
import betterwithmods.api.recipe.output.IRecipeOutputs;
import net.minecraft.util.SoundEvent;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class MillRecipe extends BulkRecipe<MillRecipe> {
    private SoundEvent sound;

    private int ticks;

    public MillRecipe(IRecipeInputs recipeInputs, IRecipeOutputs recipeOutput) {
        super(recipeInputs, recipeOutput);
    }

    public SoundEvent getSound() {
        return sound;
    }

    public MillRecipe setSound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }


    @Override
    public MillRecipe setPriority(int priority) {
        return (MillRecipe) super.setPriority(priority);
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

}
