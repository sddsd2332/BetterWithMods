package betterwithmods.common.dynamic;

import betterwithmods.BWMod;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.module.ConfigHelper;
import betterwithmods.module.recipes.miniblocks.DynamicVariant;
import betterwithmods.util.JsonUtils;
import betterwithmods.util.ReflectionHelperBlock;
import com.google.common.collect.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BWMDynamicBlocks {
    public static Function<Material, Collection<IBlockState>> MATERIALS_GETTER = BWMDynamicBlocks.MATERIALS::get;

    public static final List<DynamicBlockEntry> DYNAMIC_BLOCK_ENTRIES = Lists.newArrayList();

    public static final HashMap<DynamicVariant, HashMap<Material, BlockCamo>> MINI_MATERIAL_BLOCKS = Maps.newHashMap();
    public static final Multimap<Material, IBlockState> MATERIALS = HashMultimap.create();
    public static final Map<Material, String> MATERIAL_NAMES = Maps.newHashMap();
    public static Set<Ingredient> WHITELIST;

    static {
        for (DynamicVariant type : DynamicVariant.VALUES) {
            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.put(type, Maps.newHashMap());
        }
    }

    static {
        addMaterial(Material.WOOD, "wood");
        addMaterial(Material.ROCK, "rock");
        addMaterial(Material.IRON, "iron");
    }

    public static void addMaterial(Material material, String name) {
        if (!MATERIAL_NAMES.containsKey(material)) //so addons don't overwrite our names, causing world breakage
            MATERIAL_NAMES.put(material, name);
    }

    public static String getMaterialName(Material material) {
        return MATERIAL_NAMES.get(material);
    }


    public static boolean canBeCamo(IBlockState state) {
        Material material = state.getMaterial();
        return MATERIAL_NAMES.containsKey(material) && BWMDynamicBlocks.MATERIALS.get(material).contains(state);
    }


    public static boolean canBeCamo(IBlockState state, ItemStack stack) {
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

    private static Class<?> getDeclaringClass(
            final Class<?> blkClass,
            final String methodName,
            final Class<?>... args) {
        try {
            blkClass.getDeclaredMethod(methodName, args);
            return blkClass;
        } catch (final NoSuchMethodException | SecurityException e) {
            // nothing here...
        } catch (final NoClassDefFoundError e) {
            BWMod.logger.info("Unable to determine blocks eligibility for making a miniblock, " + blkClass.getName() + " attempted to load " + e.getMessage());
            return blkClass;
        } catch (final Throwable t) {
            return blkClass;
        }

        return getDeclaringClass(
                blkClass.getSuperclass(),
                methodName,
                args);
    }

    public static List<ItemStack> getStacks(DynamicVariant type, Material material) {
        List<ItemStack> stacks = Lists.newArrayList();
        Block block = MINI_MATERIAL_BLOCKS.get(type).get(material);
        for (IBlockState state : MATERIALS.get(material)) {
            stacks.add(fromParent(block, state));
        }
        return stacks;
    }

    public static ItemStack fromParent(Block camo, IBlockState state) {
        return fromParent(camo, state, 1);
    }

    public static ItemStack fromParent(Block camo, IBlockState state, int count) {
        ItemStack stack = new ItemStack(camo, count);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound texture = new NBTTagCompound();
        NBTUtil.writeBlockState(texture, state);
        tag.setTag("texture", texture);
        stack.setTagCompound(tag);
        return stack.copy();
    }

    public static Set<Ingredient> loadCamoWhitelist(ConfigHelper config) {
        File file = new File(config.path, "betterwithmods/miniblocks.json");

        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        if (!Files.exists(file.toPath())) {
            JsonArray DEFAULT_CONFIG = new JsonArray();
            DEFAULT_CONFIG.add(JsonUtils.fromOre("plankWood"));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.COBBLESTONE)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.STONE)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.STONEBRICK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.SANDSTONE)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.RED_SANDSTONE)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.PURPUR_BLOCK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.BRICK_BLOCK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.NETHER_BRICK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.QUARTZ_BLOCK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.GOLD_BLOCK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.IRON_BLOCK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITESTONE)));
            JsonUtils.writeFile(file, DEFAULT_CONFIG);
        }
        JsonObject[] objects = JsonUtils.readerFile(file);
        if (objects != null)
            return Arrays.stream(objects).map(object -> CraftingHelper.getIngredient(object, JsonUtils.BWM_CONTEXT)).collect(Collectors.toSet());
        return Sets.newHashSet();
    }


    public static void addDynamicBlock(DynamicBlockEntry entry) {
        DYNAMIC_BLOCK_ENTRIES.add(entry);
    }
}
