package betterwithmods.api.tile;

import betterwithmods.client.model.filters.ModelWithResource;
import betterwithmods.common.tile.TileFilteredHopper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHopperFilter {
    boolean allow(ItemStack stack);

    ResourceLocation getName();

    Ingredient getFilter();

    default void onInsert(World world, BlockPos pos, TileFilteredHopper tile, Entity entity) {
    }

    @SideOnly(Side.CLIENT)
    ModelWithResource getModelOverride(ItemStack filter);

    @SideOnly(Side.CLIENT)
    default void setModelOverride(ModelWithResource model) {
    }
}
