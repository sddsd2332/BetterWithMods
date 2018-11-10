package betterwithmods.common.blocks;

import betterwithmods.common.registry.crafting.IngredientTool;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.block.IBlockType;
import betterwithmods.library.common.block.creation.BlockTypeGenerator;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ListUtils;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.util.PlayerUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static betterwithmods.common.blocks.BlockPlanter.Type.*;

public class BlockPlanter extends BlockBase implements IGrowable {
    private final Type type;


    private BlockPlanter(Type type) {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(1.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.type = type;
    }

    public static Set<Block> getAll() {
        return Arrays.stream(Type.VALUES).map(BlockPlanter::getBlock).collect(Collectors.toSet());
    }

    public static Block getBlock(Type type) {
        return ForgeRegistries.BLOCKS.getValue(type.getRegistryName());
    }

    public static ItemStack getStack(Type type) {
        return new ItemStack(getBlock(type));
    }

    private Type getTypeFromStack(ItemStack stack) {
        for (Type type : Type.VALUES) {
            if (type.apply(stack)) {
                return type;
            }
        }
        return null;
    }

    protected static final AxisAlignedBB AXIS_ALIGNED_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AXIS_ALIGNED_BB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = PlayerUtils.getHolding(player, hand);
        Type itemType = getTypeFromStack(heldItem);
        Type newType = type;
        switch (type) {
            case EMPTY:
                if (itemType != null && itemType != EMPTY && itemType != FARMLAND && itemType != FERTILE) {
                    if (world.isRemote)
                        return true;
                    if (player.isCreative() || InventoryUtils.usePlayerItem(player, EnumFacing.UP, heldItem, 1)) {
                        world.playSound(null, pos, itemType == WATER ? SoundEvents.ITEM_BUCKET_EMPTY : itemType.getState().getBlock().getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                        player.swingArm(hand);
                        newType = itemType;
                    }
                }
                break;
            case WATER:
                if (heldItem.isItemEqual(new ItemStack(Items.BUCKET))) {
                    if (world.isRemote)
                        return true;
                    if (InventoryUtils.usePlayerItem(player, EnumFacing.UP, heldItem, 1))
                        InventoryUtils.givePlayer(player, EnumFacing.UP, ListUtils.asNonnullList(new ItemStack(Items.WATER_BUCKET)));
                    player.swingArm(hand);
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                    newType = EMPTY;
                }
                break;

            case GRASS:
            case DIRT:
                if (itemType == FERTILE) {
                    ItemDye.applyBonemeal(heldItem, world, pos, player, hand);
                } else if (itemType == FARMLAND) {
                    heldItem.damageItem(1, player);
                    player.swingArm(hand);
                    world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    newType = itemType;
                    break;
                }
            case SOULSAND:
                break;
            case FERTILE:
            case FARMLAND:
                if (itemType == FERTILE) {
                    if (world.isRemote)
                        return true;
                    if (InventoryUtils.usePlayerItem(player, EnumFacing.UP, heldItem, 1)) {
                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        newType = itemType;
                        world.playEvent(2005, pos.up(), 0);
                    }
                    break;
                }
            case SAND:
            case GRAVEL:
            case REDSAND:
                if (itemType == EMPTY) {
                    if (!player.isCreative()) {
                        InventoryUtils.givePlayer(player, EnumFacing.UP, ListUtils.asNonnullList(GlobalUtils.getStackFromState(type.getState())));
                    }
                    heldItem.damageItem(1, player);
                    world.playSound(null, pos, type.getState().getBlock().getSoundType(state, world, pos, player).getBreakSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                    newType = itemType;
                }
                break;
        }
        world.setBlockState(pos, getBlock(newType).getDefaultState());
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
            BlockPos up = pos.up();

            switch (type) {
                case DIRT:
                    if (world.isAirBlock(up) && world.getLight(up) > 8) {
                        int xP = rand.nextInt(3) - 1;
                        int yP = rand.nextInt(3) - 1;
                        int zP = rand.nextInt(3) - 1;
                        BlockPos checkPos = pos.add(xP, yP, zP);
                        if (world.getBlockState(checkPos).getBlock() == Blocks.GRASS) {
                            world.setBlockState(pos, getBlock(GRASS).getDefaultState());
                        }
                    }
                    break;
                case GRASS:
                    if (world.isAirBlock(up) && rand.nextInt(100) == 0) {
                        world.getBiome(pos).plantFlower(world, rand, up);
                        if (world.getLight(up) > 8) {
                            for (int i = 0; i < 4; i++) {
                                int xP = rand.nextInt(3) - 1;
                                int yP = rand.nextInt(3) - 1;
                                int zP = rand.nextInt(3) - 1;
                                BlockPos checkPos = pos.add(xP, yP, zP);
                                if (world.getBlockState(checkPos).getBlock() == Blocks.DIRT && world.getBlockState(checkPos) == Blocks.DIRT.getDefaultState())
                                    world.setBlockState(checkPos, Blocks.GRASS.getDefaultState());
                            }
                        }
                    }
                case FERTILE:
                    if (world.getBlockState(up).getBlock() instanceof IPlantable) {
                        IPlantable plant = (IPlantable) world.getBlockState(up).getBlock();
                        if (this.canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, plant) && world.getBlockState(up).getBlock().getTickRandomly()) {
                            IBlockState cropState = world.getBlockState(up);
                            if (rand.nextInt(100) == 0)
                                world.getBlockState(up).getBlock().updateTick(world, up, cropState, rand);
                        }
                    }
            }
        }

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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isFertile(@Nonnull World world, @Nonnull BlockPos pos) {
        return type == Type.FERTILE;
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing dir, IPlantable plant) {
        BlockPos up = pos.up();
        EnumPlantType plantType = plant.getPlantType(world, up);
        return dir == EnumFacing.UP && type.isType(plantType);
    }

    @Override
    public void onPlantGrow(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, BlockPos source) {
        if (type == GRASS && source.getY() == pos.getY() + 1)
            world.setBlockState(pos, getBlock(DIRT).getDefaultState());
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face != EnumFacing.UP)
            return face == EnumFacing.DOWN ? BlockFaceShape.CENTER_BIG : BlockFaceShape.UNDEFINED;
        switch (type) {
            case EMPTY:
            case WATER:
                return BlockFaceShape.BOWL;
            default:
                return BlockFaceShape.SOLID;
        }
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return type == GRASS;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return canGrow(worldIn, pos, state, worldIn.isRemote);
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        //Borrowed from BlockGrass
        BlockPos blockpos = pos.up();

        for (int i = 0; i < 128; ++i) {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true) {
                if (j >= i / 16) {
                    if (worldIn.isAirBlock(blockpos1)) {
                        if (rand.nextInt(8) == 0) {
                            worldIn.getBiome(blockpos1).plantFlower(worldIn, rand, blockpos1);
                        } else {
                            IBlockState iblockstate1 = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);

                            if (Blocks.TALLGRASS.canBlockStay(worldIn, blockpos1, iblockstate1)) {
                                worldIn.setBlockState(blockpos1, iblockstate1, 3);
                            }
                        }
                    }

                    break;
                }

                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS || worldIn.getBlockState(blockpos1).isNormalCube()) {
                    break;
                }

