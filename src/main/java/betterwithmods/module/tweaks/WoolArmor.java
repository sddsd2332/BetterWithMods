package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.items.tools.ItemWoolArmor;
import betterwithmods.common.registry.crafting.RecipeArmorDye;
import betterwithmods.module.Feature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WoolArmor extends Feature {
    public static final Item WOOL_HELMET = new ItemWoolArmor(EntityEquipmentSlot.HEAD).setRegistryName("wool_helmet");
    public static final Item WOOL_CHEST = new ItemWoolArmor(EntityEquipmentSlot.CHEST).setRegistryName("wool_chest");
    public static final Item WOOL_PANTS = new ItemWoolArmor(EntityEquipmentSlot.LEGS).setRegistryName("wool_pants");
    public static final Item WOOL_BOOTS = new ItemWoolArmor(EntityEquipmentSlot.FEET).setRegistryName("wool_boots");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMItems.registerItem(WOOL_BOOTS);
        BWMItems.registerItem(WOOL_CHEST);
        BWMItems.registerItem(WOOL_HELMET);
        BWMItems.registerItem(WOOL_PANTS);

        BWMRecipes.addRecipe(new RecipeArmorDye(Ingredient.fromItems(WOOL_HELMET, WOOL_CHEST, WOOL_PANTS, WOOL_BOOTS)));
    }

    @Override
    public String getFeatureDescription() {
        return "Add Wool Armor";
    }
}
