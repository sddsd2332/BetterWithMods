package betterwithmods.module.recipes.miniblocks;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.block.recipe.builder.SawRecipeBuilder;
import betterwithmods.common.tile.TileDynamic;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.variants.IBlockVariants;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.JsonUtils;
import betterwithmods.library.utils.MaterialUtil;
import betterwithmods.library.utils.VariantUtils;
import betterwithmods.module.internal.BlockRegistry;
import betterwithmods.module.internal.ItemRegistry;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.AnvilRecipes;
import betterwithmods.module.recipes.miniblocks.client.CamoModel;
import betterwithmods.module.recipes.miniblocks.client.DynamicStateMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MiniBlocks extends Feature {
    private static boolean autoGeneration;
    private static boolean requiresAnvil;
    private static Set<Ingredient> WHITELIST;


    private static ResourceLocation getRecipeRegistry(String modifier, ItemStack output, ItemStack parent) {
        if (parent.getMetadata() > 0)
            return new ResourceLocation(ModLib.MODID, modifier + output.getItem().getRegistryName().getPath() + "_" + parent.getItem().getRegistryName().getPath() + "_" + parent.getMetadata());
        return new ResourceLocation(ModLib.MODID, modifier + output.getItem().getRegistryName().getPath() + "_" + parent.getItem().getRegistryName().getPath());
    }
    private static ResourceLocation getRecipeRegistry(ItemStack output, ItemStack parent) {
        return getRecipeRegistry("", output, parent);
    }


    public static void placeMini(World world, BlockPos pos, DynamicType type, IBlockState parent) {
        Material material = parent.getMaterial();
        Block block = DynblockUtils.getDynamicVariant(type, material);
        world.setBlockState(pos, block.getDefaultState());
        TileDynamic camo = (TileDynamic) world.getTileEntity(pos);
        if (camo != null)
            camo.setState(parent);
    }

    public void createBlocks() {
        for (Material material : MaterialUtil.materials()) {
            String name = MaterialUtil.getMaterialName(material);
            for (DynamicType type : DynamicType.VALUES) {
                DynblockUtils.addDynamicVariant(type, material, name);
            }
        }

        for (BlockDynamic dynamic : DynblockUtils.DYNAMIC_VARIANT_TABLE.values()) {
            BlockRegistry.registerBlock(dynamic.setCreativeTab(BWMCreativeTabs.MINI_BLOCKS), dynamic.createItemBlock().setRegistryName(dynamic.getRegistryName()));
        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onModelRegistry(ModelRegistryEvent event) {
        for (DynamicType type : DynamicType.VALUES) {
            IStateMapper mapper = new DynamicStateMapper(type);
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(ModLib.MODID, type.getName()), "inventory");
            for (BlockDynamic block : DynblockUtils.getTypeBlocks(type)) {
                ModelLoader.setCustomStateMapper(block, mapper);
                Item item = Item.getItemFromBlock(block);
                ItemRegistry.setInventoryModel(item, modelResourceLocation);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {

        EnumMap<DynamicType, Function<IBakedModel, IBakedModel>> modelReplacer = Maps.newEnumMap(DynamicType.class);
        for (DynamicType type : DynamicType.VALUES) {
            modelReplacer.put(type, CamoModel::new);
        }

        for (ModelResourceLocation location : event.getModelRegistry().getKeys()) {
            IBakedModel model = event.getModelRegistry().getObject(location);
            if (model != null) {
                for (Map.Entry<DynamicType, Function<IBakedModel, IBakedModel>> entry : modelReplacer.entrySet()) {
                    if (location.getPath().equals(entry.getKey().getName())) {
                        event.getModelRegistry().putObject(location, entry.getValue().apply(model));
                    }
                }
            }
        }
    }

    public Set<Ingredient> loadMiniblockWhitelist() {
        File file = new File(config().path, "betterwithmods/dynamicblocks.json");

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
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(Blocks.GLASS)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(new ItemStack(BWMBlocks.STEEL_BLOCK)));
            DEFAULT_CONFIG.add(JsonUtils.fromStack(BlockAesthetic.getStack(BlockAesthetic.Type.WHITESTONE)));
            JsonUtils.writeFile(file, DEFAULT_CONFIG);
        }
        JsonObject[] objects = JsonUtils.readerFile(file);
        if (objects != null)
            return Arrays.stream(objects).map(object -> CraftingHelper.getIngredient(object, BetterWithMods.JSON_CONTEXT)).collect(Collectors.toSet());
        return Sets.newHashSet();
    }

    public void registerMiniblocks() {
        WHITELIST = loadMiniblockWhitelist();

        final NonNullList<ItemStack> list = NonNullList.create();

        Iterable<Item> items = autoGeneration
                ? ForgeRegistries.ITEMS
                : WHITELIST.stream().map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(ItemStack::getItem).collect(Collectors.toSet());

        for (Item item : items) {
            if (!(item instanceof ItemBlock))
                continue;
            try {
                final CreativeTabs ctab = item.getCreativeTab();
                if (ctab != null) {
                    item.getSubItems(ctab, list);
                }
                for (final ItemStack stack : list) {
                    if (!(stack.getItem() instanceof ItemBlock))
                        continue;
                    IBlockState state = GlobalUtils.getStateFromStack(stack);
                    if (state != null && DynblockUtils.isValidMini(state, stack)) {
                        Material material = state.getMaterial();
                        if (MaterialUtil.isValid(material)) {
                            DynblockUtils.MATERIAL_VARIANTS.put(material, state);
                        }
                    }
                }
                list.clear();
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        autoGeneration = loadProperty("Auto Generate Miniblocks", false).setComment("Automatically add miniblocks for many blocks, based on heuristics and probably planetary alignments. WARNING: Exposure to this config option can kill pack developers.").get();
        requiresAnvil = loadProperty("Stone Miniblocks require Anvil recipe", true).setComment("When enabled stone and metal miniblocks will require an anvil recipe, when disabled they will all be made with the saw").get();
        createBlocks();
        DynamicType.registerTiles();
    }


    private static ShapedOreRecipe createShapedDynamicRecipe(ItemStack parent, ItemStack dynamicOutput, Object... inputs) {
        return (ShapedOreRecipe) new ShapedOreRecipe(dynamicOutput.getItem().getRegistryName(), dynamicOutput, inputs).setRegistryName(getRecipeRegistry(dynamicOutput, parent));
    }

    private static ShapelessOreRecipe createShapelessDynamicRecipe(ItemStack parent, ItemStack dynamicOutput, Object... inputs ) {
        return (ShapelessOreRecipe) new ShapelessOreRecipe(dynamicOutput.getItem().getRegistryName(), dynamicOutput, inputs).setRegistryName(getRecipeRegistry(dynamicOutput, parent));
    }

    @Override
    public void onRecipesRegistered(RegistryEvent.Register<IRecipe> event) {
        registerMiniblocks();

        for (Material material : MaterialUtil.materials()) {
            BlockDynamic siding = DynblockUtils.getDynamicVariant(DynamicType.SIDING, material);
            BlockDynamic moulding = DynblockUtils.getDynamicVariant(DynamicType.MOULDING, material);
            BlockDynamic corner = DynblockUtils.getDynamicVariant(DynamicType.CORNER, material);

            event.getRegistry().register(new MiniRecipe(siding, null));
            event.getRegistry().register(new MiniRecipe(moulding, siding));
            event.getRegistry().register(new MiniRecipe(corner, moulding));
        }

        SawRecipeBuilder sawBuilder = new SawRecipeBuilder();
        for (IBlockState parent : DynblockUtils.MATERIAL_VARIANTS.values()) {
            ItemStack parentStack = GlobalUtils.getStackFromState(parent);
            Material material = parent.getMaterial();
            MiniBlockIngredient siding = new MiniBlockIngredient("siding", parentStack);
            MiniBlockIngredient moulding = new MiniBlockIngredient("moulding", parentStack);

            ItemStack columnStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.COLUMN, material), parent, 8);
            ItemStack pedestalStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.PEDESTAL, material), parent, 8);
            ItemStack tableStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.TABLE, material), parent, 1);
            ItemStack benchStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.BENCH, material), parent, 1);
            ItemStack chairStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.CHAIR, material), parent, 2);

            ItemStack grateStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.GRATE, material), parent, 2);
            ItemStack slatsStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.SLATS, material), parent, 2);

            ItemStack thinwallStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.THINWALL, material), parent, 4);

            ItemStack reedChimeStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.REED_CHIME, material), parent, 1);
            ItemStack ironChimeStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.IRON_CHIME, material), parent, 1);

            Ingredient stick = new OreIngredient("stickWood");
            Ingredient string = new OreIngredient("string");

            AnvilRecipes.addSteelShapedRecipe(getRecipeRegistry("anvil_", columnStack, parentStack), columnStack, "XX", "XX", "XX", "XX", 'X', moulding);
            AnvilRecipes.addSteelShapedRecipe(getRecipeRegistry("anvil_", parentStack,parentStack), pedestalStack, " XX ", "BBBB", "BBBB", "BBBB", 'X', siding, 'B', parentStack);


            event.getRegistry().registerAll(
                    createShapedDynamicRecipe(parentStack, grateStack,  "MSM", "MSM", 'S', stick, 'M', moulding),
                    createShapedDynamicRecipe(parentStack, slatsStack,  "MM", "MM", 'M', moulding),
                    createShapedDynamicRecipe(parentStack, chairStack, "  S", "SSS", "M M", 'S', siding, 'M', moulding).setMirrored(true),
                    createShapedDynamicRecipe(parentStack, tableStack, "SSS", " M ", " M ", 'S', siding, 'M', moulding).setMirrored(true),
                    createShapedDynamicRecipe(parentStack, benchStack, "SSS", " M ", 'S', siding, 'M', moulding),
                    createShapedDynamicRecipe(parentStack, reedChimeStack, " S ", "SPS", "XMX", 'S', string, 'X', Items.REEDS , 'P', new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), 'M', moulding),
                    createShapedDynamicRecipe(parentStack, ironChimeStack, " S ", "SPS", "XMX", 'S', string, 'X', "nuggetIron", 'P', new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), 'M', moulding),
                    createShapelessDynamicRecipe(parentStack, thinwallStack, siding)
            );

            IBlockVariants blockVariants = VariantUtils.getVariantFromState(IBlockVariants.EnumBlock.BLOCK, parent);
            if (blockVariants != null) {
                ItemStack fence = blockVariants.getStack(IBlockVariants.EnumBlock.FENCE, 2);
                ItemStack fencegate = blockVariants.getStack(IBlockVariants.EnumBlock.FENCE_GATE, 1);
                ItemStack stairs = blockVariants.getStack(IBlockVariants.EnumBlock.STAIR, 1);
                ItemStack wall = blockVariants.getStack(IBlockVariants.EnumBlock.WALL, 3);
                if (!wall.isEmpty())
                    event.getRegistry().register(new ShapedOreRecipe(wall.getItem().getRegistryName(), wall, "SSS", 'S', siding).setRegistryName(getRecipeRegistry(wall, parentStack)));
                if (!stairs.isEmpty())
                    event.getRegistry().register(new ShapedOreRecipe(stairs.getItem().getRegistryName(), stairs, "M ", "MM", 'M', moulding).setMirrored(true).setRegistryName(getRecipeRegistry(stairs, parentStack)));
                if (!fence.isEmpty())
                    event.getRegistry().register(new ShapedOreRecipe(fence.getItem().getRegistryName(), fence, "MMM", 'M', moulding).setRegistryName(getRecipeRegistry(fence, parentStack)));
                if (!fencegate.isEmpty())
                    event.getRegistry().register(new ShapedOreRecipe(fencegate.getItem().getRegistryName(), fencegate, "MSM", 'M', moulding, 'S', siding).setRegistryName(getRecipeRegistry(fencegate, parentStack)));
            }

            if (!requiresAnvil || material == Material.WOOD) {
                MiniBlockIngredient corner = new MiniBlockIngredient("corner", parentStack);
                ItemStack sidingStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.SIDING, material), parent, 2);
                ItemStack mouldingStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.MOULDING, material), parent, 2);
                ItemStack cornerStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.CORNER, material), parent, 2);
                RecipeRegistry.WOOD_SAW.registerAll(
                        sawBuilder.input(parentStack).outputs(sidingStack).build(),
                        sawBuilder.input(siding).outputs(mouldingStack).build(),
                        sawBuilder.input(moulding).outputs(cornerStack).build()
                );

                if (BWMOreDictionary.isOre(parentStack, "plankWood")) {
                    RecipeRegistry.WOOD_SAW.register(sawBuilder.input(corner).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOODEN_GEAR, 2)).build());
                }
            } else {
                ItemStack sidingStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.SIDING, material), parent, 8);
                ItemStack mouldingStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.MOULDING, material), parent, 8);
                ItemStack cornerStack = DynblockUtils.fromParent(DynblockUtils.getDynamicVariant(DynamicType.CORNER, material), parent, 8);

                AnvilRecipes.addSteelShapedRecipe(getRecipeRegistry("anvil_", sidingStack,parentStack), sidingStack, "XXXX", 'X', parentStack);
                AnvilRecipes.addSteelShapedRecipe(getRecipeRegistry("anvil_", mouldingStack,parentStack), mouldingStack, "XXXX", 'X', siding);
                AnvilRecipes.addSteelShapedRecipe(getRecipeRegistry("anvil_", cornerStack,parentStack), cornerStack, "XXXX", 'X', moulding);
            }
        }

    }

    @Override
    public String getDescription() {
        return "Dynamically generate Siding, Mouldings and Corners for many of the blocks in the game.";
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
