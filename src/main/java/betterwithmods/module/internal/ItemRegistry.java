package betterwithmods.module.internal;

import betterwithmods.client.baking.BarkModel;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMCreativeTabs;
import betterwithmods.common.items.*;
import betterwithmods.common.items.tools.*;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.item.armor.ArmorBuilderGenerator;
import betterwithmods.library.common.item.creation.BasicItemBuilder;
import betterwithmods.library.common.item.creation.ItemBuilder;
import betterwithmods.library.common.item.creation.ItemFactory;
import betterwithmods.library.common.item.food.FoodBuilderGenerator;
import betterwithmods.library.common.item.food.FoodType;
import betterwithmods.library.common.item.food.SoupBuilderGenerator;
import betterwithmods.library.common.item.string.StringItemBuilderGenerator;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class ItemRegistry extends RequiredFeature {

    public static final Item.ToolMaterial SOULFORGED_STEEL = EnumHelper.addToolMaterial("SOULFORGED_STEEL", 4, 2250, 10f, 3, 22);

    private static final List<Item> ITEMS = new ArrayList<>();

    public static List<Item> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static void registerItems(Collection<Item> items) {
        ITEMS.addAll(items);
    }

    public static void registerItem(ItemBuilder<?, ?> builder) {
        addItem(builder.build());
    }

    public static void addItem(Item item) {
        ITEMS.add(item);
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

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //TODO migrate
        for (Item item : ITEMS) {
            event.getRegistry().register(item);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        getItems().forEach(ItemRegistry::setInventoryModel);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        FoodType cooked_carrot = FoodType.create("cooked_carrot", 3, 0.6F),
                cooked_beetroot = FoodType.create("cooked_beetroot", 4, 0.5F);

        List<FoodType> foods = Lists.newArrayList(
                cooked_carrot,
                cooked_beetroot,

                FoodType.create("melon_pie", 8, 0.4f),
                FoodType.create("apple_pie", 8, 0.3f),

                FoodType.create("bat_wing", 2, 0.3F).raw(),
                FoodType.create("cooked_bat_wing", 4, 0.6F),

                FoodType.create("mystery_meat", 2, 0.3f).raw(),
                FoodType.create("cooked_mystery_meat", 6, 0.8F),

                FoodType.create("wolf_chop", 3, 0.3F).raw(),
                FoodType.create("cooked_wolf_chop", 8, 0.3F),

                FoodType.create("raw_egg", 2, 0.2f).raw(),
                FoodType.create("cooked_egg", 3, 0.5f),

                FoodType.create("raw_omelet", 3, 0.5f).raw(),
                FoodType.create("cooked_omelet", 4, 1),

                FoodType.create("raw_scrambled_egg", 4, 0.3f).raw(),
                FoodType.create("cooked_scrambled_egg", 5, 0.5f),

                FoodType.create("raw_kebab", 4, 0.3f).raw(),
                FoodType.create("cooked_kebab", 8, 0.4f),

                FoodType.create("boiled_egg", 4, 0.7F),
                FoodType.create("beef_dinner", 8, 0.6f),
                FoodType.create("pork_dinner", 8, 0.6f),
                FoodType.create("beef_potatoes", 7, 0.5f),
                FoodType.create("tasty_sandwich", 7, 0.7f),
                FoodType.create("ham_and_eggs", 6, 1),
                FoodType.create("donut", 2, 0.25f),
                FoodType.create("chocolate", 2, 0.2f),
                FoodType.create("kibble", 2, 0).wolfFood(true).raw(),
                FoodType.create("creeper_oyster", 2).potionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1)
        );

        List<FoodType> soups = Lists.newArrayList(
                FoodType.create("chicken_soup", 8).maxStack(64),
                FoodType.create("chowder", 5).maxStack(64),
                FoodType.create("hearty_stew", 10).maxStack(64)
        );

        registerItems(ItemFactory.create()
                .tab(BWMCreativeTabs.FOODS)
                .builderGenerator(new FoodBuilderGenerator(foods))
                .builderGenerator(new SoupBuilderGenerator(soups))
                .complete());

        registerItems(ItemFactory.create()
                .tab(BWMCreativeTabs.ITEMS)
                .builderGenerator(new ArmorBuilderGenerator<>(ItemWoolArmor.class, new ResourceLocation(ModLib.MODID, "wool")))
                .builderGenerator(new ArmorBuilderGenerator<>(ItemSoulforgeArmor.class, new ResourceLocation(ModLib.MODID, "steel")))
                .builderGenerator(new ArmorBuilderGenerator<>(ItemLeatherTannedArmor.class, new ResourceLocation(ModLib.MODID, "leather_tanned")))
                .builderGenerator(new StringItemBuilderGenerator("sand_pile", "dirt_pile", "gravel_pile", "red_sand_pile"))
                .builderGenerator(new ItemMaterial.Generator())
                .builder(new BasicItemBuilder(ItemArcaneScroll::new).id("arcane_scroll"))
                .builder(new BasicItemBuilder(ItemStumpRemover::new).id("stump_remover"))
                .builder(new BasicItemBuilder(ItemFertilizer::new).id("fertilizer"))
                .builder(new BasicItemBuilder(ItemDynamite::new).id("dynamite"))
                .builder(new BasicItemBuilder(ItemBark::new).id("bark"))
                .builder(new BasicItemBuilder(ItemBroadheadArrow::new).id("broadhead_arrow"))
                .builder(new BasicItemBuilder(ItemCompositeBow::new).id("composite_bow"))
                .builder(new BasicItemBuilder(ItemEnderSpectacles::new).id("ender_spectacles"))
                .builder(new BasicItemBuilder(ItemSoulforgedBattleAxe::new).id("steel_battleaxe"))
                .builder(new BasicItemBuilder(ItemSoulforgedMattock::new).id("steel_mattock"))
                .builder(new BasicItemBuilder(ItemSoulforgedSword::new).id("steel_sword"))
                .builder(new BasicItemBuilder(ItemSoulforgedShovel::new).id("steel_shovel"))
                .builder(new BasicItemBuilder(ItemSoulforgedPickaxe::new).id("steel_pickaxe"))
                .builder(new BasicItemBuilder(ItemSoulforgedHoe::new).id("steel_hoe"))
                .builder(new BasicItemBuilder(ItemSoulforgedAxe::new).id("steel_axe"))
                .builder(new BasicItemBuilder(Item::new).id("betterwithboi"))

                .complete());

        //FIXME this
        ItemMaterial.init();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onPostBake(ModelBakeEvent event) {
        BarkModel.BARK = new BarkModel(RenderUtils.getModel(new ResourceLocation(ModLib.MODID, "item/bark")));
        event.getModelRegistry().putObject(BarkModel.LOCATION, BarkModel.BARK);
    }

    @Override
    public int priority() {
        return 99;
    }
}
