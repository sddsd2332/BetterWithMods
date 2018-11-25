package betterwithmods.module.conversion.hunger;

import betterwithmods.BetterWithMods;
import betterwithmods.client.gui.GuiHunger;
import betterwithmods.client.gui.GuiStatus;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemEdibleSeeds;
import betterwithmods.common.items.itemblocks.ItemBlockEdible;
import betterwithmods.common.penalties.FatPenalties;
import betterwithmods.common.penalties.HungerPenalties;
import betterwithmods.library.common.item.creation.BasicItemBuilder;
import betterwithmods.library.common.item.creation.ItemFactory;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.hardcore.needs.HCTools;
import betterwithmods.module.internal.ItemRegistry;
import betterwithmods.module.internal.MiscRegistry;
import betterwithmods.network.BWMNetwork;
import betterwithmods.network.messages.MessageHungerShake;
import betterwithmods.util.PlayerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdibleBlock;
import squeek.applecore.api.hunger.ExhaustionEvent;
import squeek.applecore.api.hunger.HealthRegenEvent;
import squeek.applecore.api.hunger.HungerEvent;
import squeek.applecore.api.hunger.StarvationEvent;

/**
 * Created by primetoxinz on 6/20/17.
 */


public class HCHunger extends Feature {

    @GameRegistry.ObjectHolder("minecraft:pumpkin_seeds")
    public static final Item PUMPKIN_SEEDS = null;
    @GameRegistry.ObjectHolder("minecraft:brown_mushroom")
    public static final Item BROWN_MUSHROOM = null;
    @GameRegistry.ObjectHolder("minecraft:red_mushroom")
    public static final Item RED_MUSHROOM = null;

    private static final DataParameter<Integer> EXHAUSTION_TICK = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    public static float blockBreakExhaustion;
    public static float passiveExhaustion;
    public static int passiveExhaustionTick;

    public static boolean overridePumpkinSeeds;
    public static boolean overrideMushrooms;
    public static HungerPenalties hungerPenalties;
    public static FatPenalties fatPenalties;

