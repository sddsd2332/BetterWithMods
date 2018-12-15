package betterwithmods.module.conversion;

import betterwithmods.client.tesr.TESRFurnaceContent;
import betterwithmods.common.blocks.BlockFurnace;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.creation.BlockEntryBuilderFactory;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.LocaleUtils;
import betterwithmods.module.internal.BlockRegistry;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

import static betterwithmods.lib.TooltipLib.FURNACE_TIME;


public class HCFurnace extends Feature {

    public static boolean CONSUME_FUEL_WHEN_IDLE, TOOLTIP;
    public static int DEFAULT_FURNACE_TIMING = 200;
    public static HashMap<Ingredient, Integer> FURNACE_TIMINGS = Maps.newHashMap();
    public static HashMap<Ingredient, Integer> FUEL_TIMINGS = Maps.newHashMap();

    public HCFurnace() {
    }

    public static OptionalInt getCookingTime(ItemStack stack) {
        return FURNACE_TIMINGS.entrySet().stream().filter(e -> e.getKey().apply(stack)).mapToInt(Map.Entry::getValue).findAny();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitch(TextureStitchEvent event) {
        event.getMap().registerSprite(new ResourceLocation("betterwithmods:blocks/furnace_full"));
    }

    @SubscribeEvent
    public void getFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
        int speed = FUEL_TIMINGS.entrySet().stream().filter(e -> e.getKey().apply(event.getItemStack())).mapToInt(Map.Entry::getValue).findAny().orElse(-1);
        if (speed >= 0) {
            event.setBurnTime(speed);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!TOOLTIP)
            return;
        if (!FurnaceRecipes.instance().getSmeltingResult(event.getItemStack()).isEmpty()) {
            double ticks = HCFurnace.getCookingTime(event.getItemStack()).orElse(HCFurnace.DEFAULT_FURNACE_TIMING);
            double seconds = ticks / 20.0;
            event.getToolTip().add(LocaleUtils.getTooltip(ModLib.MODID, FURNACE_TIME, String.format("%.2f", seconds)));
        }
    }

    @Override
    public String getDescription() {
        return "Overrides the vanilla furnace to allow for some changes: Allows varying item cook times, changes fuel values and a tweak to make the furnace visually show whether it has content";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        CONSUME_FUEL_WHEN_IDLE = loadProperty("Consume Fuel When Idle", true).setComment("Furnaces will consume fuel even if no smeltable items are present.").get();
        DEFAULT_FURNACE_TIMING = loadProperty("Default Furnace Timing", 200).setMin(1).setComment("Default number of ticks for an item to smelt in the furnace (vanilla is 200)").get();
        TOOLTIP = loadProperty("Tooltip for modified cooking time", true).setComment("Shows a tooltip for items with modified cooking time").get();

        BlockRegistry.registerBlocks(BlockEntryBuilderFactory.<Void>create(getLogger())
                .builder().subalias("field_150460_al", "FURNACE").block(new BlockFurnace(false)).id("minecraft:furnace").build()
                .builder().subalias("field_150470_am", "LIT_FURNACE").block(new BlockFurnace(true)).id("minecraft:lit_furnace").noItem().build()
                .complete());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFurnace.class, new TESRFurnaceContent());
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        RecipeRegistry.removeFurnaceRecipe(new ItemStack(Blocks.DIAMOND_ORE));
        RecipeRegistry.removeFurnaceRecipe(new ItemStack(Blocks.COAL_ORE));
        RecipeRegistry.removeFurnaceRecipe(new ItemStack(Blocks.EMERALD_ORE));
        RecipeRegistry.removeFurnaceRecipe(new ItemStack(Blocks.REDSTONE_ORE));
        RecipeRegistry.removeFurnaceRecipe(new ItemStack(Blocks.LAPIS_ORE));
        RecipeRegistry.removeFurnaceRecipe(new ItemStack(Blocks.QUARTZ_BLOCK));

        //Remove Furnace Recycling
        RecipeRegistry.removeFurnaceRecipe(Items.CHAINMAIL_HELMET);
        RecipeRegistry.removeFurnaceRecipe(Items.CHAINMAIL_CHESTPLATE);
        RecipeRegistry.removeFurnaceRecipe(Items.CHAINMAIL_LEGGINGS);
        RecipeRegistry.removeFurnaceRecipe(Items.CHAINMAIL_BOOTS);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_PICKAXE);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_SHOVEL);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_AXE);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_HOE);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_SWORD);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_HELMET);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_CHESTPLATE);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_LEGGINGS);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_BOOTS);
        RecipeRegistry.removeFurnaceRecipe(Items.IRON_HORSE_ARMOR);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_PICKAXE);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_SHOVEL);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_AXE);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_HOE);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_SWORD);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_HELMET);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_CHESTPLATE);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_LEGGINGS);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_BOOTS);
        RecipeRegistry.removeFurnaceRecipe(Items.GOLDEN_HORSE_ARMOR);

        FURNACE_TIMINGS = config().loadItemStackIntMap("Furnace Timing Recipes", getCategory(), "example recipes  minecraft:iron_ore=1000  or ore:oreIron=1000", new String[]{
                "ore:oreIron=1600",
                "ore:oreGold=1600",
                "ore:cobblestone=1600",
                "ore:sand=1600"
        });
        FUEL_TIMINGS = config().loadItemStackIntMap("Furnace Fuel Timing Overrides", getCategory(), "Overrides the fuel time for inputted items or oredict, see Furnace Timing for entry format", new String[]{
                "minecraft:boat=750",
                "minecraft:log:0=1600",
                "minecraft:log:1=1200",
                "minecraft:log:2=2000",
                "minecraft:log:3=1200",
                "minecraft:log2:0=1600",
                "minecraft:log2:1=1600",
                "minecraft:coal:0=1600",
                "minecraft:planks:0=400",
                "minecraft:planks:1=300",
                "minecraft:planks:2=500",
                "minecraft:planks:3=300",
                "minecraft:planks:4=400",
                "minecraft:planks:5=300",
                "minecart:sapling=25"
        });
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

}

