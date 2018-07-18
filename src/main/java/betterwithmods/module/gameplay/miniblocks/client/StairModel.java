package betterwithmods.module.gameplay.miniblocks.client;

import betterwithmods.module.gameplay.miniblocks.orientations.StairOrientation;
import net.minecraftforge.client.model.IModel;

public class StairModel extends MiniModel {

    private final IModel inner_corner;

    public StairModel(IModel template, IModel inner_corner) {
        super(template);
        this.inner_corner = inner_corner;
    }

    @Override
    public IModel getTemplate(MiniInfo object) {
        if (object.getOrientation() instanceof StairOrientation) {
            StairOrientation orientation = (StairOrientation) object.getOrientation();
            if (orientation.isCorner()) {
                return inner_corner;
            }
        }
        return super.getTemplate(object);
    }
}
