package betterwithmods.module.recipes;

import betterwithmods.BetterWithMods;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.block.recipe.builder.KilnRecipeBuilder;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.hardcore.needs.HCCooking;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.miniblocks.client.CamoModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by primetoxinz on 5/16/17.
 */

public class KilnRecipes extends Feature {

    @SubscribeEvent
    public void formKiln(BlockEvent.NeighborNotifyEvent event) {
        BlockPos up = event.getPos().up();
        World world = event.getWorld();
        if (KilnStructureManager.isKilnBlock(world, up) && BWMHeatRegistry.getHeat(world, event.getPos()) > 0) {
            KilnStructureManager.createKiln(world, up);
        }
    }

    @SubscribeEvent
    public void onKilnPlace(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock().isAir(event.getPlacedBlock(), event.getWorld(), event.getPos())) {
            KilnStructureManager.createKiln(event.getWorld(), event.getPos());
        }
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        KilnRecipeBuilder builder = new KilnRecipeBuilder();

        RecipeRegistry.KILN.registerAll(
                builder.stoked()
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.CRUCIBLE))
                        .outputs(new ItemStack(BWMBlocks.CRUCIBLE)).build(),
                builder.stoked()
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.PLANTER))
                        .outputs(BlockPlanter.getStack(BlockPlanter.Type.EMPTY)).build(),
                builder.stoked()
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.URN))
                        .outputs(new ItemStack(BWMBlocks.URN)).build(),
                builder.stoked()
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.VASE))
                        .outputs(BlockVase.getStack(EnumDyeColor.WHITE)).build(),
                builder.stoked()
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.BRICK))
                        .outputs(new ItemStack(Items.BRICK)).build(),
                builder.stoked()
                        .input(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.NETHER_BRICK))
                        .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE)).build(),
                builder.stoked()
                        .input(new ItemStack(Blocks.CLAY))
                        .outputs(new ItemStack(Blocks.HARDENED_CLAY)).build(),
                builder.stoked()
                        .input(new ItemStack(BWMBlocks.NETHER_CLAY))
                        .outputs(BlockAesthetic.getStack(BlockAesthetic.Type.NETHERCLAY)).build()
        );

        int foodModifier = BetterWithMods.MODULE_LOADER.isFeatureEnabled(HCCooking.class) ? 1 : 2;
        RecipeRegistry.KILN.registerAll(
                builder.input(BlockRawPastry.getStack(BlockRawPastry.Type.CAKE)).outputs(new ItemStack(Items.CAKE, foodModifier)).build(),
                builder.input(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD)).outputs(new ItemStack(Items.BREAD, foodModifier)).build(),
                builder.input(BlockRawPastry.getStack(BlockRawPastry.Type.COOKIE)).outputs(new ItemStack(Items.COOKIE, 8 * foodModifier)).build(),
                builder.input(BlockRawPastry.getStack(BlockRawPastry.Type.PUMPKIN)).outputs(new ItemStack(Items.PUMPKIN_PIE, foodModifier)).build(),
                builder.input(BlockRawPastry.getStack(BlockRawPastry.Type.APPLE)).outputs(new ItemStack(BWMItems.APPLE_PIE, foodModifier)).build(),
                builder.input(BlockRawPastry.getStack(BlockRawPastry.Type.MELON)).outputs(new ItemStack(BWMItems.MELON_PIE, foodModifier)).build()
        );
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {
//        event.getModelRegistry().putObject(new ModelResourceLocation(ModLib.MODID + ":kiln", "normal"), new CamoModel(RenderUtils.getModel(new ResourceLocation("minecraft", "block/cube")), "kiln"));
    }
}

