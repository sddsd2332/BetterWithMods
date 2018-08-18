package betterwithmods.module.recipes;

import betterwithmods.module.Module;
import betterwithmods.module.recipes.animal_restraint.AnimalRestraint;
import betterwithmods.module.recipes.miniblocks.MiniBlocks;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class Recipes extends Module {

    @Override
    public void addFeatures() {
        addFeature(new MetalReclaming());
        addFeature(new NuggetCompression());
        addFeature(new HarderSteelRecipe());
        addFeature(new AnvilRecipes());
        addFeature(new CraftingRecipes());
        addFeature(new CauldronRecipes());
        addFeature(new CrucibleRecipes());
        addFeature(new KilnRecipes());
        addFeature(new MillRecipes());
        addFeature(new SawRecipes());
        addFeature(new TurntableRecipes());
        addFeature(new HopperRecipes());
        addFeature(new NetherGrowth());
        addFeature(new AnimalRestraint().recipes());
        addFeature(new MiniBlocks());
        addFeature(new BlastingOil());
    }
}

