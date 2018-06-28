package betterwithmods.common.blocks.mechanical.mech_machine;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.mechanical.tile.TileTurntable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockTurntable extends BlockMechMachine {
    public static final ResourceLocation TURNTABLE = LootTableList.register(new ResourceLocation(BWMod.MODID, "block/turntable"));
    public static final PropertyInteger SPEED = PropertyInteger.create("speed", 0, 3);

    public BlockTurntable() {
        super(Material.WOOD, TURNTABLE);
        useNeighborBrightness = true;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SPEED, ACTIVE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileTurntable();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.getTileEntity(pos) instanceof TileTurntable) {
            return ((TileTurntable) world.getTileEntity(pos)).processRightClick(player);
        }
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.getTileEntity(pos) instanceof TileTurntable) {
            if (!world.getGameRules().getBoolean("doDaylightCycle"))
                ((TileTurntable) world.getTileEntity(pos)).toggleAsynchronous(null);
        }
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileTurntable) {
            TileTurntable tile = (TileTurntable) world.getTileEntity(pos);
            return state.withProperty(SPEED, tile.getTimerPos());
        }
        return state;
    }
}
