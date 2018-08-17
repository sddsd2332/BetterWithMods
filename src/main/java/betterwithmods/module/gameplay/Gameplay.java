package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.registry.block.recipe.BlockDropIngredient;
import betterwithmods.common.registry.block.recipe.StateIngredient;
import betterwithmods.common.tile.TileWaterwheel;
import betterwithmods.module.Module;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.animal_restraint.AnimalRestraint;
import betterwithmods.module.gameplay.miniblocks.MiniBlocks;
import betterwithmods.util.SetBlockIngredient;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class Gameplay extends Module {
    public static double generatorRenderDistance;

    public static double crankExhaustion;
    public static boolean kidFriendly, disableBlastingOilEvents;
    public static float cauldronNormalSpeedFactor, cauldronStokedSpeedFactor, cauldronMultipleFiresFactor;

    public static boolean dropHempSeeds;
    public static List<String> blacklistDamageSources;
    private String[] waterwheelFluidConfig;
    public Gameplay(ModuleLoader loader) {
        super(loader);
    }

    @Override
    public void addFeatures() {
        registerFeature(new MechanicalBreakage());
        registerFeature(new MetalReclaming());
        registerFeature(new NuggetCompression());
        registerFeature(new HarderSteelRecipe());
        registerFeature(new AnvilRecipes());
        registerFeature(new CraftingRecipes());
        registerFeature(new CauldronRecipes());
        registerFeature(new CrucibleRecipes());
        registerFeature(new KilnRecipes());
        registerFeature(new MillRecipes());
        registerFeature(new SawRecipes());
        registerFeature(new TurntableRecipes());
        registerFeature(new HopperRecipes());
        registerFeature(new NetherGrowth());
        registerFeature(new AnimalRestraint().recipes());
        registerFeature(new PlayerDataHandler());
        registerFeature(new ReadTheFingManual());
        registerFeature(new MiniBlocks());
        registerFeature(new BlastingOil());
    }

    @Override
    public void setupConfig() {
        generatorRenderDistance = loadPropDouble("Render Distance for Axle Generators", "Allows expanding the render distance radius for Windmills and Waterwheels", 256);
        crankExhaustion = loadPropDouble("Crank Exhaustion", "How much saturation turning the crank eats. Set to 0.0 to disable.", 6.0, 0.0, 6.0);
        kidFriendly = loadPropBool("Kid Friendly", "Makes some features more kid friendly", false);

        loadRecipeCondition("higheff", "High Efficiency Recipes", "Enables High Efficiency Recipes", true);
        cauldronNormalSpeedFactor = (float) loadPropDouble("Cauldron normal speed factor", "Cooking speed multiplier for unstoked cauldrons.", 1.0);
        cauldronStokedSpeedFactor = (float) loadPropDouble("Cauldron stoked speed factor", "Cooking speed multiplier for stoked cauldrons and crucibles.", 1.0);
        cauldronMultipleFiresFactor = (float) loadPropDouble("Cauldron Multiple fires factor", "Sets how strongly multiple fire sources (in a 3x3 grid below the pot) affect cooking times.", 1.0);
        dropHempSeeds = loadPropBool("Drop Hemp Seeds", "Adds Hemp seeds to the grass seed drop list.", true);
        waterwheelFluidConfig = loader.configHelper.loadPropStringList("Waterwheel fluids", name, "Additional Fluids which will allow the Waterwheel to turn, format fluid_name. (Vanilla water will always work)", new String[]{
                "swamp_water"
        });

        blacklistDamageSources = Lists.newArrayList(loader.configHelper.loadPropStringList("Blasting oil damage source blacklist", name, "Disallow these damage sources from disturbing blasting oil", new String[]{
                "drown",
                "cramming",
                "generic",
                "wither",
                "starve",
                "outOfWorld"
        }));

        super.setupConfig();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);


        if (dropHempSeeds) {
            MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1), 5);
        }
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        Arrays.stream(waterwheelFluidConfig).map(FluidRegistry::getFluid).filter(Objects::nonNull).collect(Collectors.toList()).forEach(fluid -> TileWaterwheel.registerWater(fluid.getBlock()));

        BlockBUD.BLACKLIST = new SetBlockIngredient(
                new StateIngredient(Blocks.REDSTONE_WIRE, Items.REDSTONE),
                new StateIngredient(Blocks.POWERED_REPEATER, Items.REPEATER),
                new StateIngredient(Blocks.UNPOWERED_REPEATER, Items.REPEATER),
                new StateIngredient(Blocks.UNLIT_REDSTONE_TORCH),
                new StateIngredient(Blocks.REDSTONE_TORCH),
                new BlockDropIngredient(new ItemStack(BWMBlocks.LIGHT)),
                new BlockDropIngredient(new ItemStack(BWMBlocks.BUDDY_BLOCK))
        );
    }
}

