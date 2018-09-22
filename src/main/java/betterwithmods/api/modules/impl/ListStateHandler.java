package betterwithmods.api.modules.impl;

import betterwithmods.api.modules.CollectionStateHandler;
import betterwithmods.api.modules.IStateHandler;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public abstract class ListStateHandler<T extends IStateHandler> extends CollectionStateHandler<T, ArrayList<T>> {
    @Override
    protected ArrayList<T> createCollection() {
        return Lists.newArrayList();
    }
}
