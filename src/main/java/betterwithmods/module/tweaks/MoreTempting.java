package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.EntityIngredientRelationRegistry;
import betterwithmods.common.entity.ai.eat.EntityAITempt;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.EntityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;


public class MoreTempting extends Feature {

    public static EntityIngredientRelationRegistry REGISTRY = new EntityIngredientRelationRegistry();

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            if (entity instanceof EntityAnimal) {
                EntityAnimal animal = ((EntityAnimal) entity);
                Ingredient ingredient = REGISTRY.findIngredient(animal);
                if (ingredient != null) {
                    EntityUtils.removeTask(animal, net.minecraft.entity.ai.EntityAITempt.class);
                    animal.tasks.addTask(3, new EntityAITempt(animal, 1.5, false, ingredient));
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Add more valid items for tempting animals to follow. Sheep and cows follow Tall Grass or Wheat." +
                " Chickens follow most seeds." +
                " Pigs will follow Wheat, Potatoes, Beets, Chocolate";
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        REGISTRY.addPredicateEntry(new ResourceLocation(ModLib.MODID, "chicken"), e -> e instanceof EntityChicken)
                .addIngredient(new OreIngredient("seed"));
        REGISTRY.addPredicateEntry(new ResourceLocation(ModLib.MODID, "pig"), e -> e instanceof EntityPig)
                .addIngredient(Ingredient.fromItems(BWMItems.CHOCOLATE, Items.CARROT, Items.POTATO, Items.BEETROOT, Items.WHEAT));
        REGISTRY.addPredicateEntry(new ResourceLocation(ModLib.MODID, "herd"), e -> e instanceof EntitySheep || e instanceof EntityCow)
                .addIngredient(Ingredient.fromStacks(new ItemStack(Items.WHEAT), new ItemStack(Blocks.TALLGRASS)));
    }

    @Override
    public String[] getIncompatibleMods() {
        return new String[]{"easybreeding"};
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

}
