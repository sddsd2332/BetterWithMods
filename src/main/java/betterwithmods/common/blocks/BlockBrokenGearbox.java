package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.StackIngredient;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 7/21/17.
 */
public class BlockBrokenGearbox extends BWMBlock {
    private Ingredient repairIngredient;
    private Block repairedBlock;
    private int repairCost;

    public BlockBrokenGearbox(Material material) {
        super(material);
        setHardness(3.5f);
        setRepairIngredient(StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOOD_GEAR)));
        setRepairedBlock(BWMBlocks.WOODEN_GEARBOX);
        setRepairCost(2);
    }

    public void setRepairIngredient(StackIngredient ingredient) {
        this.repairIngredient = ingredient;
    }

    public void setRepairedBlock(Block repairedBlock) {
        this.repairedBlock = repairedBlock;
    }

    public void setRepairCost(int count) {
        this.repairCost = count;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (PlayerHelper.isHolding(playerIn, repairIngredient)) {
            if (InvUtils.usePlayerItemStrict(playerIn, EnumFacing.UP, repairIngredient, repairCost)) {
                worldIn.setBlockState(pos, repairedBlock.getDefaultState().withProperty(DirUtils.FACING, state.getValue(DirUtils.FACING)));
                worldIn.playSound(null, pos, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1, 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(DirUtils.FACING, facing);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DirUtils.FACING, EnumFacing.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DirUtils.FACING).getIndex();
    }
}
