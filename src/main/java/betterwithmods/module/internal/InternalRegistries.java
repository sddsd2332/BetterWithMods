package betterwithmods.module.internal;

import betterwithmods.library.modularity.impl.RequiredModule;
import betterwithmods.network.BWMNetwork;

public class InternalRegistries extends RequiredModule {


    @Override
    public void setup() {
        addFeatures(
                new UnitTesting(),
                new BlockRegistry(),
                new ItemRegistry(),
                new PotionRegistry(),
                new SoundRegistry(),
                new EntityRegistry(),
                new RecipeRegistry(),
                new MiscRegistry(),
                BWMNetwork.INSTANCE
        );
    }

    //Register first.
    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }
}
