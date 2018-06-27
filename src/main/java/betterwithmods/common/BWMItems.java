package betterwithmods.common;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.items.*;
import betterwithmods.common.items.tools.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BWMItems {
    public static final ToolMaterial SOULFORGED_STEEL = EnumHelper.addToolMaterial("SOULFORGED_STEEL", 4, 2250, 10f, 3, 22);
    //Foods

    //Bowl
    public static final Item HEARTY_STEW = new ItemSoup(10).setMaxStackSize(64).setCreativeTab(CreativeTabs.FOOD).setRegistryName("hearty_stew");
    public static final Item CHOWDER = new ItemSoup(5).setMaxStackSize(64).setCreativeTab(CreativeTabs.FOOD).setRegistryName("chowder");
    public static final Item CHICKEN_SOUP = new ItemSoup(8).setMaxStackSize(64).setCreativeTab(CreativeTabs.FOOD).setRegistryName("chicken_soup");

    //Other
    public static final Item CREEPER_OYSTER = (new ItemFood(2, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1)).setCreativeTab(CreativeTabs.FOOD).setRegistryName("creeper_oyster");
    public static final Item KIBBLE = new ItemFood(3, 0, true).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setCreativeTab(CreativeTabs.FOOD).setRegistryName("kibble");
    public static final Item CHOCOLATE = new ItemFood(2, 0.2f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("chocolate");
    public static final Item DONUT = new ItemFood(2, 0.25f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("donut");

    public static final Item RAW_KEBAB = new ItemFood(4, 0.3f, false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setCreativeTab(CreativeTabs.FOOD).setRegistryName("raw_kebab");
    public static final Item COOKED_KEBAB = new ItemFood(8, 0.4f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("cooked_kebab");

    public static final Item RAW_SCRAMBLED_EGG = new ItemFood(4, 0.3f, false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setCreativeTab(CreativeTabs.FOOD).setRegistryName("raw_scrambled_egg");
    public static final Item COOKED_SCRAMBLED_EGG = new ItemFood(5, 0.5f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("cooked_scrambled_egg");

    public static final Item RAW_OMELET = new ItemFood(3, 0.5f, false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setCreativeTab(CreativeTabs.FOOD).setRegistryName("raw_omelet");
    public static final Item COOKED_OMELET = new ItemFood(4, 1, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("cooked_omelet");

    public static final Item HAM_AND_EGGS = new ItemFood(6, 1, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("ham_and_eggs");
    public static final Item TASTY_SANDWICH = new ItemFood(7, 0.7f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("tasty_sandwich");

    public static final Item BEEF_POTATOES = new ItemFood(7, 0.5f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("beef_potatoes");
    public static final Item PORK_DINNER = new ItemFood(8, 0.6f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("pork_dinner");
    public static final Item BEEF_DINNER = new ItemFood(8, 0.6f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("beef_dinner");

    //Meat
    public static final Item BOILED_EGG = new ItemFood(4, 0.7F, false).setRegistryName("boiled_egg");
    public static final Item RAW_EGG = new ItemFood(2, 0.2f, false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setCreativeTab(CreativeTabs.FOOD).setRegistryName("raw_egg");
    public static final Item COOKED_EGG = new ItemFood(3, 0.5f, false).setCreativeTab(CreativeTabs.FOOD).setRegistryName("cooked_egg");

    public static final Item WOLF_CHOP = new ItemFood(3, 0.3F, true).setCreativeTab(CreativeTabs.FOOD).setRegistryName("wolf_chop");
    public static final Item COOKED_WOLF_CHOP = new ItemFood(8, 0.3F, true).setCreativeTab(CreativeTabs.FOOD).setRegistryName("cooked_wolf_chop");

    public static final Item MYSTERY_MEAT = new ItemFood(2, 0.3f, true).setRegistryName("mystery_meat");
    public static final Item COOKED_MYSTERY_MEAT = new ItemFood(6, 0.8F, true).setRegistryName("cooked_mystery_meat");

    public static final Item BAT_WING = new ItemFood(2, 0.3F, false).setRegistryName("bat_wing");
    public static final Item COOKED_BAT_WING = new ItemFood(4, 0.6F, false).setRegistryName("cooked_bat_wing");

    //Vegetable
    public static final Item COOKED_BEETROOT = new ItemFood(4, 0.5F, false).setRegistryName("cooked_beetroot");
    public static final Item COOKED_CARROT = new ItemFood(3, 0.6F, false).setRegistryName("cooked_carrot");

    //Fruit
    public static final Item APPLE_PIE = new ItemFood(8, 0.3F, false).setRegistryName("apple_pie");
    public static final Item MELON_PIE = new ItemFood(8, 0.4f, false).setRegistryName("melon_pie");


    public static final Item BARK = new ItemBark().setRegistryName("bark");
    public static final Item DYNAMITE = new ItemDynamite().setRegistryName("dynamite");
    public static final Item FERTILIZER = new ItemFertilizer().setRegistryName("fertilizer");
    public static final Item STEEL_AXE = new ItemSoulforgedAxe().setRegistryName("steel_axe");
    public static final Item STEEL_HOE = new ItemSoulforgedHoe().setRegistryName("steel_hoe");
    public static final Item STEEL_PICKAXE = new ItemSoulforgedPickaxe().setRegistryName("steel_pickaxe");
    public static final Item STEEL_SHOVEL = new ItemSoulforgedShovel().setRegistryName("steel_shovel");
    public static final Item STEEL_SWORD = new ItemSoulforgedSword().setRegistryName("steel_sword");
    public static final Item STEEL_MATTOCK = new ItemSoulforgedMattock().setRegistryName("steel_mattock");
    public static final Item STEEL_BATTLEAXE = new ItemSoulforgedBattleAxe().setRegistryName("steel_battleaxe");
    public static final Item ENDER_SPECTACLES = new ItemEnderSpectacles().setRegistryName("ender_spectacles");
    public static final Item STEEL_HELMET = new ItemSoulforgeArmor(EntityEquipmentSlot.HEAD).setRegistryName("steel_helmet");
    public static final Item STEEL_CHEST = new ItemSoulforgeArmor(EntityEquipmentSlot.CHEST).setRegistryName("steel_chest");
    public static final Item STEEL_PANTS = new ItemSoulforgeArmor(EntityEquipmentSlot.LEGS).setRegistryName("steel_pants");
    public static final Item STEEL_BOOTS = new ItemSoulforgeArmor(EntityEquipmentSlot.FEET).setRegistryName("steel_boots");
    public static final Item COMPOSITE_BOW = new ItemCompositeBow().setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("composite_bow");
    public static final Item BROADHEAD_ARROW = new ItemBroadheadArrow().setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("broadhead_arrow");
    public static final Item STUMP_REMOVER = new ItemStumpRemover().setRegistryName("stump_remover");
    public static final Item DIRT_PILE = new Item().setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("dirt_pile");
    public static final Item GRAVEL_PILE = new Item().setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("gravel_pile");
    public static final Item SAND_PILE = new Item().setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("sand_pile");
    public static final Item RED_SAND_PILE = new Item().setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("red_sand_pile");
    public static final Item MANUAL = new ItemBookManual().setRegistryName("manual").setCreativeTab(BWCreativeTabs.BWTAB);
    public static final Item ARCANE_SCROLL = new ItemArcaneScroll().setRegistryName("arcane_scroll").setCreativeTab(BWCreativeTabs.BWTAB);
    public static final Item LEATHER_TANNED_HELMET = new ItemLeatherTannedArmor(EntityEquipmentSlot.HEAD).setRegistryName("leather_tanned_helmet");
    public static final Item LEATHER_TANNED_CHEST = new ItemLeatherTannedArmor(EntityEquipmentSlot.CHEST).setRegistryName("leather_tanned_chest");
    public static final Item LEATHER_TANNED_PANTS = new ItemLeatherTannedArmor(EntityEquipmentSlot.LEGS).setRegistryName("leather_tanned_pants");
    public static final Item LEATHER_TANNED_BOOTS = new ItemLeatherTannedArmor(EntityEquipmentSlot.FEET).setRegistryName("leather_tanned_boots");

    public static final Item WOOL_HELMET = new ItemWoolArmor(EntityEquipmentSlot.HEAD).setRegistryName("wool_helmet");
    public static final Item WOOL_CHEST = new ItemWoolArmor(EntityEquipmentSlot.CHEST).setRegistryName("wool_chest");
    public static final Item WOOL_PANTS = new ItemWoolArmor(EntityEquipmentSlot.LEGS).setRegistryName("wool_pants");
    public static final Item WOOL_BOOTS = new ItemWoolArmor(EntityEquipmentSlot.FEET).setRegistryName("wool_boots");

    private static final List<Item> ITEMS = new ArrayList<>();

    static {
        ItemMaterial.init();
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static void registerItems() {
        ItemMaterial.MATERIALS.values().forEach(BWMItems::registerItem);

        for (Field field : BWMItems.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                if (field.getType().isAssignableFrom(Item.class)) {
                    try {
                        registerItem((Item) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Register an Item.
     *
     * @param item Item instance to register.
     * @return Registered item.
     */
    public static Item registerItem(Item item) {
        //TODO remove this in 1.13, it is done automatically
        item.setUnlocalizedName(item.getRegistryName().toString());
        ITEMS.add(item);
        return item;
    }

    @SideOnly(Side.CLIENT)
    private static void setModelLocation(Item item, int meta, String variantSettings) {
        setModelLocation(item, meta, item.getRegistryName(), variantSettings);
    }

    @SideOnly(Side.CLIENT)
    private static void setModelLocation(Item item, int meta, ResourceLocation location, String variantSettings) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, variantSettings));
    }

    @SideOnly(Side.CLIENT)
    public static void setInventoryModel(Item item) {
            setModelLocation(item, 0, "inventory");
    }
}