    //Adds Exhaustion when Jumping and cancels Jump if too exhausted
    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            player.addExhaustion(0.5f);
        }
    }

    @SubscribeEvent
    public void entityConstruct(EntityEvent.EntityConstructing e) {
        if (e.getEntity() instanceof EntityPlayer) {
            e.getEntity().getDataManager().register(EXHAUSTION_TICK, 0);
        }
    }

    private static int getExhaustionTick(EntityPlayer player) {
        return player.getDataManager().get(EXHAUSTION_TICK);
    }

    private static void setExhaustionTick(EntityPlayer player, int tick) {
        player.getDataManager().set(EXHAUSTION_TICK, tick);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote && event.phase == TickEvent.Phase.START) {
            EntityPlayer player = event.player;

            if (!PlayerUtils.isSurvival(player) || player.world.getDifficulty() == EnumDifficulty.PEACEFUL)
                return;
            int tick = getExhaustionTick(player);

            int totalTicks = passiveExhaustionTick * (PlayerUtils.isSitting(player) ? 2 : 1);

            if (tick > totalTicks) {
                BetterWithMods.getLog().debug("Adding Exhaustion ({}) after {} ticks", passiveExhaustion, totalTicks);
                player.addExhaustion(passiveExhaustion);
                setExhaustionTick(player, 0);
            } else {
                BetterWithMods.getLog().debug(" {} exhaustion ticks", getExhaustionTick(player));
                setExhaustionTick(player, getExhaustionTick(player) + 1);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvest(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (event.isCanceled() || !PlayerUtils.isSurvival(player))
            return;
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = player.getHeldItemMainhand();
        String tooltype = state.getBlock().getHarvestTool(state);
        if (tooltype != null && state.getBlockHardness(world, pos) <= 0 && stack.getItem().getHarvestLevel(stack, tooltype, player, state) < HCTools.noHungerThreshold)
            return; //doesn't consume hunger if using iron tier axes
        player.addExhaustion(blockBreakExhaustion - 0.005f);
    }

    @SubscribeEvent
    public void allowHealthRegen(HealthRegenEvent.AllowRegen event) {
        if (!event.player.world.getGameRules().getBoolean("naturalRegeneration"))
            return;
        //Whether the player can heal
        Event.Result result = MiscRegistry.PENALTY_HANDLERS.canHeal(event.player) ? Event.Result.ALLOW : Event.Result.DENY;
        event.setResult(result);
    }

    //Changes food to correct value.
    @SubscribeEvent
    public void modifyFoodValues(FoodEvent.GetFoodValues event) {
        event.foodValues = FoodHelper.getFoodValue(event.food).orElseGet(() -> new FoodValues(Math.min(event.foodValues.hunger * 3, 60), 0));
    }

    @SubscribeEvent
    public void onFoodStatsAdd(FoodEvent.FoodStatsAddition event) {
        event.setCanceled(true);

        int maxHunger = AppleCoreAPI.accessor.getMaxHunger(event.player);
        int newHunger = Math.min(event.player.getFoodStats().getFoodLevel() + event.foodValuesToBeAdded.hunger, maxHunger);
        AppleCoreAPI.mutator.setHunger(event.player, newHunger);

        float saturationIncrement = event.foodValuesToBeAdded.saturationModifier;
        float newSaturation = Math.min(event.player.getFoodStats().getSaturationLevel() + saturationIncrement, newHunger);
        AppleCoreAPI.mutator.setSaturation(event.player, newSaturation);
    }

    @SubscribeEvent
    public void getPlayerFoodValue(FoodEvent.GetPlayerFoodValues event) {
        if (event.player == null)
            return;
        FoodStats stats = event.player.getFoodStats();
        int playerFoodLevel = stats.getFoodLevel();
        int foodLevel = event.foodValues.hunger;
        int max = AppleCoreAPI.accessor.getMaxHunger(event.player);
        int newFood = (foodLevel + playerFoodLevel);
        if (newFood <= max) {
            event.foodValues = new FoodValues(foodLevel, event.foodValues.saturationModifier);
        } else {
            float fat = event.foodValues.saturationModifier == 0 ? (newFood - max) : event.foodValues.saturationModifier;
            event.foodValues = new FoodValues(foodLevel, fat);
        }
    }

    //Changes exhaustion to reduce food first, then fat.
    @SubscribeEvent
    public void exhaust(ExhaustionEvent.Exhausted event) {
        FoodStats stats = event.player.getFoodStats();
        int saturation = (int) ((stats.getSaturationLevel() - 1) / 6);
        int hunger = stats.getFoodLevel() / 6;
        if (hunger >= saturation) {
            event.deltaSaturation = 0;
            event.deltaHunger = -1;
        } else {
            event.deltaSaturation = -1;
            event.deltaHunger = 0;
        }
    }



    /*--------------------------------------------------*/
    /* Apple core code                                  /*
    /*--------------------------------------------------*/

    @SubscribeEvent
    public void setMaxFood(HungerEvent.GetMaxHunger event) {
        event.maxHunger = 60;
    }

    //Change Health Regen speed to take 30 seconds
    @SubscribeEvent
    public void healthRegenSpeed(HealthRegenEvent.GetRegenTickPeriod event) {
        event.regenTickPeriod = 600;
    }

    //Stop regen from Fat value.
    @SubscribeEvent
    public void denyFatRegen(HealthRegenEvent.AllowSaturatedRegen event) {
        event.setResult(Event.Result.DENY);
    }

    //Shake Hunger bar whenever any exhaustion is given?
    @SubscribeEvent
    public void onExhaustAdd(ExhaustionEvent.ExhaustionAddition event) {
        if (PlayerUtils.isSurvival(event.player)) {
            if (event.deltaExhaustion >= HCHunger.blockBreakExhaustion) {
                if (event.player instanceof EntityPlayerMP)
                    BWMNetwork.INSTANCE.sendTo(new MessageHungerShake(), (EntityPlayerMP) event.player);
                else
                    GuiHunger.INSTANCE.shake();
            }
        }
    }

    @SubscribeEvent
    public void onStarve(StarvationEvent.AllowStarvation event) {
        if (event.player.getFoodStats().getFoodLevel() <= 0 && event.player.getFoodStats().getSaturationLevel() <= 0)
            event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public void onStarve(StarvationEvent.Starve event) {
        event.setCanceled(true);
        event.player.attackEntityFrom(DamageSource.STARVE, 1);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        blockBreakExhaustion = loadProperty("Block Breaking Exhaustion", 0.1f).setComment("Set Exhaustion from breaking a block").get();
        passiveExhaustion = loadProperty("Passive Exhaustion", 3f).setComment("Passive Exhaustion value").get();
        passiveExhaustionTick = loadProperty("Passive Exhaustion Tick", 900).setComment("Passive exhaustion tick time").get();

        overrideMushrooms = loadProperty("Edible Mushrooms", true).setComment("Override Mushrooms to be edible, be careful with the red one ;)").get();
        overridePumpkinSeeds = loadProperty("Edible Pumpkin Seeds", true).setComment("Override Pumpkin Seeds to be edible").get();

        ItemFactory factory = ItemFactory.create();
        if (overridePumpkinSeeds) {
            factory.builder(new BasicItemBuilder(
                    new ItemEdibleSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND, 1, 0).setTranslationKey("seeds_pumpkin")
            ).id("minecraft:pumpkin_seeds"));
        }

        if (overrideMushrooms) {
            factory
                    .builder(new BasicItemBuilder(
                            new ItemBlockEdible(Blocks.BROWN_MUSHROOM, 1, 0, false).setTranslationKey("brown_mushroom")
                    ).id("minecraft:brown_mushroom"))
                    .builder(new BasicItemBuilder(
                            new ItemBlockEdible(Blocks.RED_MUSHROOM, 1, 0, false)
                                    .setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1)
                                    .setTranslationKey("red_mushroom")
                    ).id("minecraft:red_mushroom"));
        }
        ItemRegistry.registerItems(factory.complete());

        GuiStatus.isHungerLoaded = true;
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientSide.class);
    }

    public void registerFoods() {
        FoodHelper.registerFood(new ItemStack(Items.BEEF), 12);
        FoodHelper.registerFood(new ItemStack(Items.PORKCHOP), 12);
        FoodHelper.registerFood(new ItemStack(Items.RABBIT), 12);
        FoodHelper.registerFood(new ItemStack(Items.CHICKEN), 9);
        FoodHelper.registerFood(new ItemStack(Items.MUTTON), 9);
        FoodHelper.registerFood(new ItemStack(Items.FISH), 9);
        FoodHelper.registerFood(new ItemStack(Items.FISH, 1, 1), 9);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_BEEF), 15);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_PORKCHOP), 15);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_RABBIT), 15);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_CHICKEN), 12);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_MUTTON), 12);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_FISH), 12);
        FoodHelper.registerFood(new ItemStack(Items.COOKED_FISH, 1, 1), 12);
        FoodHelper.registerFood(new ItemStack(Items.SPIDER_EYE), 6);
        FoodHelper.registerFood(new ItemStack(Items.ROTTEN_FLESH), 9);
        FoodHelper.registerFood(new ItemStack(Items.MUSHROOM_STEW), 9);
        FoodHelper.registerFood(new ItemStack(Items.BEETROOT_SOUP), 9);
        FoodHelper.registerFood(new ItemStack(Items.RABBIT_STEW), 30);
        FoodHelper.registerFood(new ItemStack(Items.MELON), 2);
        FoodHelper.registerFood(new ItemStack(Items.APPLE), 3);
        FoodHelper.registerFood(new ItemStack(Items.POTATO), 3);
        FoodHelper.registerFood(new ItemStack(Items.CARROT), 3);
        FoodHelper.registerFood(new ItemStack(Items.BEETROOT), 3);
        FoodHelper.registerFood(new ItemStack(Items.BAKED_POTATO), 6);
        FoodHelper.registerFood(new ItemStack(Items.BREAD), 12);

        FoodHelper.registerFood(new ItemStack(Items.GOLDEN_APPLE), 3);
        FoodHelper.registerFood(new ItemStack(Items.GOLDEN_APPLE, 1, 1), 3);
        FoodHelper.registerFood(new ItemStack(Items.GOLDEN_CARROT), 3);
        FoodHelper.registerFood(new ItemStack(BWMItems.BEEF_DINNER), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.BEEF_POTATOES), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_KEBAB), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_KEBAB), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.CHICKEN_SOUP), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.CHOWDER), 15);
        FoodHelper.registerFood(new ItemStack(BWMItems.HEARTY_STEW), 30);
        FoodHelper.registerFood(new ItemStack(BWMItems.PORK_DINNER), 24);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_EGG), 6);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_EGG), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_SCRAMBLED_EGG), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 15);
        FoodHelper.registerFood(new ItemStack(BWMItems.RAW_OMELET), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_OMELET), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.HAM_AND_EGGS), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.TASTY_SANDWICH), 18);
        FoodHelper.registerFood(new ItemStack(BWMItems.CREEPER_OYSTER), 6);
        FoodHelper.registerFood(new ItemStack(BWMItems.KIBBLE), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.WOLF_CHOP), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_WOLF_CHOP), 15);
        FoodHelper.registerFood(new ItemStack(BWMItems.MYSTERY_MEAT), 9);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_MYSTERY_MEAT), 12);
        FoodHelper.registerFood(new ItemStack(BWMItems.BAT_WING), 3);
        FoodHelper.registerFood(new ItemStack(BWMItems.COOKED_BAT_WING), 6);
        FoodHelper.registerFood(new ItemStack(Items.CHORUS_FRUIT), 3, 0, true);

        FoodHelper.registerFood(new ItemStack(BWMItems.DONUT), 3, 1.5f, true);
        FoodHelper.registerFood(new ItemStack(BWMItems.APPLE_PIE), 9, 12, true);
        FoodHelper.registerFood(new ItemStack(BWMItems.CHOCOLATE), 6, 3, true);
        FoodHelper.registerFood(new ItemStack(Items.COOKIE), 3, 3, true);
        FoodHelper.registerFood(new ItemStack(Items.PUMPKIN_PIE), 9, 12, true);
        FoodHelper.registerFood(new ItemStack(Items.CAKE), 4, 12, true);
        FoodHelper.registerFood(new ItemStack(HCHunger.PUMPKIN_SEEDS), 1);

        ((IEdibleBlock) Blocks.CAKE).setEdibleAtMaxHunger(true);
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        MiscRegistry.PENALTY_HANDLERS.add(hungerPenalties = new HungerPenalties(this));
        MiscRegistry.PENALTY_HANDLERS.add(fatPenalties = new FatPenalties(this));


        registerFoods();
    }

    public String getDescription() {
        return "Revamps the hunger system.";
    }

    @SideOnly(Side.CLIENT)
    public static class ClientSide {

        //Replaces Hunger Gui with HCHunger
        @SubscribeEvent
        public static void replaceHungerGui(RenderGameOverlayEvent.Pre event) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                event.setCanceled(true);
                GuiHunger.INSTANCE.draw();
            }
        }

    }

    @Override
    public boolean hasEvent() {
        return true;
    }

}




