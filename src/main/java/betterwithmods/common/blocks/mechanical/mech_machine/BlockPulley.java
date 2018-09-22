package betterwithmods.common.blocks.mechanical.mech_machine;

import betterwithmods.common.tile.TilePulley;
import betterwithmods.lib.ModLib;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPulley extends BlockMechMachine {

    public static final ResourceLocation PULLEY = LootTableList.register(new ResourceLocation(ModLib.MODID, "block/pulley"));

    public BlockPulley() {
        super(Material.WOOD, PULLEY);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TilePulley();
    }
}
