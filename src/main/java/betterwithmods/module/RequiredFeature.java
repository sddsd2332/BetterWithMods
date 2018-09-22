package betterwithmods.module;

public abstract class RequiredFeature extends Feature {
    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
