package betterwithmods.module.recipes.miniblocks;

import betterwithmods.BetterWithMods;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.block.recipe.builder.SawRecipeBuilder;
import betterwithmods.common.tile.TileCamo;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.variants.IBlockVariants;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.JsonUtils;
import betterwithmods.library.utils.MaterialUtil;
import betterwithmods.library.utils.VariantUtils;
import betterwithmods.module.internal.BlockRegistry;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.AnvilRecipes;
import betterwithmods.module.recipes.miniblocks.blocks.*;
import betterwithmods.module.recipes.miniblocks.client.CamoModel;
import betterwithmods.module.recipes.miniblocks.client.MiniModel;
import betterwithmods.module.recipes.miniblocks.client.StairModel;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;


public class MiniBlocks extends Feature {
    public static final HashMap<MiniType, HashMap<Material, BlockCamo>> MINI_MATERIAL_BLOCKS = Maps.newHashMap();
    public static final Multimap<Material, IBlockState> MATERIAL_VARIANTS = HashMultimap.create();
    private static boolean autoGeneration;
    private static boolean requiresAnvil;
    private static Set<Ingredient> WHITELIST;


    static {
        for (MiniType type : MiniType.VALUES) {
            MINI_MATERIAL_BLOCKS.put(type, Maps.newHashMap());
        }
    }


