package betterwithmods.common.blocks;

import betterwithmods.api.tile.dispenser.IBehaviorCollect;
import betterwithmods.api.tile.dispenser.IBehaviorEntity;
import betterwithmods.common.blocks.behaviors.BehaviorBreakBlock;
import betterwithmods.common.blocks.behaviors.BehaviorDefaultDispenseBlock;
import betterwithmods.common.blocks.behaviors.BehaviorEntity;
import betterwithmods.common.tile.TileAdvancedDispenser;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class OldBlockBDispenser extends BlockDispenser {
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> BLOCK_DISPENSER_REGISTRY = new RegistryDefaulted<>(new BehaviorDefaultDispenseBlock());
    public static final RegistryDefaulted<Block, IBehaviorCollect> BLOCK_COLLECT_REGISTRY = new RegistryDefaulted<>(new BehaviorBreakBlock());
    public static final RegistryDefaulted<ResourceLocation, IBehaviorEntity> ENTITY_COLLECT_REGISTRY = new RegistryDefaulted<>(new BehaviorEntity());

    public OldBlockBDispenser() {
        super();

        this.setHardness(3.5F);
        this.setHarvestLevel("pickaxe", 0);
        this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, false));
    }

    @Override
    public int tickRate(World worldIn) {
        return 1;
    }

    private boolean isValidEntity(Entity entity) {
        if (entity != null) {
            ResourceLocation key = EntityList.getKey(entity);
            if (key != null)
                return ENTITY_COLLECT_REGISTRY.containsKey(key) && entity.isEntityAlive();
        }
        return false;
    }

    private Optional<Entity> getEntity(World world, BlockPos pos) {
        return world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)), this::isValidEntity).stream().findFirst();
    }

    @Override
    protected void dispense(@Nonnull World world, @Nonnull BlockPos pos) {
        BlockSourceImpl impl = new BlockSourceImpl(world, pos);
        TileAdvancedDispenser tile = impl.getBlockTileEntity();
        if (!world.getBlockState(pos).getValue(TRIGGERED)) {
            BlockPos check = pos.offset(impl.getBlockState().getValue(FACING));
            Block block = world.getBlockState(check).getBlock();

            if (world.getBlockState(check).getBlockHardness(world, check) < 0)
                return;

            Optional<Entity> entity = getEntity(world, check);
            if (entity.isPresent()) {
                Entity e = entity.get();
                ResourceLocation name = EntityList.getKey(e);
                IBehaviorEntity behaviorEntity = ENTITY_COLLECT_REGISTRY.getObject(name);
                NonNullList<ItemStack> stacks = behaviorEntity.collect(world, check, e, tile.getCurrentSlot());
                InventoryUtils.insert(tile.getWorld(), check, tile.inventory, stacks, false);
                return;
            }

            IBehaviorCollect behavior = BLOCK_COLLECT_REGISTRY.getObject(block);
            if (!world.isAirBlock(check) || !block.isReplaceable(world, check)) {
                NonNullList<ItemStack> stacks = behavior.collect(new BlockSourceImpl(world, check));
                InventoryUtils.insert(tile.getWorld(), check, tile.inventory, stacks, false);
            }
        } else {
            int index = tile.nextIndex;
            ItemStack stack = tile.getNextStackFromInv();
            if (index == -1 || stack.isEmpty())
                world.playEvent(1001, pos, 0);
            else {
                IBehaviorDispenseItem behavior = this.getBehavior(stack);
                behavior.dispense(impl, stack);
            }
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

    @Nonnull
    @Override
    protected IBehaviorDispenseItem getBehavior(@Nullable ItemStack stack) {
        return BLOCK_DISPENSER_REGISTRY.getObject(stack == null ? null : stack.getItem());
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAdvancedDispenser();
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, @Nonnull BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return CapabilityUtils.getInventory(tile, null).map(InventoryUtils::calculateComparatorLevel).orElse(0);
    }
}


