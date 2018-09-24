package betterwithmods.api.tile;

import betterwithmods.library.utils.BannerUtils;
import net.minecraft.item.ItemStack;

public interface IBannerInfo {

    default void apply(ItemStack stack) {
        BannerUtils.BannerData data = BannerUtils.fromStack(stack);
        if (data != null) {
            setBannerData(getSelected(), data);
            next();
        }
    }

    int getSelected();

    void next();

    void setBannerData(int selected, BannerUtils.BannerData data);

    BannerUtils.BannerData[] getData();

}
