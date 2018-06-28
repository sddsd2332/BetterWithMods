package betterwithmods.common.blocks.mechanical.mech_machine;

import betterwithmods.BWMod;
import betterwithmods.api.block.IUrnConnector;
import betterwithmods.common.blocks.mechanical.tile.TileFilteredHopper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockFilteredHopper extends BlockMechMachine implements IUrnConnector {
    public static final ResourceLocation HOPPER = LootTableList.register(new ResourceLocation(BWMod.MODID, "block/hopper"));

    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 4 / 16d, 0, 1, 0.99d, 1);

    public BlockFilteredHopper() {
        super(Material.WOOD, HOPPER);
        this.useNeighborBrightness = true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileFilteredHopper) {
                TileFilteredHopper hopper = (TileFilteredHopper) tile;
                hopper.insert(entityIn);
            }
        }
        if (entityIn instanceof EntityItem) {
            entityIn.setPosition(entityIn.posX, entityIn.posY + 0.1, entityIn.posZ); //Fix to stop items being caught on this
        }
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFilteredHopper();
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOX;
    }

    @Override
    public int tickRate(World worldIn) {
        return 10;
    }

    @Override
    public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }


    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
