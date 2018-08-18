package betterwithmods.module.gameplay;

import betterwithmods.BWMod;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.miniblocks.client.CamoModel;
import betterwithmods.module.hardcore.needs.HCCooking;
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

    @Override
    public void onInit(FMLInitializationEvent event) {
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.CRUCIBLE), new ItemStack(BWMBlocks.CRUCIBLE));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.PLANTER), BlockPlanter.getStack(BlockPlanter.EnumType.EMPTY));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.URN), new ItemStack(BWMBlocks.URN));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.VASE), BlockVase.getStack(EnumDyeColor.WHITE));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.BRICK), new ItemStack(Items.BRICK));
        BWRegistry.KILN.addStokedRecipe(BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.NETHER_BRICK), ItemMaterial.getStack(ItemMaterial.EnumMaterial.NETHER_SLUDGE));

        BWRegistry.KILN.addStokedRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Blocks.HARDENED_CLAY));
        BWRegistry.KILN.addStokedRecipe(new ItemStack(BWMBlocks.NETHER_CLAY), BlockAesthetic.getStack(BlockAesthetic.EnumType.NETHERCLAY));

        int foodModifier = BWMod.MODULE_LOADER.isFeatureEnabled(HCCooking.class) ? 1 : 2;
        BWRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), IntStream.range(0, foodModifier).mapToObj(i -> new ItemStack(Items.CAKE)).collect(Collectors.toList()));
        BWRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD, foodModifier));
        BWRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 8 * foodModifier));
        BWRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE, foodModifier));
        BWRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE, foodModifier));
        BWRegistry.KILN.addUnstokedRecipe(BlockRawPastry.getStack(BlockRawPastry.EnumType.MELON), new ItemStack(BWMItems.MELON_PIE, foodModifier));
    }

    @SubscribeEvent
    public void formKiln(BlockEvent.NeighborNotifyEvent event) {
        BlockPos up = event.getPos().up();
        World world = event.getWorld();
        if (KilnStructureManager.isKilnBlock(world.getBlockState(up)) && BWMHeatRegistry.getHeat(world, event.getPos()) > 0) {
            KilnStructureManager.createKiln(world, up);
        }
    }

    @SubscribeEvent
    public void onKilnPlace(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock().isAir(event.getPlacedBlock(), event.getWorld(), event.getPos())) {
            KilnStructureManager.createKiln(event.getWorld(), event.getPos());
        }
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostBake(ModelBakeEvent event) {
        CamoModel.KILN = new CamoModel(RenderUtils.getModel(new ResourceLocation("minecraft", "block/cube")));
        event.getModelRegistry().putObject(new ModelResourceLocation(BWMod.MODID + ":kiln", "normal"), CamoModel.KILN);
    }
}

