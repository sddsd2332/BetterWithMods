package betterwithmods.module.gameplay;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.module.Feature;
import betterwithmods.module.hardcore.needs.HCCooking;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class KilnRecipes extends Feature {
    public KilnRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.CRUCIBLE), new ItemStack(BWMBlocks.CRUCIBLE));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.PLANTER), BlockPlanter.getStack(BlockPlanter.EnumType.EMPTY));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.URN), new ItemStack(BWMBlocks.URN));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.VASE), BlockVase.getStack(EnumDyeColor.WHITE));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.BRICK), new ItemStack(Items.BRICK));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.NETHER_BRICK), ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE));

        BWRegistry.KILN.addStokedRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Blocks.HARDENED_CLAY));
        BWRegistry.KILN.addStokedRecipe(new ItemStack(BWMBlocks.NETHER_CLAY), BlockAesthetic.getStack(BlockAesthetic.EnumType.NETHERCLAY));

        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 2));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE, 1));

        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 2), 0.1F);
        int foodModifier = BWMod.MODULE_LOADER.isFeatureEnabled(HCCooking.class) ? 1 : 2;
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), IntStream.range(0,foodModifier).mapToObj(i -> new ItemStack(Items.CAKE)).collect(Collectors.toList()));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD,foodModifier));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8*foodModifier));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, foodModifier));
        BWRegistry.KILN.addStokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE, foodModifier));

        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE, 1), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, 1), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE, 1), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD, 1), 0.1F);
    }

    @SubscribeEvent
    public void formKiln(BlockEvent.NeighborNotifyEvent event) {
        KilnStructureManager.createKiln(event.getWorld(), event.getPos());
    }

    @SubscribeEvent
    public void onKilnPlace(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock() != Blocks.AIR)
            KilnStructureManager.createKiln(event.getWorld(), event.getPos());
    }


    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}

