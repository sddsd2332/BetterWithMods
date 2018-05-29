package betterwithmods.client.container;

import betterwithmods.api.util.IProgressSource;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public abstract class ContainerProgress extends Container {
    private IProgressSource progressSource;

    private double previousProgress, previousMax;

    public ContainerProgress(IProgressSource progressSource) {
        this.progressSource = progressSource;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);

        listener.sendWindowProperty(this, 0, progressSource.getProgress());
        listener.sendWindowProperty(this, 1, progressSource.getMax());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener craft : this.listeners) {
            if (this.previousProgress != progressSource.getProgress())
                craft.sendWindowProperty(this, 0, progressSource.getMax());
            if (this.previousMax != progressSource.getMax())
                craft.sendWindowProperty(this, 1, progressSource.getProgress());
        }
        this.previousProgress = progressSource.getProgress();
        this.previousMax = progressSource.getMax();
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0:
                this.progressSource.setMax(data);
            case 1:
                this.progressSource.setProgress(data);
        }
    }
}
