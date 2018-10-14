package betterwithmods.module.internal;

import betterwithmods.api.tile.dispenser.IBehaviorCollect;
import betterwithmods.api.tile.dispenser.IBehaviorEntity;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockMiningCharge;
import betterwithmods.common.blocks.behaviors.*;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import betterwithmods.library.lib.ReflectionLib;
import betterwithmods.library.utils.GlobalUtils;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ListUtils;
import betterwithmods.library.utils.ingredient.EntityIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.library.utils.ingredient.collections.BaseIngredientMap;
import betterwithmods.library.utils.ingredient.collections.BlockIngredientMap;
import betterwithmods.library.utils.ingredient.collections.EntityIngredientMap;
import betterwithmods.library.utils.ingredient.collections.IngredientMap;
import com.google.common.collect.Sets;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class AdvancedDispenserRegistry extends RequiredFeature {

    public static IngredientMap<IBehaviorDispenseItem> BLOCK_DISPENSER_REGISTRY = new IngredientMap<>(new BehaviorDefaultDispenseBlock());
    public static BlockIngredientMap<IBehaviorCollect> BLOCK_COLLECT_REGISTRY = new BlockIngredientMap<>(new BehaviorBreakBlock());
    public static EntityIngredientMap<IBehaviorEntity> ENTITY_COLLECT_REGISTRY = new EntityIngredientMap<>(new BehaviorEntity());


    @Override
    public void onInit(FMLInitializationEvent event) {
        BLOCK_DISPENSER_REGISTRY.put(Items.REPEATER, new BehaviorDiodeDispense());
        BLOCK_DISPENSER_REGISTRY.put(Items.COMPARATOR, new BehaviorDiodeDispense());
        BLOCK_DISPENSER_REGISTRY.put(BWMItems.DYNAMITE, new DispenserBehaviorDynamite());
        BLOCK_DISPENSER_REGISTRY.put(BWMBlocks.MINING_CHARGE, BlockMiningCharge::dispenser);
        BLOCK_COLLECT_REGISTRY.put(Blocks.STONE, new BehaviorSilkTouch());
        BLOCK_COLLECT_REGISTRY.put(Blocks.LOG, new BehaviorSilkTouch());
        BLOCK_COLLECT_REGISTRY.put(Blocks.LOG2, new BehaviorSilkTouch());

        //Dispenser Minecarts
        IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = ReflectionHelper.getPrivateValue(ItemMinecart.class, null, ReflectionLib.MINECART_DISPENSER_BEHAVIOR);
        for (Item minecart : Sets.newHashSet(Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.HOPPER_MINECART, Items.TNT_MINECART, Items.COMMAND_BLOCK_MINECART)) {
            BLOCK_DISPENSER_REGISTRY.put(minecart, MINECART_DISPENSER_BEHAVIOR);
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
            ENTITY_COLLECT_REGISTRY.put(new EntityIngredient(loc), MINECART_COLLECT_BEHAVIOR);
        }


        ResourceLocation SHEEP = new ResourceLocation("minecraft:sheep");
        ENTITY_COLLECT_REGISTRY.put(new EntityIngredient(SHEEP), (world, pos, entity, stack) -> {
            EntitySheep sheep = (EntitySheep) entity;
            if (sheep.isShearable(new ItemStack(Items.SHEARS), world, pos)) {
                return ListUtils.asNonnullList(sheep.onSheared(new ItemStack(Items.SHEARS), world, pos, 0));
            }
            return NonNullList.create();
        });

        ResourceLocation CHICKEN = new ResourceLocation("minecraft:chicken");
        ENTITY_COLLECT_REGISTRY.put(new EntityIngredient(CHICKEN), (world, pos, entity, stack) -> {
            if (((EntityAgeable) entity).isChild())
                return NonNullList.create();
            InventoryUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.FEATHER, 1 + world.rand.nextInt(2)));
            world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
            entity.setDead();
            return ListUtils.asNonnullList(new ItemStack(Items.EGG));
        });

        ResourceLocation COW = new ResourceLocation("minecraft:cow");
        ENTITY_COLLECT_REGISTRY.put(new EntityIngredient(COW), (world, pos, entity, stack) -> {
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
}
