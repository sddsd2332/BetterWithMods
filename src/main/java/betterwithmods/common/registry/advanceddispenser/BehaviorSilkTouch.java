package betterwithmods.common.registry.advanceddispenser;

import betterwithmods.api.tile.dispenser.IBehaviorCollect;
import betterwithmods.library.lib.ReflectionLib;
import betterwithmods.library.utils.ListUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by primetoxinz on 5/25/17.
 */
public class BehaviorSilkTouch implements IBehaviorCollect {

    private static final Method method = ReflectionHelper.findMethod(Block.class, ReflectionLib.SILK_TOUCH_DROP.getKey(), ReflectionLib.SILK_TOUCH_DROP.getValue(), IBlockState.class);

    public static ItemStack getBlockSilkTouchDrop(IBlockState state) {
        try {
            return (ItemStack) method.invoke(state.getBlock(), state);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public NonNullList<ItemStack> collect(IBlockSource source) {
        NonNullList<ItemStack> list = ListUtils.asNonnullList(getBlockSilkTouchDrop(source.getBlockState()));
        breakBlock(source.getWorld(), source.getBlockState(), source.getBlockPos());
        return list;
    }
}
