package betterwithmods.api.util;

import net.minecraft.block.state.IBlockState;

public interface IVariantProvider {

    boolean match(IBlockState state);

    IBlockVariants getVariant(IBlockVariants.EnumBlock block, IBlockState state);

}
