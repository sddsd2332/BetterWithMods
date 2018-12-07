package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.module.recipes.miniblocks.orientations.StairOrientation;
import net.minecraftforge.client.model.IModel;

public class StairModel extends MiniModel {

    private final IModel inner_corner;

    public StairModel(IModel template, IModel inner_corner, String registryName) {
        super(template, registryName);
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
