package betterwithmods.module.recipes.miniblocks;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.library.utils.MaterialUtil;
import betterwithmods.library.utils.ReflectionHelperBlock;
import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DynblockUtils {

    public static final Table<DynamicType, Material, BlockDynamic> DYNAMIC_VARIANT_TABLE = HashBasedTable.create();
    public static final Multimap<Material, IBlockState> MATERIAL_VARIANTS = HashMultimap.create();

    public static Collection<ItemStack> getStacks(DynamicType type, Material material) {
        List<ItemStack> stacks = Lists.newArrayList();
        Block block = getDynamicVariant(type, material);
        for (IBlockState state : MATERIAL_VARIANTS.get(material)) {
            stacks.add(fromParent(block, state));
        }
        return stacks;
    }

    public static boolean isValidMini(IBlockState state) {
        Material material = state.getMaterial();
        return MaterialUtil.isValid(material) && MATERIAL_VARIANTS.get(material).contains(state);
    }

    public static boolean isValidMini(IBlockState state, ItemStack stack) {
        Block blk = state.getBlock();
        final ReflectionHelperBlock pb = new ReflectionHelperBlock();
        final Class<? extends Block> blkClass = blk.getClass();

        pb.onBlockActivated(null, null, null, null, null, null, 0, 0, 0);
        boolean noActivation = (getDeclaringClass(blkClass, pb.MethodName, World.class, BlockPos.class, IBlockState.class, EntityPlayer.class, EnumHand.class, EnumFacing.class, float.class, float.class, float.class) == Block.class);

        pb.updateTick(null, null, null, null);
        boolean noUpdate = getDeclaringClass(blkClass, pb.MethodName, World.class, BlockPos.class, IBlockState.class, Random.class) == Block.class;

        // ignore blocks with custom collision.
        pb.onEntityCollision(null, null, null, null);
        boolean noCustomCollision = getDeclaringClass(blkClass, pb.MethodName, World.class, BlockPos.class, IBlockState.class, Entity.class) == Block.class;
        final boolean isFullBlock = state.isFullBlock() || blkClass == BlockStainedGlass.class || blkClass == BlockGlass.class || blk == Blocks.SLIME_BLOCK || blk == Blocks.ICE;
        final boolean hasItem = Item.getItemFromBlock(blk) != Items.AIR;
        final boolean tickingBehavior = blk.getTickRandomly();
        final boolean isOre = BWMOreDictionary.hasPrefix(stack, "ore") || BWMOreDictionary.isOre(stack, "logWood");

        boolean hasBehavior = (blk.hasTileEntity(state) || tickingBehavior) && blkClass != BlockGrass.class && blkClass != BlockIce.class;

        return noUpdate && noActivation && noCustomCollision && isFullBlock && !hasBehavior && hasItem && !isOre;
    }

    public static ItemStack fromParent(Block mini, IBlockState state) {
        return fromParent(mini, state, 1);
    }

    public static ItemStack fromParent(Block mini, IBlockState state, int count) {
        ItemStack stack = new ItemStack(mini, count);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound texture = new NBTTagCompound();
        NBTUtil.writeBlockState(texture, state);
        tag.setTag("texture", texture);
        stack.setTagCompound(tag);
        return stack.copy();
    }

    public static Class<?> getDeclaringClass(
            final Class<?> blkClass,
            final String methodName,
            final Class<?>... args) {
        try {
            blkClass.getDeclaredMethod(methodName, args);
            return blkClass;
        } catch (final NoSuchMethodException | SecurityException e) {
            // nothing here...
        } catch (final NoClassDefFoundError e) {
            BetterWithMods.LOGGER.info("Unable to determine blocks eligibility for making a miniblock, " + blkClass.getName() + " attempted to load " + e.getMessage());
            return blkClass;
        } catch (final Throwable t) {
            return blkClass;
        }

        return getDeclaringClass(
                blkClass.getSuperclass(),
                methodName,
                args);
    }


    static void addDynamicVariant(DynamicType type, Material material, BlockDynamic block) {
        DYNAMIC_VARIANT_TABLE.put(type, material, block);
    }

    static BlockDynamic getDynamicVariant(DynamicType type, Material material) {
        return DYNAMIC_VARIANT_TABLE.get(type, material);
    }
}
