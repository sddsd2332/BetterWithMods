package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import betterwithmods.util.ReflectionLib;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Set;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCTools extends Feature {

    private static final Set<Item> TOOLS = Sets.newHashSet(
            Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE,
            Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_HOE,
            Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_HOE,
            Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE,
            Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_HOE
    );
    public static boolean earlyPickaxesRebalance;
    public static boolean removeLowTools;
    public static int noHungerThreshold;
    public static int noDamageThreshold;
    public static boolean changeAxeRecipe;
    private static int woodDurability;
    private static int stoneDurability;
    private static int ironDurability;
    private static int diamondDurability;
    private static int goldDurability;

    /**
     * Edit the values of {@link Item.ToolMaterial}.
     * The new values are described in {@link ToolMaterialOverride}.
     */
    private static void changeVanillaToolMaterials() {
        // Edit MATERIALS
        for (Item.ToolMaterial material : Item.ToolMaterial.values()) {
            ToolMaterialOverride newValues = ToolMaterialOverride.getOverride(material.name());
            if (newValues == null) continue;
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, newValues.getMaxUses(), ReflectionLib.TOOLMATERIAL_MAXUSES);
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, newValues.getEfficiencyOnProperMaterial(), ReflectionLib.TOOLMATERIAL_EFFICIENCY);
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, newValues.getEnchantability(), ReflectionLib.TOOLMATERIAL_ENCHANTABILITIY);
        }
        // Change values already taken from material at that time

        for (Item item : TOOLS) {
            if (!(item instanceof ItemTool)) continue;
            ItemTool tool = (ItemTool) item;
            ToolMaterialOverride newValues = ToolMaterialOverride.getOverride(tool.getToolMaterialName());
            if (newValues == null) continue;
            tool.setMaxDamage(newValues.getMaxUses());
            ReflectionHelper.setPrivateValue(ItemTool.class, tool, newValues.getEfficiencyOnProperMaterial(), ReflectionLib.ITEMTOOL_EFFICIENCY);
        }
    }

    public static void removeLowTierToolRecipes() {
        BWMRecipes.removeRecipe(new ItemStack(Items.WOODEN_AXE, OreDictionary.WILDCARD_VALUE));
        BWMRecipes.removeRecipe(new ItemStack(Items.WOODEN_HOE, OreDictionary.WILDCARD_VALUE));
        BWMRecipes.removeRecipe(new ItemStack(Items.WOODEN_SWORD, OreDictionary.WILDCARD_VALUE));
        BWMRecipes.removeRecipe(new ItemStack(Items.STONE_HOE, OreDictionary.WILDCARD_VALUE));
        BWMRecipes.removeRecipe(new ItemStack(Items.STONE_SWORD, OreDictionary.WILDCARD_VALUE));
    }

    @Override
    public String getFeatureDescription() {
        return "Overhaul the durability of tools to be more rewarding when reaching the next level. Completely disables wooden tools (other than pick) by default.";
    }

    @Override
    public void setupConfig() {
        earlyPickaxesRebalance = loadPropBool("Early pickaxes rebalance", "Wooden Pickaxe will have 1 usage and Stone Pickaxe 6.", true);
        removeLowTools = loadPropBool("Remove cheapest tools", "The minimum level of the hoe and the sword is iron, and the axe needs at least stone.", true);

        woodDurability = loadPropInt("Hardcore Hardness Wood Durability", "Number of usages for wooden tools. Does not change Pickaxe if earlyPickaxesRebalanced is enabled", "", 1, 1, 60);
        stoneDurability = loadPropInt("Hardcore Hardness Stone Durability", "Number of usages for stone tools. Does not change Pickaxe if earlyPickaxesRebalanced is enabled", "", 50, 1, 132);
        ironDurability = loadPropInt("Hardcore Hardness Iron Durability", "Number of usages for iron tools.", "", 500, 1, 251);
        diamondDurability = loadPropInt("Hardcore Hardness Diamond Durability", "Number of usages for diamond tools.", "", 1561, 1, 1562);
        goldDurability = loadPropInt("Hardcore Hardness Gold Durability", "Number of usages for golden tools.", "", 32, 1, 33);

        changeAxeRecipe = loadRecipeCondition("changeAxeRecipe", "Change Axe Recipe", "Change the axe recipes to only require 2 MATERIALS", true);

        noHungerThreshold = loadPropInt("No Exhaustion Harvest Level", "When destroying a 0 hardness block with a tool of this harvest level or higher, no exhaustion is applied", Item.ToolMaterial.IRON.getHarvestLevel());
        noDamageThreshold = loadPropInt("No Durability Damage Harvest Level", "When destroying a 0 hardness block with a tool of this harvest level or higher, no durability damage is applied", Item.ToolMaterial.DIAMOND.getHarvestLevel());
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (removeLowTools)
            removeLowTierToolRecipes();
        if (changeAxeRecipe) {
            BWMRecipes.removeRecipe(new ResourceLocation("minecraft:stone_axe"));
            BWMRecipes.removeRecipe(new ResourceLocation("minecraft:iron_axe"));
            BWMRecipes.removeRecipe(new ResourceLocation("minecraft:golden_axe"));
            BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_axe"));
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        changeVanillaToolMaterials();
        if (earlyPickaxesRebalance) {
            Items.WOODEN_PICKAXE.setMaxDamage(1);
            Items.STONE_PICKAXE.setMaxDamage(5);
        }
    }

    /**
     * Sets the wooden pickaxe to 1 usage. Why:
     * {@link Item#setMaxDamage} used with "1" gives 2 usages, and with "0" gives unbreakable item.
     * So we needed another solution to set it to 1 usage.
     */
    @SubscribeEvent
    public void onBreaking(BlockEvent.BreakEvent event) {
        if (!earlyPickaxesRebalance) return;
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty()) return;
        if (stack.getMaxDamage() == 1) {
            destroyItem(stack, player);
        }
    }

    //Gee BWM why does bord let you have TWO BreakEvents in one class???
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void harvestGarbage(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (event.isCanceled() || player == null || player.isCreative())
            return;
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = player.getHeldItemMainhand();
        String tooltype = state.getBlock().getHarvestTool(state);
        if (tooltype != null && state.getBlockHardness(world, pos) <= 0 && stack.getItem().getHarvestLevel(stack, tooltype, player, state) < noDamageThreshold)
            stack.damageItem(1, player); //Make 0 hardness blocks damage tools that are not over some harvest level
    }

    private void destroyItem(ItemStack stack, EntityLivingBase entity) {
        int damage = stack.getMaxDamage();
        stack.damageItem(damage, entity);
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    /**
     * New values for {@link net.minecraft.item.Item.ToolMaterial}
     */
    private enum ToolMaterialOverride {
        WOOD(woodDurability, 1.01F, 0),
        STONE(stoneDurability, 1.01F, 5),
        IRON(ironDurability, 6.0F, 14),
        DIAMOND(diamondDurability, 8.0F, 14),
        GOLD(goldDurability, 12.0F, 22);
        private final int maxUses;
        private final float efficiencyOnProperMaterial;
        private final int enchantability;

        ToolMaterialOverride(int maxUses, float efficiency, int enchantability) {
            this.maxUses = maxUses;
            this.efficiencyOnProperMaterial = efficiency;
            this.enchantability = enchantability;
        }

        public static ToolMaterialOverride getOverride(String material) {
            switch (material.toUpperCase()) {
                case "WOOD":
                    return ToolMaterialOverride.WOOD;
                case "STONE":
                    return ToolMaterialOverride.STONE;
                case "IRON":
                    return ToolMaterialOverride.IRON;
                case "DIAMOND":
                    return ToolMaterialOverride.DIAMOND;
                case "GOLD":
                    return ToolMaterialOverride.GOLD;
                default:
                    return null;
            }
        }

        public int getMaxUses() {
            return this.maxUses;
        }

        public float getEfficiencyOnProperMaterial() {
            return this.efficiencyOnProperMaterial;
        }

        public int getEnchantability() {
            return this.enchantability;
        }
    }
}
