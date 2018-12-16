package betterwithmods.module.hardcore.world.structures;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.library.common.event.structure.StructureSetBlockEvent;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

/**
 * Created by primetoxinz on 4/20/17.
 */


public class HCStructures extends Feature {
    public static int HARDCORE_STRUCTURE_RADIUS;
    public static Set<StructureChanger> DESERT_TEMPLE = Sets.newHashSet();
    public static StructureChanger ABANDONED_DESERT_TEMPLE, NORMAL_DESERT_TEMPLE;
    private static Set<StructureChanger> JUNGLE_TEMPLE = Sets.newHashSet();
    private static StructureChanger ABANDONED_JUNGLE_TEMPLE, NORMAL_JUNGLE_TEMPLE;
    private static Set<StructureChanger> WITCH_HUT = Sets.newHashSet();
    private static StructureChanger WITCH_HUT_CHANGER;
    private boolean disableRecipes;

    @Override
    public String getDescription() {
        return "Makes it so structures are looted within a radius of spawn and unlooted outside of that radius. Encourages exploration and also makes unlooted structures the only source of Enchanting Tables and Brewing Stands.";
    }


    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        HARDCORE_STRUCTURE_RADIUS = loadProperty("Hardcore EnumStructure Radius", 2000).setComment("Radius from original spawn which structures will be abandoned in").get();
        disableRecipes = loadProperty("Disable Recipes", true).setComment("Disable Recipes for blocks that generate only in structures, including Enchanting Tables and Brewing Stands").get();
        ABANDONED_DESERT_TEMPLE = StructureChanger.create(DESERT_TEMPLE, (w, p) -> p.distanceSq(w.getSpawnPoint()) < HARDCORE_STRUCTURE_RADIUS * HARDCORE_STRUCTURE_RADIUS);
        NORMAL_DESERT_TEMPLE = StructureChanger.create(DESERT_TEMPLE, (w, p) -> p.distanceSq(w.getSpawnPoint()) >= HARDCORE_STRUCTURE_RADIUS * HARDCORE_STRUCTURE_RADIUS);

        ABANDONED_JUNGLE_TEMPLE = StructureChanger.create(JUNGLE_TEMPLE, (w, p) -> p.distanceSq(w.getSpawnPoint()) < HARDCORE_STRUCTURE_RADIUS * HARDCORE_STRUCTURE_RADIUS);
        NORMAL_JUNGLE_TEMPLE = StructureChanger.create(JUNGLE_TEMPLE, (w, p) -> p.distanceSq(w.getSpawnPoint()) >= HARDCORE_STRUCTURE_RADIUS * HARDCORE_STRUCTURE_RADIUS);

        WITCH_HUT_CHANGER = StructureChanger.create(WITCH_HUT, (w, p) -> true);
    }

    @Override
    public void registerRecipes() {
        if (disableRecipes) {
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT, new ItemStack(Blocks.ENCHANTING_TABLE)));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT, new ItemStack(Items.BREWING_STAND)));
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        ABANDONED_DESERT_TEMPLE
                //Dig hole
                .addChanger(new RelativePosChanger(Blocks.AIR.getDefaultState(),
                        new BlockPos(10, 0, 10),
                        new BlockPos(11, 0, 10),
                        new BlockPos(10, 0, 9)))
                //Remove TNT
                .addChanger(new RelativePosChanger(Blocks.SANDSTONE.getDefaultState(), new AxisAlignedBB(9, -13, 9, 11, -13, 11)))
                //Remove pressure plate
                .addChanger(new RelativePosChanger(Blocks.AIR.getDefaultState(), new BlockPos(10, -11, 10)))
                //Place Ladder
                .addChanger(new RelativePosChanger(Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST), new AxisAlignedBB(11, 0, 9, 11, -13, 9)))
                //Deletes loot table
                .addChanger(new ChestLootChanger(null))
                //Replace clay with obsidian
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.STAINED_HARDENED_CLAY), Blocks.OBSIDIAN.getDefaultState()));

        NORMAL_DESERT_TEMPLE
                .addChanger(new RelativePosChanger(Blocks.ENCHANTING_TABLE.getDefaultState(), new BlockPos(10, 1, 10)))
                //Replace clay with obsidian
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.STAINED_HARDENED_CLAY), Blocks.OBSIDIAN.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.STONE_PRESSURE_PLATE), Blocks.WOODEN_PRESSURE_PLATE.getDefaultState()));

        ABANDONED_JUNGLE_TEMPLE
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.REDSTONE_WIRE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.STICKY_PISTON), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.DISPENSER), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.TRIPWIRE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.TRIPWIRE_HOOK), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.UNPOWERED_REPEATER), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.POWERED_REPEATER), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.LEVER), Blocks.AIR.getDefaultState()))
                .addChanger(new RelativePosChanger(Blocks.AIR.getDefaultState(), new BlockPos(5, 3, 10)))
                .addChanger(new RelativePosChanger(Blocks.AIR.getDefaultState(), new BlockPos(6, 3, 10)))
                .addChanger(new ChestLootChanger(null))
                ;


        NORMAL_JUNGLE_TEMPLE
                .addChanger(new RelativePosChanger(BWMBlocks.HAND_CRANK.getDefaultState(), new BlockPos(5, 3, 10)))
                .addChanger(new RelativePosChanger(BWMBlocks.DRAGON_VESSEL.getDefaultState(), new BlockPos(6, 3, 10)))
                .addChanger(new RelativePosChanger(BlockAesthetic.getVariant(BlockAesthetic.Type.CHOPBLOCKBLOOD), new BlockPos(5, 4, 11),new BlockPos(6, 4, 11)));


        WITCH_HUT_CHANGER
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.FLOWER_POT),Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.CRAFTING_TABLE),Blocks.BREWING_STAND.getDefaultState()));
    }


    @SubscribeEvent
    public void onStructureSetBlock(StructureSetBlockEvent event) {
        if (event.getComponent() instanceof ComponentScatteredFeaturePieces.DesertPyramid) {
            StructureChanger.convert(DESERT_TEMPLE, event);
        } else if (event.getComponent() instanceof ComponentScatteredFeaturePieces.JunglePyramid) {
            StructureChanger.convert(JUNGLE_TEMPLE, event);
        } else if (event.getComponent() instanceof ComponentScatteredFeaturePieces.SwampHut) {
            StructureChanger.convert(WITCH_HUT, event);
        }
    }

    @Override
    public boolean hasTerrainGen() {
        return true;
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
