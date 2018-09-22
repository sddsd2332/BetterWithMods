package betterwithmods.api.modules.impl;

import betterwithmods.api.modules.CollectionStateHandler;
import betterwithmods.api.modules.IStateHandler;

import java.util.PriorityQueue;
import java.util.function.Consumer;

public abstract class PriorityStateHandler<T extends IStateHandler> extends CollectionStateHandler<T, PriorityQueue<T>> {

    @Override
    protected PriorityQueue<T> createCollection() {
        return new PriorityQueue<>();
    }

    @Override
    public void forEach(Consumer<T> t) {
        this.basedCollection.spliterator().forEachRemaining(t);
    }
}
