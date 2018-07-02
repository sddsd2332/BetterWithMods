package betterwithmods.common.registry.variants;

import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import net.minecraft.item.ItemStack;

import static betterwithmods.api.util.IBlockVariants.EnumBlock.*;

public class WoodVariant extends BlockVariant {

    public static WoodVariant builder() {
        return new WoodVariant();
    }

    @Override
    public WoodVariant addVariant(EnumBlock type, ItemStack stack) {
        if (type == LOG) {
            addVariant(BARK, ItemBark.fromParentStack(BWMItems.BARK, stack, 1));
            addVariant(SAWDUST, ItemMaterial.getStack(ItemMaterial.EnumMaterial.SAWDUST,1));
        }
        return (WoodVariant) super.addVariant(type, stack);
    }
}
