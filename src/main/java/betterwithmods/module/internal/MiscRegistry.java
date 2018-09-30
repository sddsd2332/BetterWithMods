package betterwithmods.module.internal;

import betterwithmods.api.BWMAPI;
import betterwithmods.api.capabilities.CapabilityAxle;
import betterwithmods.api.capabilities.CapabilityMechanicalPower;
import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.api.tile.dispenser.IBehaviorEntity;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.advancements.BWAdvancements;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockDetector;
import betterwithmods.common.blocks.BlockHemp;
import betterwithmods.common.blocks.behaviors.BehaviorDiodeDispense;
import betterwithmods.common.blocks.behaviors.BehaviorSilkTouch;
import betterwithmods.common.blocks.behaviors.DispenserBehaviorDynamite;
import betterwithmods.common.entity.EntityMiningCharge;
import betterwithmods.common.penalties.PenaltyHandlerRegistry;
import betterwithmods.common.registry.BellowsManager;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.lib.ModLib;
import betterwithmods.lib.ReflectionLib;
import betterwithmods.library.modularity.impl.RequiredFeature;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ListUtils;
import betterwithmods.library.utils.WeatherUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.blockstate.PredicateBlockStateIngredient;
import betterwithmods.library.utils.ingredient.collections.BlockStateIngredientSet;
import betterwithmods.util.MechanicalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class MiscRegistry extends RequiredFeature {

    public static final Fluid MILK = new Fluid("milk", new ResourceLocation(ModLib.MODID, "blocks/milk_still"), new ResourceLocation(ModLib.MODID, "blocks/milk_flowing"));
    public static final PenaltyHandlerRegistry PENALTY_HANDLERS = new PenaltyHandlerRegistry();

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        //FIXME Initialize api
        BWMAPI.IMPLEMENTATION = new MechanicalUtil();

        //FIXME Registry capabilities
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new CapabilityMechanicalPower.Impl(), CapabilityMechanicalPower.Default::new);
        CapabilityManager.INSTANCE.register(IAxle.class, new CapabilityAxle.Impl(), CapabilityAxle.Default::new);

        //FIXME Registry kiln blocks
        KilnStructureManager.registerKilnBlock(Blocks.BRICK_BLOCK.getDefaultState());
        KilnStructureManager.registerKilnBlock(Blocks.NETHER_BRICK.getDefaultState());

        FluidRegistry.registerFluid(MILK);
        //FIXME migrate
        BWAdvancements.registerAdvancements();
        MiscRegistry.registerBlockDispenserBehavior();
    }


    @Override
    public void onInit(FMLInitializationEvent event) {
        registerHeatSources();
        registerBUDBlacklist();
        registerDetectorHandlers();
        registerFireInfo();
        BellowsManager.init();
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

    public static void registerHeatSources() {
        BWMHeatRegistry.addHeatSource(new BlockIngredient(Blocks.FIRE, Items.AIR), 1);
        BWMHeatRegistry.addHeatSource(new BlockIngredient(BWMBlocks.STOKED_FLAME, Items.AIR), 2);
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


    public static void registerFireInfo(BlockStateIngredient ingredient, int encouragement, int flammability) {
        for (IBlockState state : ingredient.getStates()) {
            Blocks.FIRE.setFireInfo(state.getBlock(), encouragement, flammability);
        }
    }

}
