package betterwithmods.module.recipes;

import betterwithmods.BetterWithMods;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.KilnStructureManager;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by primetoxinz on 5/16/17.
 */
@Mod.EventBusSubscriber
public class KilnRecipes extends Feature {

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @SubscribeEvent
    public static void formKiln(BlockEvent.NeighborNotifyEvent event) {
        BlockPos up = event.getPos().up();
        World world = event.getWorld();
        if (KilnStructureManager.isKilnBlock(world.getBlockState(up)) && BWMHeatRegistry.getHeat(world, event.getPos()) > 0) {
            KilnStructureManager.createKiln(world, up);
        }
    }

    @SubscribeEvent
    public static void onKilnPlace(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock().isAir(event.getPlacedBlock(), event.getWorld(), event.getPos())) {
            KilnStructureManager.createKiln(event.getWorld(), event.getPos());
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        RecipeRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.CRUCIBLE), new ItemStack(BWMBlocks.CRUCIBLE));
        RecipeRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.PLANTER), BlockPlanter.getStack(BlockPlanter.Type.EMPTY));
        RecipeRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.URN), new ItemStack(BWMBlocks.URN));
        RecipeRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.VASE), BlockVase.getStack(EnumDyeColor.WHITE));
        RecipeRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.BRICK), new ItemStack(Items.BRICK));
        RecipeRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.Type.NETHER_BRICK), ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE));

        RecipeRegistry.KILN.addStokedRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Blocks.HARDENED_CLAY));
        RecipeRegistry.KILN.addStokedRecipe(new ItemStack(BWMBlocks.NETHER_CLAY), BlockAesthetic.getStack(BlockAesthetic.Type.NETHERCLAY));

        int foodModifier = BetterWithMods.MODULE_LOADER.isFeatureEnabled(HCCooking.class) ? 1 : 2;
        RecipeRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.Type.CAKE), IntStream.range(0, foodModifier).mapToObj(i -> new ItemStack(Items.CAKE)).collect(Collectors.toList()));
        RecipeRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD), new ItemStack(Items.BREAD, foodModifier));
        RecipeRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.Type.COOKIE), new ItemStack(Items.COOKIE, 8 * foodModifier));
        RecipeRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.Type.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, foodModifier));
        RecipeRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.Type.APPLE), new ItemStack(BWMItems.APPLE_PIE, foodModifier));
        RecipeRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.Type.MELON), new ItemStack(BWMItems.MELON_PIE, foodModifier));
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {
        CamoModel.KILN = new CamoModel(RenderUtils.getModel(new ResourceLocation("minecraft", "block/cube")));
        event.getModelRegistry().putObject(new ModelResourceLocation(ModLib.MODID + ":kiln", "normal"), CamoModel.KILN);
    }
}

