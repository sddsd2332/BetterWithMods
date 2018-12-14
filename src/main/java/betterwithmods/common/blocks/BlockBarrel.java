package betterwithmods.common.blocks;

import betterwithmods.common.tile.TileBarrel;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.BlockAxis;
import betterwithmods.library.utils.LocaleUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockBarrel extends BlockAxis {
    public BlockBarrel(Material material) {
        super(material);
        setHardness(2F);
        setResistance(10.0F);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing);
        FluidTank handler = (FluidTank) FluidUtil.getFluidHandler(worldIn, pos, facing);
        if (!worldIn.isRemote && handler != null) {
            FluidStack fluid = handler.getFluid();
            if (fluid != null) {
                int f = handler.getFluidAmount(), c = handler.getCapacity();
                playerIn.sendStatusMessage(LocaleUtils.getMessageComponent(ModLib.MODID, "barrel_info", fluid.getLocalizedName(), f, c), false);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileBarrel();
    }
}
