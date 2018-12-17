package betterwithmods.module.conversion.enchanting;

import betterwithmods.library.common.modularity.impl.Feature;

public class HCEnchanting extends Feature {

    @Override
    public boolean hasEvent() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Make fundamental changes to how the vanilla enchantment table functions";
    }
}
