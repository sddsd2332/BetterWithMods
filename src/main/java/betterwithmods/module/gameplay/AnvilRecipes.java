package betterwithmods.module.gameplay;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.anvil.ShapelessAnvilRecipe;
import betterwithmods.module.Feature;
import betterwithmods.module.hardcore.needs.HCTools;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class AnvilRecipes extends Feature {
    public AnvilRecipes() {
        canDisable = false;
    }

    public static ShapedAnvilRecipe addSteelShapedRecipe(String recipeName, ItemStack output, Object... input) {
        return addSteelShapedRecipe(new ResourceLocation(BWMod.MODID, recipeName), output, input);
    }

    public static ShapedAnvilRecipe addSteelShapedRecipe(ResourceLocation recipeName, ItemStack output, Object... input) {
        if (recipeName == null || recipeName.toString().isEmpty()) {
            BWMod.logger.warn("Anvil Recipe is missing recipeName" + output);
            recipeName = output.getItem().getRegistryName();
        }
        ShapedAnvilRecipe recipe = new ShapedAnvilRecipe(null, output, input);
        addAnvilRecipe(recipe.setRegistryName(recipeName));
        return recipe;
    }

    public static ShapelessAnvilRecipe addSteelShapelessRecipe(String recipeName, ItemStack output, Object... input) {
        return addSteelShapelessRecipe(new ResourceLocation(BWMod.MODID, recipeName), output, input);
    }

    public static ShapelessAnvilRecipe addSteelShapelessRecipe(ResourceLocation recipeName, ItemStack output, Object... input) {
        if (recipeName == null || recipeName.toString().isEmpty()) {
            BWMod.logger.warn("Anvil Recipe is missing recipeName" + output);
            recipeName = output.getItem().getRegistryName();
        }
        ShapelessAnvilRecipe recipe = new ShapelessAnvilRecipe(null, output, input);
        addAnvilRecipe(recipe.setRegistryName(recipeName));
        return recipe;
    }

    private static IRecipe addAnvilRecipe(IRecipe recipe) {
        BWRegistry.ANVIL.addRecipe(recipe);
        if (recipe.getRecipeOutput().isEmpty()) {
            BWMod.logger.warn("Recipe is missing output " + recipe.getGroup());
        }
        return recipe;
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        addSteelShapedRecipe("block_dispenser", new ItemStack(BWMBlocks.BLOCK_DISPENSER), "MMMM", "MUUM", "STTS", "SRRS", 'M', Blocks.MOSSY_COBBLESTONE, 'U', new ItemStack(BWMBlocks.SOUL_URN), 'S', "stone", 'R', "dustRedstone", 'T', Blocks.REDSTONE_TORCH);
        addSteelShapedRecipe("buddy_block", new ItemStack(BWMBlocks.BUDDY_BLOCK), "SSLS", "LTTS", "STTL", "SLSS", 'S', "stone", 'T', Blocks.REDSTONE_TORCH, 'L', ItemMaterial.getStack(ItemMaterial.EnumMaterial.POLISHED_LAPIS));
        addSteelShapedRecipe("detector", new ItemStack(BWMBlocks.DETECTOR), "CCCC", "LTTL", "SRRS", "SRRS", 'C', "cobblestone", 'L', ItemMaterial.getStack(ItemMaterial.EnumMaterial.POLISHED_LAPIS), 'T', Blocks.REDSTONE_TORCH, 'S', "stone", 'R', "dustRedstone");
        addSteelShapedRecipe("steel_helmet", new ItemStack(BWMItems.STEEL_HELMET), "SSSS", "S  S", "S  S", " PP ", 'P', ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe("steel_chest", new ItemStack(BWMItems.STEEL_CHEST), "P  P", "SSSS", "SSSS", "SSSS", 'P', ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe("steel_pants", new ItemStack(BWMItems.STEEL_PANTS), "SSSS", "PSSP", "P  P", "P  P", 'P', ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe("steel_boots", new ItemStack(BWMItems.STEEL_BOOTS), " SS ", " SS ", "SPPS", 'P', ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe("polished_lapis", ItemMaterial.getStack(ItemMaterial.EnumMaterial.POLISHED_LAPIS, 2), "LLL", "LLL", "GGG", " R ", 'L', "gemLapis", 'R', "dustRedstone", 'G', "nuggetGold");
        if (BWMod.MODULE_LOADER.isFeatureEnabled(HCTools.class)) {
            addSteelShapedRecipe("steel_axe", new ItemStack(BWMItems.STEEL_AXE), "X ", "XH", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        } else {
            addSteelShapedRecipe("steel_axe", new ItemStack(BWMItems.STEEL_AXE), "XX", "XH", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        }
        addSteelShapedRecipe("steel_hoe", new ItemStack(BWMItems.STEEL_HOE), "XX", " H", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe("steel_pickaxe", new ItemStack(BWMItems.STEEL_PICKAXE), "XXX", " H ", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe("steel_shovel", new ItemStack(BWMItems.STEEL_SHOVEL), "X", "H", "H", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe("steel_sword", new ItemStack(BWMItems.STEEL_SWORD), "X", "X", "X", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe("steel_battleaxe", new ItemStack(BWMItems.STEEL_BATTLEAXE), "XXX", "XHX", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe("steel_mattock", new ItemStack(BWMItems.STEEL_MATTOCK), " XXX", "X H ", "  H ", "  H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getStack(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe("armor_plate", ItemMaterial.getStack(ItemMaterial.EnumMaterial.ARMOR_PLATE), "BSPB", 'B', ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'S', "ingotSoulforgedSteel", 'P', ItemMaterial.getStack(ItemMaterial.EnumMaterial.PADDING));
        addSteelShapedRecipe("broadhead", ItemMaterial.getStack(ItemMaterial.EnumMaterial.BROADHEAD, 6), " N ", " N ", "NNN", " N ", 'N', "nuggetSoulforgedSteel");
        addSteelShapedRecipe("steel_block", new ItemStack(BWMBlocks.STEEL_BLOCK), "XXXX", "XXXX", "XXXX", "XXXX", 'X', "ingotSoulforgedSteel");
        addSteelShapedRecipe("chopping_block", BlockAesthetic.getStack(BlockAesthetic.EnumType.CHOPBLOCK), "X  X", "XXXX", 'X', "stone");
        addSteelShapedRecipe("chain_mail", ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHAIN_MAIL, 2), "N N ", " N N", "N N ", " N N", 'N', "nuggetIron");
        addSteelShapedRecipe("iron_wall", new ItemStack(BWMBlocks.IRON_WALL, 8, 0), "XXXX", "XXXX", "XXXX", "XXXX", 'X', new ItemStack(Blocks.IRON_BARS));
        addSteelShapedRecipe("steel_pressure_plate", new ItemStack(BWMBlocks.STEEL_PRESSURE_PLATE, 2), "IIII", " RR ", 'I', "ingotSoulforgedSteel", 'R', "dustRedstone");
        addSteelShapedRecipe("lightning_rod", new ItemStack(BWMBlocks.CANDLE_HOLDER, 2), "N", "N", "N", "I", 'I', "ingotSoulforgedSteel", 'N', "nuggetSoulforgedSteel");
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}

