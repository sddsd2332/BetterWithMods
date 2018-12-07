package betterwithmods.common.blocks;

import betterwithmods.library.common.block.IBlockActive;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockSteelPressurePlate extends BlockBasePressurePlate implements IBlockActive {

    public BlockSteelPressurePlate() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
        setSoundType(SoundType.METAL);
    }

    protected int getRedstoneStrength(@Nonnull IBlockState state) {
        return isActive(state) ? 15 : 0;
    }

    @Nonnull
    protected IBlockState setRedstoneStrength(@Nonnull IBlockState state, int strength) {
        return state.withProperty(ACTIVE, strength > 0);
    }

    protected void playClickOnSound(@Nonnull World worldIn, @Nonnull BlockPos color) {
        worldIn.playSound(null, color, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    protected void playClickOffSound(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }

    protected int computeRedstoneStrength(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
        List<? extends Entity> list = worldIn.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    return 15;
                }
            }
        }

        return 0;
    }

    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta == 1);
    }

    public int getMetaFromState(IBlockState state) {
        return isActive(state) ? 1 : 0;
    }

    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getProperties());
    }

    @Override
    public IProperty<?>[] getProperties() {
        return IBlockActive.super.getProperties();
    }
}
