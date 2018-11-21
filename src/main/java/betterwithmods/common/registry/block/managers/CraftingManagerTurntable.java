package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.block.recipe.TurntableRecipe;
import betterwithmods.common.tile.TileTurntable;
import betterwithmods.lib.ModLib;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerTurntable extends CraftingManagerBlock<TurntableRecipe> {

    public CraftingManagerTurntable() {
        super(new ResourceLocation(ModLib.MODID, "turntable"), TurntableRecipe.class);
    }

    public static TileTurntable findTurntable(World world, BlockPos craftingPos) {
        for (int i = 1; i <= 2; i++) {
            TileEntity tile = world.getTileEntity(craftingPos.down(i));
            if (tile instanceof TileTurntable) {
                return (TileTurntable) tile;
            }
        }
        return null;
    }

    protected List<TurntableRecipe> findRecipe(IBlockState output) {
        return getValuesCollection().stream().filter(r -> r.getProductState() == output).collect(Collectors.toList());
    }

    public boolean remove(IBlockState output) {
        return getValuesCollection().removeAll(findRecipe(output));
    }

}
