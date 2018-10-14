package betterwithmods.common.blocks.mechanical.mech_machine;

import betterwithmods.BetterWithMods;
import betterwithmods.api.block.IOverpower;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.block.IBlockActive;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

public abstract class BlockMechMachine extends BlockBase implements IBlockActive, IOverpower {


    private final ResourceLocation overpowerDrops;

    public BlockMechMachine(Material material, ResourceLocation overpowerDrops) {
        super(material);
        this.overpowerDrops = overpowerDrops;
        this.setTickRandomly(true);
        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public int tickRate(World worldIn) {
        return 10;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking() && !world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && CapabilityUtils.hasInventory(tile, null)) {
                player.openGui(BetterWithMods.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ACTIVE, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        if (doesOverpower()) {
            overpowerSound(world, pos);
            InventoryUtils.ejectBrokenItems(world, pos.offset(EnumFacing.random(world.rand)), overpowerDrops);
            world.setBlockToAir(pos);
        }
    }



    public enum EnumType implements IStringSerializable {
        MILL(0, "mill", true),
        PULLEY(1, "pulley", true),
        HOPPER(2, "hopper"),
        TURNTABLE(3, "turntable", true);

        private static final BlockMechMachine.EnumType[] META_LOOKUP = values();

        private final int meta;
        private final String name;
        private final boolean solidity;

        EnumType(int meta, String name) {
            this(meta, name, false);
        }

        EnumType(int meta, String name, boolean solid) {
            this.meta = meta;
            this.name = name;
            this.solidity = solid;
        }

        public static BlockMechMachine.EnumType byMeta(int meta) {
            return META_LOOKUP[meta % META_LOOKUP.length];
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

        public boolean getSolidity() {
            return solidity;
        }
    }
}
