package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.api.BWMAPI;
import betterwithmods.api.capabilities.CapabilityAxle;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.api.tile.dispenser.IBehaviorEntity;
import betterwithmods.common.advancements.BWAdvancements;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockDetector;
import betterwithmods.common.blocks.BlockHemp;
import betterwithmods.common.blocks.behaviors.BehaviorDiodeDispense;
import betterwithmods.common.blocks.behaviors.BehaviorSilkTouch;
import betterwithmods.common.blocks.behaviors.DispenserBehaviorDynamite;
import betterwithmods.common.entity.EntityMiningCharge;
import betterwithmods.common.fluid.BWFluidRegistry;
import betterwithmods.common.penalties.PenaltyHandlerRegistry;
import betterwithmods.common.potion.BWPotion;
import betterwithmods.common.potion.PotionSlowfall;
import betterwithmods.common.potion.PotionTruesight;
import betterwithmods.common.registry.BellowsManager;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.anvil.CraftingManagerAnvil;
import betterwithmods.common.registry.block.managers.CrafingManagerKiln;
import betterwithmods.common.registry.block.managers.CraftingManagerSaw;
import betterwithmods.common.registry.block.managers.CraftingManagerTurntable;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ListUtils;
import betterwithmods.library.utils.WeatherUtils;
import betterwithmods.common.registry.bulk.manager.CraftingManagerMill;
import betterwithmods.common.registry.bulk.manager.CraftingManagerPot;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.common.registry.hopper.filters.HopperFilters;
import betterwithmods.common.registry.hopper.manager.CraftingManagerHopper;
import betterwithmods.lib.ModLib;
import betterwithmods.lib.ReflectionLib;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.blockstate.PredicateBlockStateIngredient;
import betterwithmods.library.utils.ingredient.collections.BlockStateIngredientSet;
import betterwithmods.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class BWMRegistry {

    public static final PenaltyHandlerRegistry PENALTY_HANDLERS = new PenaltyHandlerRegistry();

    public static final CraftingManagerPot CAULDRON = new CraftingManagerPot();
    public static final CraftingManagerPot CRUCIBLE = new CraftingManagerPot();
    public static final CraftingManagerMill MILLSTONE = new CraftingManagerMill();
    public static final CraftingManagerSaw WOOD_SAW = new CraftingManagerSaw();
    public static final CrafingManagerKiln KILN = new CrafingManagerKiln();
    public static final CraftingManagerTurntable TURNTABLE = new CraftingManagerTurntable();
    public static final CraftingManagerHopper FILTERED_HOPPER = new CraftingManagerHopper();
    public static final CraftingManagerAnvil ANVIL = new CraftingManagerAnvil();

    public static final HopperFilters HOPPER_FILTERS = new HopperFilters();

    @GameRegistry.ObjectHolder("betterwithmods:true_sight")
    public static final Potion POTION_TRUESIGHT = null;
    @GameRegistry.ObjectHolder("betterwithmods:fortune")
    public static final Potion POTION_FORTUNE = null;
    @GameRegistry.ObjectHolder("betterwithmods:looting")
    public static final Potion POTION_LOOTING = null;
    @GameRegistry.ObjectHolder("betterwithmods:slow_fall")
    public static final Potion POTION_SLOWFALL = null;

    public static final BannerTextures.Cache WINDMILLS = new BannerTextures.Cache("betterwithmods:W", new ResourceLocation(ModLib.MODID, "textures/blocks/windmills/banner.png"), "betterwithmods:textures/blocks/windmills/");

    private static int availableEntityId = 0;

    static {
        BWMAPI.IMPLEMENTATION = new MechanicalUtil();
    }

    public static void onPreInit() {
        BWFluidRegistry.registerFluids();
        BWAdvancements.registerAdvancements();
        BWMItems.registerItems();
        BWMBlocks.registerTileEntities();
        BWMRegistry.registerBlockDispenserBehavior();
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new CapabilityMechanicalPower.Impl(), CapabilityMechanicalPower.Default::new);
        CapabilityManager.INSTANCE.register(IAxle.class, new CapabilityAxle.Impl(), CapabilityAxle.Default::new);
        KilnStructureManager.registerKilnBlock(Blocks.BRICK_BLOCK.getDefaultState());
        KilnStructureManager.registerKilnBlock(Blocks.NETHER_BRICK.getDefaultState());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BWMItems.getItems().forEach(event.getRegistry()::register);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void postPreInit(RegistryEvent.Register<Item> event) {
        BWMOreDictionary.registerOres();
        BWMOreDictionary.oreGathering();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>) event.getRegistry();

        for (IRecipe recipe : reg) {
            for (Pattern pattern : BWMRecipes.REMOVE_BY_REGEX) {
                if (recipe.getRegistryName() != null) {
                    Matcher matcher = pattern.matcher(recipe.getRegistryName().toString());
                    if (matcher.matches()) {
                        reg.remove(recipe.getRegistryName());
                    }
                }
            }
            for (ResourceLocation loc : BWMRecipes.REMOVE_RECIPE_BY_RL) {
                if (loc.equals(recipe.getRegistryName()))
                    reg.remove(recipe.getRegistryName());
            }
            for (ItemStack output : BWMRecipes.REMOVE_RECIPE_BY_OUTPUT) {
                if (InventoryUtils.matches(recipe.getRecipeOutput(), output)) {
                    reg.remove(recipe.getRegistryName());
                }
            }
            for (List<Ingredient> inputs : BWMRecipes.REMOVE_RECIPE_BY_INPUT) {
                if (InventoryUtils.containsIngredient(recipe.getIngredients(), inputs)) {
                    reg.remove(recipe.getRegistryName());
                }
            }
        }

        BWMod.MODULE_LOADER.registerRecipes(event);
    }

    public static void init() {
        BWMRegistry.registerHeatSources();
        BWMOreDictionary.registerOres();
        BWMRegistry.registerBUDBlacklist();
        BWMRegistry.registerDetectorHandlers();
    }

    public static void postInit() {
        BellowsManager.postInit();
    }

    public static void registerBlockDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(BWMItems.DYNAMITE, new DispenserBehaviorDynamite());
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(BWMItems.DYNAMITE, new DispenserBehaviorDynamite());
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Items.REPEATER, new BehaviorDiodeDispense());
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Items.COMPARATOR, new BehaviorDiodeDispense());
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Item.getItemFromBlock(BWMBlocks.MINING_CHARGE),
                (source, stack) -> {
                    World worldIn = source.getWorld();
                    EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
                    BlockPos pos = source.getBlockPos().offset(facing);
                    EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, pos.getX() + 0.5F, pos.getY(),
                            pos.getZ() + 0.5F, null, facing);
                    miningCharge.setNoGravity(false);
                    worldIn.spawnEntity(miningCharge);
                    worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ,
                            SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return stack;
                });
        BlockBDispenser.BLOCK_COLLECT_REGISTRY.putObject(Blocks.STONE, new BehaviorSilkTouch());
        BlockBDispenser.BLOCK_COLLECT_REGISTRY.putObject(Blocks.LOG, new BehaviorSilkTouch());
        BlockBDispenser.BLOCK_COLLECT_REGISTRY.putObject(Blocks.LOG2, new BehaviorSilkTouch());

        //Dispenser Minecarts
        IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = ReflectionHelper.getPrivateValue(ItemMinecart.class, null, ReflectionLib.MINECART_DISPENSER_BEHAVIOR);
        for (Item minecart : Sets.newHashSet(Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.HOPPER_MINECART, Items.TNT_MINECART, Items.COMMAND_BLOCK_MINECART)) {
            BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(minecart, MINECART_DISPENSER_BEHAVIOR);
        }

        IBehaviorEntity MINECART_COLLECT_BEHAVIOR = (world, pos, entity, stack) -> {
            EntityMinecart minecart = (EntityMinecart) entity;

            if (minecart instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, minecart, (IInventory) minecart);
            }

            IBlockState state = minecart.getDisplayTile();
            if (state == Blocks.LIT_FURNACE.getDefaultState())
                state = Blocks.FURNACE.getDefaultState();
            ItemStack tile = GlobalUtils.getStackFromState(state);
            minecart.setDead();

            return ListUtils.asNonnullList(new ItemStack(Items.MINECART), tile);
        };

        for (String name : Sets.newHashSet("commandblock_minecart", "minecart", "chest_minecart", "furnace_minecart", "tnt_minecart", "hopper_minecart", "spawner_minecart")) {
            ResourceLocation loc = new ResourceLocation(name);
            BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(loc, MINECART_COLLECT_BEHAVIOR);
        }


        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(new ResourceLocation("minecraft:sheep"), (world, pos, entity, stack) -> {
            EntitySheep sheep = (EntitySheep) entity;
            if (sheep.isShearable(new ItemStack(Items.SHEARS), world, pos)) {
                return ListUtils.asNonnullList(sheep.onSheared(new ItemStack(Items.SHEARS), world, pos, 0));
            }
            return NonNullList.create();
        });
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(new ResourceLocation("minecraft:chicken"), (world, pos, entity, stack) -> {
            if (((EntityAgeable) entity).isChild())
                return NonNullList.create();
            InventoryUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.FEATHER, 1 + world.rand.nextInt(2)));
            world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
            entity.setDead();
            return ListUtils.asNonnullList(new ItemStack(Items.EGG));
        });
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(new ResourceLocation("minecraft:cow"), (world, pos, entity, stack) -> {
            if (((EntityAgeable) entity).isChild())
                return NonNullList.create();
            if (stack.isItemEqual(new ItemStack(Items.BUCKET))) {
                stack.shrink(1);
                world.playSound(null, pos, SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                InventoryUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.MILK_BUCKET));
            }
            return NonNullList.create();
        });


    }


    public static void registerHeatSources() {
        BWMHeatRegistry.addHeatSource(new BlockIngredient(Blocks.FIRE, Items.AIR), 1);
        BWMHeatRegistry.addHeatSource(new BlockIngredient(BWMBlocks.STOKED_FLAME, Items.AIR), 2);
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(registerPotion(new PotionTruesight("true_sight", true, 14270531).setIconIndex(4, 1)));
        event.getRegistry().register(registerPotion(new BWPotion("fortune", true, 14270531).setIconIndex(5, 2)));
        event.getRegistry().register(registerPotion(new BWPotion("looting", true, 14270531).setIconIndex(6, 2)));
        event.getRegistry().register(registerPotion(new PotionSlowfall("slow_fall", true, 0xF46F20).setIconIndex(4, 1)));
    }

    private static Potion registerPotion(Potion potion) {
        if (potion.getRegistryName() != null) {
            String potionName = potion.getRegistryName().getPath();
            potion.setPotionName("betterwithmods.effect." + potionName);
        }
        return potion;

    }

    private static void registerFireInfo() {

        Blocks.FIRE.setFireInfo(BWMBlocks.WOODEN_AXLE, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.WOODEN_BROKEN_GEARBOX, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.WOODEN_GEARBOX, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.HORIZONTAL_WINDMILL, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.VERTICAL_WINDMILL, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.WATERWHEEL, 5, 20);
        Blocks.FIRE.setFireInfo(BWMBlocks.VINE_TRAP, 5, 20);
        //TODO 1.13 block of nethercoal

        registerFireInfo(new BlockStateIngredient("blockCandle"), 5, 20);
        registerFireInfo(new BlockStateIngredient("slats"), 5, 20);
        registerFireInfo(new BlockStateIngredient("grates"), 5, 20);
    }

    private static void registerBUDBlacklist() {
        BlockBUD.BLACKLIST = new BlockStateIngredientSet(
                new BlockIngredient(Blocks.REDSTONE_WIRE, Items.REDSTONE),
                new BlockIngredient(Blocks.POWERED_REPEATER, Items.REPEATER),
                new BlockIngredient(Blocks.UNPOWERED_REPEATER, Items.REPEATER),
                new BlockIngredient(Blocks.UNLIT_REDSTONE_TORCH),
                new BlockIngredient(Blocks.REDSTONE_TORCH),
                new BlockDropIngredient(new ItemStack(BWMBlocks.LIGHT)),
                new BlockDropIngredient(new ItemStack(BWMBlocks.BUDDY_BLOCK))
        );
    }

    private static void registerDetectorHandlers() {
        BlockDetector.DETECTION_HANDLERS = Sets.newHashSet(
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(WeatherUtils::isPrecipitationAt), facing -> facing == EnumFacing.UP),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> world.getBlockState(pos).getMaterial().isSolid()))),
                new BlockDetector.IngredientDetection(new BlockDropIngredient(new ItemStack(Items.REEDS))),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> world.getBlockState(pos).getBlock() instanceof BlockVine))),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> world.getBlockState(pos).getBlock().equals(BWMBlocks.LIGHT_SOURCE)))),
                new BlockDetector.IngredientDetection(new BlockIngredient(Lists.newArrayList(BWMBlocks.HEMP.getDefaultState().withProperty(BlockHemp.TOP, true)), Lists.newArrayList(new ItemStack(BWMBlocks.HEMP)))),
                new BlockDetector.EntityDetection(),
                new BlockDetector.IngredientDetection(new PredicateBlockStateIngredient(((world, pos) -> {
                    BlockPos downOffset = pos.down();
                    IBlockState downState = world.getBlockState(downOffset);
                    Block downBlock = downState.getBlock();
                    if (!(downBlock instanceof BlockHemp) && downBlock instanceof BlockCrops) {
                        return ((BlockCrops) downBlock).isMaxAge(downState);
                    } else if (downBlock == Blocks.NETHER_WART) {
                        return downState.getValue(BlockNetherWart.AGE) >= 3;
                    }
                    return false;
                })))
        );
    }

    public static void registerFireInfo(BlockStateIngredient ingredient, int encouragement, int flammability) {
        for (IBlockState state : ingredient.getStates()) {
            Blocks.FIRE.setFireInfo(state.getBlock(), encouragement, flammability);
        }
    }

}


