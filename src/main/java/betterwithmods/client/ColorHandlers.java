package betterwithmods.client;

import betterwithmods.common.blocks.BlockPlanter;
import betterwithmods.common.items.tools.BWMArmor;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;

/**
 * Created by Christian on 21.10.2016.
 */
public class ColorHandlers {
    public static final IBlockColor BLOCK_PLANTER = (state, worldIn, pos, tintIndex) ->
            state.getBlock() instanceof BlockPlanter ? ((BlockPlanter) state.getBlock()).colorMultiplier(worldIn, pos, tintIndex) : -1;
    public static final IBlockColor BLOCK_FOLIAGE = (state, worldIn, pos, tintIndex) ->
            worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
    public static final IBlockColor BLOCK_BLOOD_LEAF = (state, worldIn, pos, tintIndex) ->
            worldIn != null && pos != null ? blendColors(0xB70606, BiomeColorHelper.getFoliageColorAtPos(worldIn, pos)) : blendColors(0xB70606, ColorizerFoliage.getFoliageColor(0.5D, 1.0D));
    public static final IBlockColor BLOCK_GRASS = (state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);

    public static final IItemColor ITEM_PLANTER = (stack, tintIndex) -> ColorizerGrass.getGrassColor(0.5D, 1.0D);
    public static final IItemColor ITEM_GRASS = (stack, tintIndex) -> ColorizerGrass.getGrassColor(0.5D, 1.0D);
    public static final IItemColor ITEM_FOLIAGE = (stack, tintIndex) ->
            BLOCK_FOLIAGE.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex);
    public static final IItemColor ITEM_BLOOD_LEAF = (stack, tintIndex) ->
            BLOCK_BLOOD_LEAF.colorMultiplier(((ItemBlock) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex);

    public static final IItemColor armor = (stack, tintIndex) ->
            (tintIndex != 1 && stack.getItem() instanceof BWMArmor) ? ((BWMArmor) stack.getItem()).getColor(stack) : -1;


    public static int blendColors(int main, int blend) {
        float mRatio = 0.825F;
        float ratio = 0.175F;

        int mainA = main >> 24 & 0xFF;
        int mainR = (main & 0xFF0000) >> 16;
        int mainG = (main & 0xFF00) >> 8;
        int mainB = main & 0xFF;

        int blendA = blend >> 24 & 0xFF;
        int blendR = (blend & 0xFF0000) >> 16;
        int blendG = (blend & 0xFF00) >> 8;
        int blendB = blend & 0xFF;

        int a = (int) ((mainA * mRatio) + (blendA * ratio));
        int r = (int) ((mainR * mRatio) + (blendR * ratio));
        int g = (int) ((mainG * mRatio) + (blendG * ratio));
        int b = (int) ((mainB * mRatio) + (blendB * ratio));

        return a << 24 | r << 16 | g << 8 | b;
    }

}
