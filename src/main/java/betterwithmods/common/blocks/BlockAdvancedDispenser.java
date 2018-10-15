package betterwithmods.common.blocks;

import betterwithmods.BetterWithMods;
import betterwithmods.api.tile.dispenser.IBehaviorCollect;
import betterwithmods.api.tile.dispenser.IBehaviorEntity;
import betterwithmods.common.tile.TileAdvancedDispenser;
import betterwithmods.library.common.block.BlockActiveFacing;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.DirUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.RedstoneUtils;
import betterwithmods.library.utils.ingredient.EntityIngredient;
import betterwithmods.module.internal.AdvancedDispenserRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockAdvancedDispenser extends BlockActiveFacing {

    public BlockAdvancedDispenser() {
        super(Material.ROCK);
        this.setHardness(3.5F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public int tickRate(World worldIn) {
        return 1;
    }

    @Override
    public boolean onBlockActivated(World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking() && !world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && CapabilityUtils.hasInventory(tile, null)) {
                player.openGui(BetterWithMods.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, @Nonnull BlockPos pos, Block blockIn, BlockPos other) {
        boolean flag = RedstoneUtils.isRedstonePowered(state, world, pos);
        boolean flag1 = state.getValue(ACTIVE);
        if (flag && !flag1) {
            world.scheduleUpdate(pos, this, this.tickRate(world));
            world.setBlockState(pos, state.withProperty(ACTIVE, true), 5);
        } else if (!flag && flag1) {
            world.scheduleUpdate(pos, this, this.tickRate(world));
            world.setBlockState(pos, state.withProperty(ACTIVE, false), 5);
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileAdvancedDispenser) {
            InventoryUtils.ejectInventoryContents(world, pos, ((TileAdvancedDispenser) te).inventory);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAdvancedDispenser();
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, @Nonnull BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return CapabilityUtils.getInventory(tile, null).map(InventoryUtils::calculateComparatorLevel).orElse(0);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(DirUtils.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            dispense(worldIn, pos, state.getValue(ACTIVE));
        }
    }

    public void dispense(@Nonnull World world, @Nonnull BlockPos pos, boolean active) {
        BlockSourceImpl impl = new BlockSourceImpl(world, pos);
        TileAdvancedDispenser tile = impl.getBlockTileEntity();

        if (active) {
            int index = tile.nextIndex;
            ItemStack stack = tile.getNextStackFromInv();
            if (index == -1 || stack.isEmpty())
                world.playEvent(1001, pos, 0);
            else {
                IBehaviorDispenseItem behavior = AdvancedDispenserRegistry.BLOCK_DISPENSER_REGISTRY.findValue(stack);
                behavior.dispense(impl, stack);
            }
        } else {

            BlockPos check = pos.offset(impl.getBlockState().getValue(DirUtils.FACING));
            IBlockState state = world.getBlockState(check);
            Block block = state.getBlock();


            if (world.getBlockState(check).getBlockHardness(world, check) < 0)
                return;

            IBehaviorEntity behaviorEntity = AdvancedDispenserRegistry.ENTITY_COLLECT_REGISTRY.findValue(world, check, null);
            Entity entity = EntityIngredient.getEntity(world, check).orElse(null);
            if (behaviorEntity != null && entity != null) {
                NonNullList<ItemStack> stacks = behaviorEntity.collect(world, check, entity, tile.getCurrentSlot());
                InventoryUtils.insert(tile.getWorld(), check, tile.inventory, stacks, false);
                return;
            }


            IBehaviorCollect behaviorCollect = AdvancedDispenserRegistry.BLOCK_COLLECT_REGISTRY.findValue(world, check, state);
            if (!world.isAirBlock(check) || !block.isReplaceable(world, check)) {
                NonNullList<ItemStack> stacks = behaviorCollect.collect(new BlockSourceImpl(world, check));
                InventoryUtils.insert(tile.getWorld(), check, tile.inventory, stacks, false);
            }

        }

    }

}
