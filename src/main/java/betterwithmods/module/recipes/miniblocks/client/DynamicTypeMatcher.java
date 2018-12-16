package betterwithmods.module.recipes.miniblocks.client;

import betterwithmods.library.client.baking.IModelMatcher;
import betterwithmods.module.recipes.miniblocks.DynamicType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class DynamicTypeMatcher implements IModelMatcher {

    private DynamicType type;

    public DynamicTypeMatcher(DynamicType type) {
        this.type = type;
    }

    @Override
    public boolean test(ModelResourceLocation location) {
        return location.getPath().equals(type.getName());
    }
}
