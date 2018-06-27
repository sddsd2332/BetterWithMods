package betterwithmods.module;

import betterwithmods.client.gui.GuiStatus;
import betterwithmods.common.blocks.BlockHemp;
import net.minecraftforge.fluids.Fluid;

public final class GlobalConfig {
    public static boolean debug;
    public static int maxPlatformBlocks;


    public static int waterBottleAmount;
    public static void initGlobalConfig(ModuleLoader loader) {

        String category = "_global";

        loader.configHelper.setRestartNeed(true);

        debug = loader.configHelper.loadPropBool("Debug", category, "Enables debug features", false);
        maxPlatformBlocks = loader.configHelper.loadPropInt("Max Platform Blocks", category, "Max blocks a platform can have", 128);

        loader.configHelper.setRestartNeed(false);

        BlockHemp.growthChance = loader.configHelper.loadPropDouble("Growth Chance","Hemp","Hemp has a 1/X chance of growing where X is this value, the following modifiers divide this value", 15D);
        BlockHemp.fertileModifier = loader.configHelper.loadPropDouble("Fertile Modifier","Hemp","Modifies Hemp Growth Chance when planted on Fertile Farmland", 1.33);
        BlockHemp.lampModifier = loader.configHelper.loadPropDouble("Light Block Modifier","Hemp","Modifies Hemp Growth Chance when a Light Block is two blocks above the Hemp",  1.5D);
        BlockHemp.neighborModifier = loader.configHelper.loadPropDouble("Neighbor Modifier","Hemp","Modifies Hemp Growth Chance for each other crop next to it ",  1.1D);
        waterBottleAmount = loader.configHelper.loadPropInt("Water Bottle Fluid amount", category, "The amount of fluid contained in a glass bottle", "", Fluid.BUCKET_VOLUME / 3, 0, Fluid.BUCKET_VOLUME);
    }

    public static void initGlobalClient(ModuleLoader loader) {
        GuiStatus.offsetY = loader.configHelper.loadPropInt("Status Effect Offset Y", "gui", "Y Offset for the Hunger, Injury and Gloom Status effects.", 0);
        GuiStatus.offsetX = loader.configHelper.loadPropInt("Status Effect Offset X", "gui", "X Offset for the Hunger, Injury and Gloom Status effects.", 0);
    }
}
