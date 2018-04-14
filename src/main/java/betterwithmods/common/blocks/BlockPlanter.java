package betterwithmods.common.blocks;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.registry.crafting.IngredientTool;
import betterwithmods.util.InvUtils;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static betterwithmods.common.blocks.BlockPlanter.EnumType.*;

public class BlockPlanter extends BWMBlock {
    public static final HashMap<EnumType, Block> BLOCKS = Maps.newHashMap();
    private final EnumType type;

    private BlockPlanter(EnumType type) {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(1.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.type = type;
        this.setRegistryName(String.format("planter_%s", type.getName()));
    }

    public static void init() {
        for (EnumType type : EnumType.VALUES) {
            BLOCKS.put(type, new BlockPlanter(type));
        }
    }

    public static ItemStack getStack(EnumType type) {
        return new ItemStack(BLOCKS.get(type));
    }

    public int colorMultiplier(IBlockAccess world, BlockPos pos, int tintIndex) {
        return (type == GRASS && tintIndex > -1) ? world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D) : -1;
    }

    private EnumType getTypeFromStack(ItemStack stack) {
        for (EnumType type : EnumType.VALUES) {
            if (type.apply(stack)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = PlayerHelper.getHolding(player, hand);
        EnumType itemType = getTypeFromStack(heldItem);
        EnumType newType = type;
        switch (type) {
            case EMPTY:
                if (itemType != null && itemType != EMPTY && itemType != FARMLAND && itemType != FERTILE) {
                    if (world.isRemote)
                        return true;
                    if (player.isCreative() || InvUtils.usePlayerItem(player, EnumFacing.UP, heldItem, 1)) {
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
                    if (InvUtils.usePlayerItem(player, EnumFacing.UP, heldItem, 1))
                        InvUtils.givePlayer(player, EnumFacing.UP, InvUtils.asNonnullList(new ItemStack(Items.WATER_BUCKET)));
                    player.swingArm(hand);
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                    newType = EMPTY;
                }
                break;

            case GRASS:
            case DIRT:
                if (itemType == FARMLAND) {
                    heldItem.damageItem(1, player);
                    player.swingArm(hand);
                    world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    newType = itemType;
                    break;
                }
            case SOULSAND:
            case FERTILE:
            case FARMLAND:
                if (itemType == FERTILE) {
                    if (world.isRemote)
                        return true;
                    if (InvUtils.usePlayerItem(player, EnumFacing.UP, heldItem, 1)) {
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
                        InvUtils.givePlayer(player, EnumFacing.UP, InvUtils.asNonnullList(BWMRecipes.getStackFromState(type.getState())));
                    }
                    heldItem.damageItem(1, player);
                    world.playSound(null, pos, type.getState().getBlock().getSoundType(state, world, pos, player).getBreakSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                    newType = itemType;
                }
                break;
        }
        world.setBlockState(pos, BLOCKS.get(newType).getDefaultState());
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
                            world.setBlockState(pos, BLOCKS.get(GRASS).getDefaultState());
                        }
                    }
                    break;
                case GRASS:
                    if (world.isAirBlock(up) && rand.nextInt(30) == 0) {
                        world.getBiome(pos).plantFlower(world, rand, up);
                        if (world.getLight(up) > 8) {
                            for (int i = 0; i < 4; i++) {
                                int xP = rand.nextInt(3) - 1;
                                int yP = rand.nextInt(3) - 1;
                                int zP = rand.nextInt(3) - 1;
                                BlockPos checkPos = pos.add(xP, yP, zP);
                                if (world.getBlockState(checkPos) == Blocks.DIRT && world.getBlockState(checkPos) == Blocks.DIRT.getDefaultState())
                                    world.setBlockState(checkPos, Blocks.GRASS.getDefaultState());
                            }
                        }
                    }
                case FERTILE:
                    if (world.getBlockState(up).getBlock() instanceof IPlantable) {
                        IPlantable plant = (IPlantable) world.getBlockState(up).getBlock();
                        if (this.canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, plant) && world.getBlockState(up).getBlock().getTickRandomly()) {
                            IBlockState cropState = world.getBlockState(up);
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
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return type == EnumType.FERTILE;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing dir, IPlantable plant) {
        BlockPos up = pos.up();
        EnumPlantType plantType = plant.getPlantType(world, up);
        return dir == EnumFacing.UP && type.isType(plantType);
    }

    @Override
    public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source) {
        if (type == GRASS && source.getY() == pos.getY() + 1)
            world.setBlockState(pos, BLOCKS.get(DIRT).getDefaultState());
    }

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

    public enum EnumType implements IStringSerializable {
        EMPTY("empty", new IngredientTool("shovel"), Blocks.AIR.getDefaultState(), new EnumPlantType[0]),
        FARMLAND("farmland", new IngredientTool(s -> s.getItem() instanceof ItemHoe, ItemStack.EMPTY), Blocks.DIRT.getDefaultState(), new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        GRASS("grass", Ingredient.fromStacks(new ItemStack(Blocks.GRASS)), Blocks.GRASS.getDefaultState(), new EnumPlantType[]{EnumPlantType.Plains}),
        SOULSAND("soul_sand", Ingredient.fromStacks(new ItemStack(Blocks.SOUL_SAND)), Blocks.SOUL_SAND.getDefaultState(), new EnumPlantType[]{EnumPlantType.Nether}),
        FERTILE("fertile", Ingredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())), Blocks.DIRT.getDefaultState(), new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        SAND("sand", Ingredient.fromStacks(new ItemStack(Blocks.SAND)), Blocks.SAND.getDefaultState(), new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach}),
        WATER("water_still", Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET)), Blocks.WATER.getDefaultState(), new EnumPlantType[]{EnumPlantType.Water}),
        GRAVEL("gravel", Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)), Blocks.GRAVEL.getDefaultState(), new EnumPlantType[]{EnumPlantType.Cave}),
        REDSAND("red_sand", Ingredient.fromStacks(new ItemStack(Blocks.SAND, 1, BlockSand.EnumType.RED_SAND.getMetadata())), Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach}),
        DIRT("dirt", Ingredient.fromStacks(new ItemStack(Blocks.DIRT)), Blocks.DIRT.getDefaultState(), new EnumPlantType[]{EnumPlantType.Plains});

        private static final EnumType[] VALUES = values();

        private String name;
        private IBlockState state;
        private EnumPlantType[] type;
        private Ingredient ingredient;

        EnumType(String name, Ingredient ingredient, IBlockState state, EnumPlantType[] type) {
            this.name = name;
            this.ingredient = ingredient;
            this.state = state;
            this.type = type;
        }

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
    }
}
