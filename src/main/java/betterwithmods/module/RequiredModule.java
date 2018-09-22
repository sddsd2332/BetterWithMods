package betterwithmods.module;

public abstract class RequiredModule extends Module {
    @Override
    protected boolean canEnable() {
        return true;
    }
}
