package betterwithmods.module.hardcore.crafting;


import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.util.player.PlayerUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HCDeadweight extends Feature {

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT, new ItemStack(Blocks.ANVIL)));
        Blocks.ANVIL.setTranslationKey("betterwithmods:deadweight");
    }

    @SubscribeEvent
        public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
            if (!PlayerUtils.isSurvival(event.getEntityPlayer()))
                return;

        if (event.getWorld().getBlockState(event.getPos()).getBlock().equals(Blocks.ANVIL)) {
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.FAIL);
        }
    }

    @Override
    public String getDescription() {
        return "Disables the vanilla Anvil";
    }

}
