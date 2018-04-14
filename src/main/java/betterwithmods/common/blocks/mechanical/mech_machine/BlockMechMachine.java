package betterwithmods.common.blocks.mechanical.mech_machine;

import betterwithmods.BWMod;
import betterwithmods.api.block.IOverpower;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.common.blocks.mechanical.IBlockActive;
import betterwithmods.util.InvUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Arrays;

public abstract class BlockMechMachine extends BWMBlock implements IBlockActive, IOverpower {


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
        if(!world.isRemote) {
            boolean isInventory = Arrays.stream(EnumFacing.VALUES).anyMatch(f -> world.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, f));
            if (!player.isSneaking() && world.getTileEntity(pos) != null && isInventory) {
                player.openGui(BWMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

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
        overpowerSound(world,pos);
        InvUtils.ejectBrokenItems(world,pos.offset(EnumFacing.random(world.rand)),overpowerDrops);
        world.setBlockToAir(pos);
    }

    public enum EnumType implements IStringSerializable {
        MILL(0, "mill", true),
        PULLEY(1, "pulley", true),
        HOPPER(2, "hopper"),
        TURNTABLE(3, "turntable", true);

        private static final BlockMechMachine.EnumType[] META_LOOKUP = values();

        private int meta;
        private String name;
        private boolean solidity;

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
