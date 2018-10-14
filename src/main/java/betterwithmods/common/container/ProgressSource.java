package betterwithmods.common.container;

import betterwithmods.library.common.container.IProgressSource;

import java.util.function.Supplier;

public class ProgressSource implements IProgressSource {

    private Supplier<Integer> max, progress;
    private Supplier<Boolean> show;

    public ProgressSource(Supplier<Integer> max, Supplier<Integer> progress, Supplier<Boolean> show) {
        this.max = max;
        this.progress = progress;
        this.show = show;
    }

    @Override
    public int getMax() {
        return max.get();
    }

    @Override
    public int getProgress() {
        return progress.get();
    }

    @Override
    public boolean showProgress() {
        return show.get();
    }
}
