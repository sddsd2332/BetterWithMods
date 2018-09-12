package betterwithmods.module.recipes.miniblocks;

import betterwithmods.BWMod;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.dynamic.BWMDynamicBlocks;
import betterwithmods.common.dynamic.DynamicBlockEntry;
import betterwithmods.common.dynamic.DynamicBlockEntryBuilder;
import betterwithmods.module.Feature;
import betterwithmods.module.recipes.miniblocks.client.CamoModel;
import betterwithmods.module.recipes.miniblocks.client.MiniModel;
import betterwithmods.module.recipes.miniblocks.client.StairModel;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class MiniBlocks extends Feature {
    private static boolean autoGeneration;
    private static boolean requiresAnvil;


    public static void forceMiniBlock(Ingredient ingredient) {
        BWMDynamicBlocks.WHITELIST.add(ingredient);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(IRegistry<ModelResourceLocation, IBakedModel> registry, String name, IBakedModel model) {
        registerModel(registry, name, model, Sets.newHashSet("normal", "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(IRegistry<ModelResourceLocation, IBakedModel> registry, String name, IBakedModel model, Set<String> variants) {
        for (String variant : variants) {
            registry.putObject(new ModelResourceLocation(BWMod.MODID + ":" + name, variant), model);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void beforeBlockRegister(RegistryEvent.Register<Block> event) {

        Set<Material> materials = BWMDynamicBlocks.MATERIAL_NAMES.keySet();
        BWMDynamicBlocks.addDynamicBlock(DynamicBlockEntryBuilder.create().variant(DynamicVariant.SIDING).materials(materials).build());
        BWMDynamicBlocks.addDynamicBlock(DynamicBlockEntryBuilder.create().variant(DynamicVariant.MOULDING).materials(materials).build());
        BWMDynamicBlocks.addDynamicBlock(DynamicBlockEntryBuilder.create().variant(DynamicVariant.CORNER).materials(materials).build());

        for (DynamicBlockEntry entry : BWMDynamicBlocks.DYNAMIC_BLOCK_ENTRIES) {
            entry.registerBlocks();
        }


//        for (Material material : BWMDynamicBlocks.MATERIAL_NAMES.keySet()) {
//            BWMDynamicBlocks.registerDynamicVariant(DynamicVariant.SIDING, material, BlockSiding.class);
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.SIDING).put(material, (BlockMini) new BlockSiding(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "siding", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.MOULDING).put(material, (BlockMini) new BlockMoulding(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "moulding", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CORNER).put(material, (BlockMini) new BlockCorner(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "corner", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.COLUMN).put(material, (BlockMini) new BlockColumn(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "column", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.PEDESTAL).put(material, (BlockMini) new BlockPedestals(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "pedestal", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.STAIR).put(material, (BlockMini) new BlockStair(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "stair", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.TABLE).put(material, (BlockCamo) new BlockTable(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "table", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.BENCH).put(material, (BlockCamo) new BlockBench(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "bench", name)));
//            BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CHAIR).put(material, (BlockCamo) new BlockChair(material, BWMDynamicBlocks.MATERIALS::get).setRegistryName(String.format("%s_%s", "chair", name)));
//        }

//        for (DynamicVariant type : DynamicVariant.VALUES) {
//            for (BlockCamo mini : BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(type).values()) {
//                BWMBlocks.registerBlock(mini, mini.createItemBlock(mini));
//            }
//        }
    }

    private static ResourceLocation getRecipeRegistry(ItemStack output, ItemStack parent) {
        if (parent.getMetadata() > 0)
            return new ResourceLocation(BWMod.MODID, output.getItem().getRegistryName().getPath() + "_" + parent.getItem().getRegistryName().getPath() + "_" + parent.getMetadata());
        return new ResourceLocation(BWMod.MODID, output.getItem().getRegistryName().getPath() + "_" + parent.getItem().getRegistryName().getPath());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onPostBake(ModelBakeEvent event) {
        
        MiniModel.SIDING = new MiniModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/siding")));
        MiniModel.MOULDING = new MiniModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/moulding")));
        MiniModel.CORNER = new MiniModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/corner")));
        MiniModel.COLUMN = new MiniModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/column")));
        MiniModel.PEDESTAL = new MiniModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/pedestal")));
        MiniModel.STAIR = new StairModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/stair")),
                RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/mini/stair_inner_corner")));
        MiniModel.CHAIR = new MiniModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/chair")));

        CamoModel.TABLE_SUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/table_supported")));
        CamoModel.TABLE_UNSUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/table_unsupported")));

        CamoModel.BENCH_SUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/bench_supported")));
        CamoModel.BENCH_UNSUPPORTED = new CamoModel(RenderUtils.getModel(new ResourceLocation(BWMod.MODID, "block/bench_unsupported")));

        for (Material material : BWMDynamicBlocks.MATERIAL_NAMES.keySet()) {
            String name = BWMDynamicBlocks.MATERIAL_NAMES.get(material);
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

    public void registerMiniblocks() {
        BWMDynamicBlocks.WHITELIST = BWMDynamicBlocks.loadCamoWhitelist(config());

        final NonNullList<ItemStack> list = NonNullList.create();

        Iterable<Item> items = ForgeRegistries.ITEMS;
        if (!autoGeneration)
            items = BWMDynamicBlocks.WHITELIST.stream().map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(ItemStack::getItem).collect(Collectors.toSet());

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
                    IBlockState state = BWMRecipes.getStateFromStack(stack);
                    if (state != null && BWMDynamicBlocks.canBeCamo(state, stack)) {
                        Material material = state.getMaterial();
                        if (BWMDynamicBlocks.MATERIAL_NAMES.containsKey(material)) {
                            BWMDynamicBlocks.MATERIALS.put(material, state);
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
        DynamicVariant.registerTiles();
    }

    @Override
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        registerMiniblocks();
//
//        BWMOreDictionary.registerOre("miniblocks",
//                new ItemStack(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.SIDING).get(Material.WOOD)),
//                new ItemStack(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.MOULDING).get(Material.WOOD)),
//                new ItemStack(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CORNER).get(Material.WOOD)));
//
//        for (Material material : BWMDynamicBlocks.MATERIAL_NAMES.keySet()) {
//            BlockCamo siding = BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.SIDING).get(material);
//            BlockCamo moulding = BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.MOULDING).get(material);
//            BlockCamo corner = BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CORNER).get(material);
//
//            event.getRegistry().registerBlocks(new MiniRecipe(siding, null));
//            event.getRegistry().registerBlocks(new MiniRecipe(moulding, siding));
//            event.getRegistry().registerBlocks(new MiniRecipe(corner, moulding));
//        }
//
//        for (IBlockState parent : BWMDynamicBlocks.MATERIALS.values()) {
//            ItemStack parentStack = BWMRecipes.getStackFromState(parent);
//            Material material = parent.getMaterial();
//            MiniBlockIngredient siding = new MiniBlockIngredient("siding", parentStack);
//            MiniBlockIngredient moulding = new MiniBlockIngredient("moulding", parentStack);
//
//            ItemStack columnStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.COLUMN).get(material), parent, 8);
//            ItemStack pedestalStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.PEDESTAL).get(material), parent, 8);
//            ItemStack tableStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.TABLE).get(material), parent, 1);
//            ItemStack benchStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.BENCH).get(material), parent, 1);
//            ItemStack chairStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CHAIR).get(material), parent, 2);
//
//            AnvilRecipes.addSteelShapedRecipe(columnStack.getItem().getRegistryName(), columnStack, "XX", "XX", "XX", "XX", 'X', moulding);
//            AnvilRecipes.addSteelShapedRecipe(pedestalStack.getItem().getRegistryName(), pedestalStack, " XX ", "BBBB", "BBBB", "BBBB", 'X', siding, 'B', parentStack);
//
//            event.getRegistry().registerBlocks(new ShapedOreRecipe(chairStack.getItem().getRegistryName(), chairStack, "  S", "SSS", "M M", 'S', siding, 'M', moulding).setMirrored(true).setRegistryName(getRecipeRegistry(chairStack, parentStack)));
//            event.getRegistry().registerBlocks(new ShapedOreRecipe(tableStack.getItem().getRegistryName(), tableStack, "SSS", " M ", " M ", 'S', siding, 'M', moulding).setRegistryName(getRecipeRegistry(tableStack, parentStack)));
//            event.getRegistry().registerBlocks(new ShapedOreRecipe(benchStack.getItem().getRegistryName(), benchStack, "SSS", " M ", 'S', siding, 'M', moulding).setRegistryName(getRecipeRegistry(benchStack, parentStack)));
//
//            IBlockVariants blockVariants = BWMOreDictionary.getVariantFromState(IBlockVariants.EnumBlock.BLOCK, parent);
//            if (blockVariants != null) {
//                ItemStack fence = blockVariants.getVariant(IBlockVariants.EnumBlock.FENCE, 2);
//                ItemStack fencegate = blockVariants.getVariant(IBlockVariants.EnumBlock.FENCE_GATE, 1);
//                ItemStack stairs = blockVariants.getVariant(IBlockVariants.EnumBlock.STAIR, 1);
//                ItemStack wall = blockVariants.getVariant(IBlockVariants.EnumBlock.WALL, 3);
//                if (!wall.isEmpty())
//                    event.getRegistry().registerBlocks(new ShapedOreRecipe(wall.getItem().getRegistryName(), wall, "SSS", 'S', siding).setRegistryName(getRecipeRegistry(wall, parentStack)));
//                if (!stairs.isEmpty())
//                    event.getRegistry().registerBlocks(new ShapedOreRecipe(stairs.getItem().getRegistryName(), stairs, "M ", "MM", 'M', moulding).setMirrored(true).setRegistryName(getRecipeRegistry(stairs, parentStack)));
//                if (!fence.isEmpty())
//                    event.getRegistry().registerBlocks(new ShapedOreRecipe(fence.getItem().getRegistryName(), fence, "MMM", 'M', moulding).setRegistryName(getRecipeRegistry(fence, parentStack)));
//                if (!fencegate.isEmpty())
//                    event.getRegistry().registerBlocks(new ShapedOreRecipe(fencegate.getItem().getRegistryName(), fencegate, "MSM", 'M', moulding, 'S', siding).setRegistryName(getRecipeRegistry(fencegate, parentStack)));
//            }
//
//            if (!requiresAnvil || material == Material.WOOD) {
//                MiniBlockIngredient corner = new MiniBlockIngredient("corner", parentStack);
//                ItemStack sidingStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.SIDING).get(material), parent, 2);
//                ItemStack mouldingStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.MOULDING).get(material), parent, 2);
//                ItemStack cornerStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CORNER).get(material), parent, 2);
//                BWMRegistry.WOOD_SAW.addRecipe(parentStack, sidingStack);
//                BWMRegistry.WOOD_SAW.addRecipe(siding, mouldingStack);
//                BWMRegistry.WOOD_SAW.addRecipe(moulding, cornerStack);
//                if (BWMOreDictionary.isOre(parentStack, "plankWood")) {
//                    BWMRegistry.WOOD_SAW.addRecipe(corner, ItemMaterial.getStack(ItemMaterial.EnumMaterial.WOOD_GEAR, 2));
//                }
//            } else {
//                ItemStack sidingStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.SIDING).get(material), parent, 8);
//                ItemStack mouldingStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.MOULDING).get(material), parent, 8);
//                ItemStack cornerStack = BWMDynamicBlocks.fromParent(BWMDynamicBlocks.MINI_MATERIAL_BLOCKS.get(DynamicVariant.CORNER).get(material), parent, 8);
//
//                AnvilRecipes.addSteelShapedRecipe(sidingStack.getItem().getRegistryName(), sidingStack, "XXXX", 'X', parentStack);
//                AnvilRecipes.addSteelShapedRecipe(mouldingStack.getItem().getRegistryName(), mouldingStack, "XXXX", 'X', siding);
//                AnvilRecipes.addSteelShapedRecipe(cornerStack.getItem().getRegistryName(), cornerStack, "XXXX", 'X', moulding);
//            }
//        }

    }

    @Override
    public String getDescription() {
        return "Dynamically generate Siding, Mouldings and Corners for many of the blocks in the game.";
    }

}
