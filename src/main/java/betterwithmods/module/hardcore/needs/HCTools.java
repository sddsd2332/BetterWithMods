package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import betterwithmods.util.ReflectionLib;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCTools extends Feature {


    public static final HashMap<Item.ToolMaterial, ToolMaterialOverride> OVERRIDES = Maps.newHashMap();
    public static boolean removeLowTools;
    public static int noHungerThreshold;
    public static int noDamageThreshold;
    private static Set<ItemTool> TOOLS;

    private static void removeLowTierToolRecipes() {
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
    public void preInit(FMLPreInitializationEvent event) {
        if (removeLowTools) {
            removeLowTierToolRecipes();
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {

        TOOLS = Sets.newHashSet(
                BWMItems.STEEL_AXE, BWMItems.STEEL_BATTLEAXE, BWMItems.STEEL_HOE, BWMItems.STEEL_SWORD, BWMItems.STEEL_PICKAXE, BWMItems.STEEL_SWORD, BWMItems.STEEL_MATTOCK,
                Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE,
                Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_HOE,
                Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_HOE,
                Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE,
                Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_HOE
        ).stream().filter(item -> item instanceof ItemTool).map(item -> (ItemTool) item).collect(Collectors.toSet());

        OVERRIDES.put(Item.ToolMaterial.WOOD, new ToolMaterialOverride("wood", 1, 1.01F, 0));
        OVERRIDES.put(Item.ToolMaterial.STONE, new ToolMaterialOverride("stone", 6, 1.01F, 5));
        OVERRIDES.put(Item.ToolMaterial.IRON, new ToolMaterialOverride("iron", 500, 6.0F, 14));
        OVERRIDES.put(Item.ToolMaterial.DIAMOND, new ToolMaterialOverride("diamond", 1561, 8.0F, 14));
        OVERRIDES.put(Item.ToolMaterial.GOLD, new ToolMaterialOverride("gold", 32, 12.0F, 22));
        OVERRIDES.put(BWMItems.SOULFORGED_STEEL, new ToolMaterialOverride(BWMItems.SOULFORGED_STEEL));

        TOOLS.forEach(this::loadToolMaterialOverride);
    }

    private void loadToolMaterialOverride(ItemTool tool) {
        Item.ToolMaterial material = ReflectionHelper.getPrivateValue(ItemTool.class, tool, ReflectionLib.ITEMTOOL_TOOLMATERIAL);
        ToolMaterialOverride override = OVERRIDES.get(material);
        ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, override.getMaxUses(), ReflectionLib.TOOLMATERIAL_MAXUSES);
        ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, override.getEfficiency(), ReflectionLib.TOOLMATERIAL_EFFICIENCY);
        ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, override.getEnchantability(), ReflectionLib.TOOLMATERIAL_ENCHANTABILITIY);
        ReflectionHelper.setPrivateValue(ItemTool.class, tool, material.getEfficiency(), ReflectionLib.ITEMTOOL_EFFICIENCY);
        tool.setMaxDamage(Math.max(1, material.getMaxUses() - 1)); //subtract one from the max durability because the tool doesn't break until -1
    }

    @Override
    public void setupConfig() {
        removeLowTools = loadPropBool("Remove cheapest tools", "The minimum level of the hoe and the sword is iron, and the axe needs at least stone.", true);
        noHungerThreshold = loadPropInt("No Exhaustion Harvest Level", "When destroying a 0 hardness block with a tool of this harvest level or higher, no exhaustion is applied", Item.ToolMaterial.IRON.getHarvestLevel());
        noDamageThreshold = loadPropInt("No Durability Damage Harvest Level", "When destroying a 0 hardness block with a tool of this harvest level or higher, no durability damage is applied", Item.ToolMaterial.DIAMOND.getHarvestLevel());
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @SubscribeEvent
    public void onBreaking(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty()) return;
        if (stack.getMaxDamage() == 1) {
            destroyItem(stack, player);
        }
    }

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

    public class ToolMaterialOverride {
        private int maxUses;
        private float efficiencyOnProperMaterial;
        private int enchantability;


        public ToolMaterialOverride(Item.ToolMaterial material) {
            this(material.name().toLowerCase(), material.getMaxUses(), material.getEfficiency(), material.getEnchantability());
        }

        ToolMaterialOverride(String name, int maxUses, float efficiencyOnProperMaterial, int enchantability) {
            this.maxUses = configHelper.loadPropInt("Max Durability", configCategory + "." + name, "", maxUses);
            this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
            this.enchantability = enchantability;
        }

        int getMaxUses() {
            return maxUses;
        }

        float getEfficiency() {
            return efficiencyOnProperMaterial;
        }

        int getEnchantability() {
            return enchantability;
        }
    }

}
