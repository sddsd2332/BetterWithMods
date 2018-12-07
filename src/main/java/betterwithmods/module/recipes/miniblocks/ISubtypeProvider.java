package betterwithmods.module.recipes.miniblocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;

@FunctionalInterface
public interface ISubtypeProvider {

    Collection<IBlockState> getSubtypes(Material material);

}