    public static void forceMiniBlock(Ingredient ingredient) {
        WHITELIST.add(ingredient);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(IRegistry<ModelResourceLocation, IBakedModel> registry, String name, IBakedModel model) {
        registerModel(registry, name, model, Sets.newHashSet("normal", "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(IRegistry<ModelResourceLocation, IBakedModel> registry, String name, IBakedModel model, Set<String> variants) {
        for (String variant : variants) {
            registry.putObject(new ModelResourceLocation(ModLib.MODID + ":" + name, variant), model);
        }
    }

    private static ResourceLocation getRecipeRegistry(ItemStack output, ItemStack parent) {
        if (parent.getMetadata() > 0)
            return new ResourceLocation(ModLib.MODID, output.getItem().getRegistryName().getPath() + "_" + parent.getItem().getRegistryName().getPath() + "_" + parent.getMetadata());
        return new ResourceLocation(ModLib.MODID, output.getItem().getRegistryName().getPath() + "_" + parent.getItem().getRegistryName().getPath());
    }

    public static void placeMini(World world, BlockPos pos, MiniType type, IBlockState parent) {
        Material material = parent.getMaterial();
        Block block = MINI_MATERIAL_BLOCKS.get(type).get(material);
        world.setBlockState(pos, block.getDefaultState());
        TileCamo camo = (TileCamo) world.getTileEntity(pos);
        if (camo != null)
            camo.setState(parent);
    }

    public void createBlocks() {
        for (Material material : MaterialUtil.materials()) {
            String name = MaterialUtil.getMaterialName(material);
            MINI_MATERIAL_BLOCKS.get(MiniType.SIDING).put(material, (BlockMini) new BlockSiding(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "siding", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.MOULDING).put(material, (BlockMini) new BlockMoulding(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "moulding", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.CORNER).put(material, (BlockMini) new BlockCorner(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "corner", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.COLUMN).put(material, (BlockMini) new BlockColumn(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "column", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.PEDESTAL).put(material, (BlockMini) new BlockPedestals(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "pedestal", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.STAIR).put(material, (BlockMini) new BlockStair(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "stair", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.TABLE).put(material, (BlockCamo) new BlockTable(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "table", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.BENCH).put(material, (BlockCamo) new BlockBench(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "bench", name)));
            MINI_MATERIAL_BLOCKS.get(MiniType.CHAIR).put(material, (BlockCamo) new BlockChair(material, MATERIAL_VARIANTS::get).setRegistryName(String.format("%s_%s", "chair", name)));
        }

        for (MiniType type : MiniType.VALUES) {
            for (BlockCamo mini : MINI_MATERIAL_BLOCKS.get(type).values()) {
                //TODO change so a generator can be used
                BlockRegistry.registerBlock(mini.setCreativeTab(BWMCreativeTabs.MINI_BLOCKS), mini.createItemBlock(mini).setRegistryName(mini.getRegistryName()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {
        MiniModel.SIDING = new MiniModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/siding")));
        MiniModel.MOULDING = new MiniModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/moulding")));
        MiniModel.CORNER = new MiniModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/corner")));
        MiniModel.COLUMN = new MiniModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/column")));
        MiniModel.PEDESTAL = new MiniModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/pedestal")));
        MiniModel.STAIR = new StairModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/stair")),
                RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/mini/stair_inner_corner")));
        MiniModel.CHAIR = new MiniModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/chair")));

        CamoModel.TABLE_SUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/table_supported")));
        CamoModel.TABLE_UNSUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/table_unsupported")));

        CamoModel.BENCH_SUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/bench_supported")));
        CamoModel.BENCH_UNSUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "block/bench_unsupported")));

        for (Material material : MaterialUtil.materials()) {
            String name = MaterialUtil.getMaterialName(material);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "siding", name), MiniModel.SIDING);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "moulding", name), MiniModel.MOULDING);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "corner", name), MiniModel.CORNER);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "column", name), MiniModel.COLUMN);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "pedestal", name), MiniModel.PEDESTAL);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "stair", name), MiniModel.STAIR);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "chair", name), MiniModel.CHAIR);
            registerModel(event.getModelRegistry(), String.format("%s_%s", "table", name), CamoModel.TABLE_SUPPORTED, Sets.newHashSet("normal", "inventory", "supported=true"));
            registerModel(event.getModelRegistry(), String.format("%s_%s", "table", name), CamoModel.TABLE_UNSUPPORTED, Sets.newHashSet("supported=false"));
            registerModel(event.getModelRegistry(), String.format("%s_%s", "bench", name), CamoModel.BENCH_SUPPORTED, Sets.newHashSet("normal", "inventory", "supported=true"));
            registerModel(event.getModelRegistry(), String.format("%s_%s", "bench", name), CamoModel.BENCH_UNSUPPORTED, Sets.newHashSet("supported=false"));

        }
    }

    public Set<Ingredient> loadMiniblockWhitelist() {
        File file = new File(config().path, "betterwithmods/miniblocks.json");

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

        Iterable<Item> items = ForgeRegistries.ITEMS;
        if (!autoGeneration)
            items = WHITELIST.stream().map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(ItemStack::getItem).collect(Collectors.toSet());

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
                            MATERIAL_VARIANTS.put(material, state);
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
        MiniType.registerTiles();
    }

    @Override
    public void onRecipesRegistered(RegistryEvent.Register<IRecipe> event) {
        registerMiniblocks();

        BWMOreDictionary.registerOre("miniblocks",
                new ItemStack(MiniBlocks.MINI_MATERIAL_BLOCKS.get(MiniType.SIDING).get(Material.WOOD)),
                new ItemStack(MiniBlocks.MINI_MATERIAL_BLOCKS.get(MiniType.MOULDING).get(Material.WOOD)),
                new ItemStack(MiniBlocks.MINI_MATERIAL_BLOCKS.get(MiniType.CORNER).get(Material.WOOD)));

        for (Material material : MaterialUtil.materials()) {
            BlockCamo siding = MINI_MATERIAL_BLOCKS.get(MiniType.SIDING).get(material);
            BlockCamo moulding = MINI_MATERIAL_BLOCKS.get(MiniType.MOULDING).get(material);
            BlockCamo corner = MINI_MATERIAL_BLOCKS.get(MiniType.CORNER).get(material);

            event.getRegistry().register(new MiniRecipe(siding, null));
            event.getRegistry().register(new MiniRecipe(moulding, siding));
            event.getRegistry().register(new MiniRecipe(corner, moulding));
        }

        SawRecipeBuilder sawBuilder = new SawRecipeBuilder();
        for (IBlockState parent : MATERIAL_VARIANTS.values()) {
            ItemStack parentStack = GlobalUtils.getStackFromState(parent);
            Material material = parent.getMaterial();
            MiniBlockIngredient siding = new MiniBlockIngredient("siding", parentStack);
            MiniBlockIngredient moulding = new MiniBlockIngredient("moulding", parentStack);

            ItemStack columnStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.COLUMN).get(material), parent, 8);
            ItemStack pedestalStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.PEDESTAL).get(material), parent, 8);
            ItemStack tableStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.TABLE).get(material), parent, 1);
            ItemStack benchStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.BENCH).get(material), parent, 1);
            ItemStack chairStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.CHAIR).get(material), parent, 2);

            AnvilRecipes.addSteelShapedRecipe(columnStack.getItem().getRegistryName(), columnStack, "XX", "XX", "XX", "XX", 'X', moulding);
            AnvilRecipes.addSteelShapedRecipe(pedestalStack.getItem().getRegistryName(), pedestalStack, " XX ", "BBBB", "BBBB", "BBBB", 'X', siding, 'B', parentStack);

            event.getRegistry().register(new ShapedOreRecipe(chairStack.getItem().getRegistryName(), chairStack, "  S", "SSS", "M M", 'S', siding, 'M', moulding).setMirrored(true).setRegistryName(getRecipeRegistry(chairStack, parentStack)));
            event.getRegistry().register(new ShapedOreRecipe(tableStack.getItem().getRegistryName(), tableStack, "SSS", " M ", " M ", 'S', siding, 'M', moulding).setRegistryName(getRecipeRegistry(tableStack, parentStack)));
            event.getRegistry().register(new ShapedOreRecipe(benchStack.getItem().getRegistryName(), benchStack, "SSS", " M ", 'S', siding, 'M', moulding).setRegistryName(getRecipeRegistry(benchStack, parentStack)));

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
                ItemStack sidingStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.SIDING).get(material), parent, 2);
                ItemStack mouldingStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.MOULDING).get(material), parent, 2);
                ItemStack cornerStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.CORNER).get(material), parent, 2);
                RecipeRegistry.WOOD_SAW.registerAll(
                        sawBuilder.input(parentStack).outputs(sidingStack).build(),
                        sawBuilder.input(siding).outputs(mouldingStack).build(),
                        sawBuilder.input(moulding).outputs(cornerStack).build()
                );

                if (BWMOreDictionary.isOre(parentStack, "plankWood")) {
                    RecipeRegistry.WOOD_SAW.register(sawBuilder.input(corner).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOODEN_GEAR, 2)).build());
                }
            } else {
                ItemStack sidingStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.SIDING).get(material), parent, 8);
                ItemStack mouldingStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.MOULDING).get(material), parent, 8);
                ItemStack cornerStack = DynblockUtils.fromParent(MINI_MATERIAL_BLOCKS.get(MiniType.CORNER).get(material), parent, 8);

                AnvilRecipes.addSteelShapedRecipe(sidingStack.getItem().getRegistryName(), sidingStack, "XXXX", 'X', parentStack);
                AnvilRecipes.addSteelShapedRecipe(mouldingStack.getItem().getRegistryName(), mouldingStack, "XXXX", 'X', siding);
                AnvilRecipes.addSteelShapedRecipe(cornerStack.getItem().getRegistryName(), cornerStack, "XXXX", 'X', moulding);
            }
        }

    }

    @Override
    public String getDescription() {
        return "Dynamically generate Siding, Mouldings and Corners for many of the blocks in the game.";
    }


}
