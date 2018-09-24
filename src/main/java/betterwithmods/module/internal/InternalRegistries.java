package betterwithmods.module.internal;

import betterwithmods.library.modularity.impl.RequiredModule;

public class InternalRegistries extends RequiredModule {


    @Override
    public void setup() {
        addFeatures(
                new UnitTesting(),
                new BlockRegistry(),
                new SoundRegistry(),
                new EntityRegistry()
        );
    }

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }
}
