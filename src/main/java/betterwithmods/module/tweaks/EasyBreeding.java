package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.ai.EntityAISearchFood;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.creatures.chicken.HCChickens;
import betterwithmods.util.ReflectionLib;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class EasyBreeding extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Animals will pick up breeding items off of the ground as necessary. " +
                "Additionally makes sheep and cows follow Tall Grass or Wheat" +
                "Chickens follow most seeds" +
                "Pigs follow and will breed with Wheat, Potatoes, Beets, Chocolate and will breed with Kibble";
    }

    public static Set<Item> CHICKEN;
    public static Set<Item> PIG;
    public static Set<Item> HERD_ANIMAL;

    public static Set<Item> getTempted(EntityAnimal entity) {
        if (entity instanceof EntityPig)
            return PIG;
        if (entity instanceof EntitySheep || entity instanceof EntityCow)
            return HERD_ANIMAL;
        if (entity instanceof EntityChicken)
            return CHICKEN;
        return Sets.newHashSet();
    }

    @GameRegistry.ObjectHolder("betterwithmods:hemp")
    public static final Item HEMP_SEED = null;

    public static HashMap<Item,IExtraFoodItem> EXTRA_FOOD_ITEMS = new HashMap<>();

    public static boolean isOtherValidFood(ItemStack stack, EntityLivingBase animal) {
        Item item = stack.getItem();
        return EXTRA_FOOD_ITEMS.containsKey(item) && EXTRA_FOOD_ITEMS.get(item).canEat(stack, animal);
    }

    public static boolean eatFood(ItemStack stack, EntityLivingBase animal) {
        Item item = stack.getItem();
        return EXTRA_FOOD_ITEMS.containsKey(item) && EXTRA_FOOD_ITEMS.get(item).eat(stack, animal);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Set<Item> items = ReflectionHelper.getPrivateValue(EntityPig.class, null, ReflectionLib.ENTITY_PIG_TEMPTATIONITEM);
        items.addAll(Sets.newHashSet(BWMItems.CHOCOLATE, BWMItems.KIBBLE));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        CHICKEN = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, HEMP_SEED);
        PIG = Sets.newHashSet(BWMItems.CHOCOLATE, Items.CARROT, Items.POTATO, Items.BEETROOT, Items.WHEAT);
        HERD_ANIMAL = Sets.newHashSet(Items.WHEAT, Item.getItemFromBlock(Blocks.TALLGRASS));
    }

    public static void removeTask(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        entity.tasks.taskEntries.removeIf(task -> clazz.isAssignableFrom(task.action.getClass()));
    }

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            if (entity instanceof EntityAnimal) {
                EntityAnimal animal = ((EntityAnimal) entity);
                if (!ModuleLoader.isFeatureEnabled(HCChickens.class) || !(event.getEntity() instanceof EntityChicken)) {
                    animal.tasks.addTask(3, new EntityAISearchFood(((EntityAnimal) entity)));
                }
                if (!getTempted(animal).isEmpty()) {
                    removeTask(animal, EntityAITempt.class);
                    animal.tasks.addTask(3, new EntityAITempt(animal, 1.25D, false, getTempted(animal)));
                }

            }
        }

    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack foodStack = event.getItemStack();
        IExtraFoodItem extraFoodItem = EXTRA_FOOD_ITEMS.get(foodStack.getItem());
        Entity target = event.getTarget();
        if (target instanceof EntityLivingBase && extraFoodItem != null && extraFoodItem.canEat(foodStack,(EntityLivingBase) target)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            extraFoodItem.eat(foodStack,(EntityLivingBase) target);
            foodStack.shrink(1);
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String[] getIncompatibleMods() {
        return new String[]{"easyBreeding"};
    }

    public interface IExtraFoodItem
    {
        boolean canEat(ItemStack item, EntityLivingBase eater);

        boolean eat(ItemStack item, EntityLivingBase eater);
    }
}
