package betterwithmods.common.blocks;

import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.utils.ToolUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockLight extends BlockBase {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockLight() {
        super(Material.GLASS);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, true));
        this.setSoundType(SoundType.GLASS);
        ToolUtils.setPickaxesAsEffectiveAgainst(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ACTIVE) ? 15 : 0;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        onChanged(world, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        onChanged(world, pos, state);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
            world.setBlockState(pos, state.withProperty(ACTIVE, isPowered(world,pos)));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta == 1);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        IBlockState neighbor = world.getBlockState(pos.offset(side));
        return state != neighbor;
    }

    private boolean isPowered(World world, BlockPos pos) {
        return world.isBlockPowered(pos);
    }
    
    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    private void onChanged(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            boolean active = state.getValue(ACTIVE);
            boolean powered = isPowered(world,pos);
            if (active != powered) {
                world.scheduleUpdate(pos, this, 4);
            }
        }
    }
}