                ++j;
            }
        }
    }

    public enum Type implements IBlockType {
        EMPTY("empty", new IngredientTool("shovel"), Blocks.AIR.getDefaultState(), new EnumPlantType[0]),
        FARMLAND("farmland", new IngredientTool(s -> s.getItem() instanceof ItemHoe, ItemStack.EMPTY), Blocks.DIRT.getDefaultState(), new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        GRASS("grass", StackIngredient.fromStacks(new ItemStack(Blocks.GRASS)), Blocks.GRASS.getDefaultState(), new EnumPlantType[]{EnumPlantType.Plains}),
        SOULSAND("soul_sand", StackIngredient.fromStacks(new ItemStack(Blocks.SOUL_SAND)), Blocks.SOUL_SAND.getDefaultState(), new EnumPlantType[]{EnumPlantType.Nether}),
        FERTILE("fertile", StackIngredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())), Blocks.DIRT.getDefaultState(), new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        SAND("sand", StackIngredient.fromStacks(new ItemStack(Blocks.SAND)), Blocks.SAND.getDefaultState(), new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach}),
        WATER("water_still", StackIngredient.fromStacks(new ItemStack(Items.WATER_BUCKET)), Blocks.WATER.getDefaultState(), new EnumPlantType[]{EnumPlantType.Water}),
        GRAVEL("gravel", StackIngredient.fromStacks(new ItemStack(Blocks.GRAVEL)), Blocks.GRAVEL.getDefaultState(), new EnumPlantType[]{EnumPlantType.Cave}),
        REDSAND("red_sand", StackIngredient.fromStacks(new ItemStack(Blocks.SAND, 1, BlockSand.EnumType.RED_SAND.getMetadata())), Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach}),
        DIRT("dirt", StackIngredient.fromStacks(new ItemStack(Blocks.DIRT)), Blocks.DIRT.getDefaultState(), new EnumPlantType[]{EnumPlantType.Plains});

        protected static final Type[] VALUES = values();

        private final String name;
        private final IBlockState state;
        private final EnumPlantType[] type;
        private final Ingredient ingredient;
        private final ResourceLocation registryName;

        Type(String name, Ingredient ingredient, IBlockState state, EnumPlantType[] type) {
            this.name = name;
            this.ingredient = ingredient;
            this.state = state;
            this.type = type;
            this.registryName = new ResourceLocation(ModLib.MODID, "planter_" + getName());
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        public boolean isType(EnumPlantType type) {
            return this.type.length != 0 && Arrays.asList(this.type).contains(type);
        }

        public IBlockState getState() {
            return state;
        }

        public boolean apply(ItemStack stack) {
            return ingredient.apply(stack);
        }

        public ItemStack getStack() {
            return Lists.newArrayList(ingredient.getMatchingStacks()).stream().findFirst().orElse(ItemStack.EMPTY);
        }

        @Override
        public ResourceLocation getRegistryName() {
            return registryName;
        }
    }

    public static class Generator extends BlockTypeGenerator<Type> {

        public Generator() {
            super(Type.VALUES);
        }

        @Override
        public Block createBlock(Type variant) {
            return new BlockPlanter(variant);
        }

    }

}
