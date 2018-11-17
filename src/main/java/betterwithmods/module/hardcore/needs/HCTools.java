package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMItems;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.lib.ReflectionLib;
import betterwithmods.module.internal.ItemRegistry;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCTools extends Feature {


    public static final HashMap<Item.ToolMaterial, ToolMaterialOverride> OVERRIDES = Maps.newHashMap();
    public static boolean removeLowTools, perToolOverrides;
    public static int noHungerThreshold;
    public static int noDamageThreshold;

    private static void removeLowTierToolRecipes() {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.ITEM_OUTPUT, Items.WOODEN_AXE));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.ITEM_OUTPUT, Items.WOODEN_HOE));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.ITEM_OUTPUT, Items.WOODEN_SWORD));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.ITEM_OUTPUT, Items.STONE_HOE));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.ITEM_OUTPUT, Items.STONE_SWORD));
    }

    @SubscribeEvent
    public static void onHitEntity(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = player.getHeldItemMainhand();
            breakTool(stack, player);
        }
    }

    @SubscribeEvent
    public static void onUseHoe(UseHoeEvent event) {
        breakTool(event.getCurrent(), event.getEntityPlayer());
    }

    @SubscribeEvent
    public static void onBreaking(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        breakTool(stack, player);
    }

    private static void breakTool(ItemStack stack, EntityPlayer player) {
        if (stack.isEmpty()) return;
        if (stack.getMaxDamage() == 1) {
            destroyItem(stack, player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void harvestGarbage(BlockEvent.BreakEvent event) {
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

    private static void destroyItem(ItemStack stack, EntityLivingBase entity) {
        int damage = stack.getMaxDamage();
        stack.damageItem(damage, entity);
    }

    @Override
    public String getDescription() {
        return "Overhaul the durability of tools to be more rewarding when reaching the next level. Completely disables wooden tools (other than pick) by default.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        if (removeLowTools) {
            removeLowTierToolRecipes();
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        removeLowTools = loadProperty("Remove cheapest tools", true).setComment("The minimum level of the hoe and the sword is iron, and the axe needs at least stone.").get();
        noHungerThreshold = loadProperty("No Exhaustion Harvest Level", Item.ToolMaterial.IRON.getHarvestLevel()).setComment("When destroying a 0 hardness block with a tool of this harvest level or higher, no exhaustion is applied").get();
        noDamageThreshold = loadProperty("No Durability Damage Harvest Level", Item.ToolMaterial.DIAMOND.getHarvestLevel()).setComment("When destroying a 0 hardness block with a tool of this harvest level or higher, no durability damage is applied").get();
        perToolOverrides = loadProperty("Change Durability per Tool", true).setComment("Allow configuring tool durability for each class").get();

        Set<Item> TOOLS = new HashSet<>(Sets.newHashSet(
                BWMItems.STEEL_AXE, BWMItems.STEEL_BATTLEAXE, BWMItems.STEEL_HOE, BWMItems.STEEL_SWORD, BWMItems.STEEL_PICKAXE, BWMItems.STEEL_SWORD, BWMItems.STEEL_MATTOCK,
                Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE,
                Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_HOE,
                Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_HOE,
                Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE,
                Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_HOE
        ));

        OVERRIDES.put(Item.ToolMaterial.WOOD, new ToolMaterialOverride(this, "wood", 1, 1.01F, 0).addClassOverride("shovel", 10));
        OVERRIDES.put(Item.ToolMaterial.STONE, new ToolMaterialOverride(this, "stone", 6, 1.01F, 5).addClassOverride("shovel", 50).addClassOverride("axe", 50));
        OVERRIDES.put(Item.ToolMaterial.IRON, new ToolMaterialOverride(this, "iron", 500, 6.0F, 14));
        OVERRIDES.put(Item.ToolMaterial.DIAMOND, new ToolMaterialOverride(this, "diamond", 1561, 8.0F, 14));
        OVERRIDES.put(Item.ToolMaterial.GOLD, new ToolMaterialOverride(this, "gold", 32, 12.0F, 22));
        OVERRIDES.put(ItemRegistry.SOULFORGED_STEEL, new ToolMaterialOverride(this, ItemRegistry.SOULFORGED_STEEL));

        TOOLS.forEach(this::loadToolMaterialOverride);
    }

    private Item.ToolMaterial getMaterial(Item tool) {
        if (tool instanceof ItemTool) {
            return ReflectionHelper.getPrivateValue(ItemTool.class, (ItemTool) tool, ReflectionLib.ITEMTOOL_TOOLMATERIAL);
        } else if (tool instanceof ItemHoe) {
            return ReflectionHelper.getPrivateValue(ItemHoe.class, (ItemHoe) tool, ReflectionLib.ITEMHOE_TOOLMATERIAL);
        } else if (tool instanceof ItemSword) {
            return ReflectionHelper.getPrivateValue(ItemSword.class, (ItemSword) tool, ReflectionLib.ITEMSWORD_TOOLMATERIAL);
        }
        return null;
    }

    private Set<String> getToolClass(ItemStack stack) {
        Item item = stack.getItem();
        Set<String> classes = Sets.newHashSet();
        classes.addAll(item.getToolClasses(stack));
        if (item instanceof ItemSword) {
            classes.add("sword");
        } else if (item instanceof ItemHoe) {
            classes.add("hoe");
        }
        return classes;
    }

    private void loadToolMaterialOverride(Item tool) {
        Item.ToolMaterial material = getMaterial(tool);
        if (material != null) {
            ToolMaterialOverride override = OVERRIDES.get(material);
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, override.getMaxUses(), ReflectionLib.TOOLMATERIAL_MAXUSES);
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, override.getEfficiency(), ReflectionLib.TOOLMATERIAL_EFFICIENCY);
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, override.getEnchantability(), ReflectionLib.TOOLMATERIAL_ENCHANTABILITIY);
            if (tool instanceof ItemTool) {
                ReflectionHelper.setPrivateValue(ItemTool.class, (ItemTool) tool, material.getEfficiency(), ReflectionLib.ITEMTOOL_EFFICIENCY);
            }

            ItemStack stack = new ItemStack(tool);

            Set<String> classes = getToolClass(stack);
            if (!classes.isEmpty()) {
                classes.stream().findFirst().ifPresent(c -> tool.setMaxDamage(override.getMaxUses(c)));
            } else {
                tool.setMaxDamage(override.getMaxUses());
            }
        }

    }

    public class ToolMaterialOverride {
        private final String name;
        public Map<String, Integer> toolClassOverrides = Maps.newHashMap();
        private int maxUses;
        private float efficiencyOnProperMaterial;
        private int enchantability;

        private Feature feature;

        public ToolMaterialOverride(Feature feature, Item.ToolMaterial material) {
            this(feature, material.name().toLowerCase(), material.getMaxUses(), material.getEfficiency(), material.getEnchantability());
        }

        ToolMaterialOverride(Feature feature, String name, int maxUses, float efficiencyOnProperMaterial, int enchantability) {
            this.feature = feature;
            this.name = name;
            this.maxUses = feature.loadProperty("Max Durability", maxUses).subCategory(this.name).get();
            this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
            this.enchantability = enchantability;
        }

        int getMaxUses(String toolClass) {
            int use = getMaxUses();
            if (perToolOverrides) {
                use = feature.loadProperty("Max Durability:" + toolClass, toolClassOverrides.getOrDefault(toolClass, use)).subCategory(this.name).get();
            }
            return Math.max(1, use - 1); //subtract one from the max durability because the tool doesn't break until -1
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

        ToolMaterialOverride addClassOverride(String toolClass, int durability) {
            toolClassOverrides.put(toolClass, durability);
            return this;
        }

    }

}
