package betterwithmods.module.hardcore.world.structures;

import betterwithmods.library.common.event.structure.StructureLootEvent;
import betterwithmods.library.common.event.structure.StructureSetBlockEvent;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.blockstate.BlockIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.blockstate.MaterialIngredient;
import betterwithmods.library.utils.ingredient.blockstate.PredicateBlockStateIngredient;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

/**
 * Created by primetoxinz on 5/21/17.
 */


public class HCVillages extends Feature {

    public static boolean disableAllComplexBlocks;
    public static boolean disableVillagerSpawning;
    public static boolean disableIronGolems;
    private static Set<StructureChanger> VILLAGE = Sets.newHashSet();
    private int normalRadius, semiabandonedRadius;
    private StructureChanger ABANDONED, SEMIABANDONED, NORMAL;

    @SubscribeEvent
    public void onStructureSetBlock(StructureSetBlockEvent event) {
        if (event.getComponent() instanceof StructureVillagePieces.Village) {
            StructureChanger.convert(VILLAGE, event);
        }
    }

    @SubscribeEvent
    public void onStructureLoot(StructureLootEvent event) {
        if (event.getComponent() instanceof StructureVillagePieces.Village) {
            StructureChanger.convert(VILLAGE, event);
        }
    }

    @Override
    public String getDescription() {
        return "Makes it so villages with in the reaches of the spawn zone are abandoned and gradually gain more resources the further out. What this means to be gained by the player.";
    }


    @Override
    public void onInit(FMLInitializationEvent event) {

        semiabandonedRadius = loadProperty("Semi-Abandoned Village Radius", 2000).setComment("Block radius from 0,0 at which villages are now semi-abandoned, all villages inside this radius are abandoned").get();
        normalRadius = loadProperty("Normal Village Radius", 3000).setComment("Block radius from 0,0 at which villages are now normal, all villages in between this and semi-abandoned are semi-abandoned").get();
        //TODO out of the scope of this Feature
        disableVillagerSpawning = loadProperty("Replace Villager Spawning with Nitwits", true).setComment("Replaces all villager spawns with Nitwits, which have no trades").get();
        disableIronGolems = loadProperty("Disable Village Iron Golem Spawns", true).setComment("WARNING: Stops all non-player created Iron Golem Spawns").get();

        ABANDONED = StructureChanger.create(VILLAGE, (w, p) -> p.distanceSq(w.getSpawnPoint()) < semiabandonedRadius * semiabandonedRadius);
        SEMIABANDONED = StructureChanger.create(VILLAGE, (w, p) -> p.distanceSq(w.getSpawnPoint()) < normalRadius * normalRadius);
        NORMAL = StructureChanger.create(VILLAGE, (w, p) -> p.distanceSq(w.getSpawnPoint()) >= normalRadius * normalRadius);

        TableChanger tableChanger = new TableChanger();

        ABANDONED
                .addChanger(tableChanger)
                .addChanger(new BiomeIngredientChanger(new BlockStateIngredient(new ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.getMetadata())), Blocks.PLANKS.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.CRAFTING_TABLE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.BOOKSHELF), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.TORCH), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.FURNACE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new MaterialIngredient(Material.GLASS), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new MaterialIngredient(Material.PLANTS), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new MaterialIngredient(Material.WATER), Blocks.DIRT.getDefaultState()))
                .addChanger(new IngredientChanger(new PredicateBlockStateIngredient((world, pos) -> world.getBlockState(pos).getBlock() instanceof BlockDoor), Blocks.AIR.getDefaultState()))
                .addChanger(new ChestLootChanger(null));

        SEMIABANDONED
                .addChanger(tableChanger)
                .addChanger(new BiomeIngredientChanger(new BlockStateIngredient(new ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.getMetadata())), Blocks.PLANKS.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.CRAFTING_TABLE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.BOOKSHELF), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.TORCH), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredient(Blocks.FURNACE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new MaterialIngredient(Material.GLASS), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new MaterialIngredient(Material.WATER), Blocks.DIRT.getDefaultState()))
                .addChanger(new IngredientChanger(new PredicateBlockStateIngredient((world, pos) -> world.getBlockState(pos).getBlock() instanceof BlockDoor), Blocks.AIR.getDefaultState()))
                .addChanger(new ChestLootChanger(null))
                .addChanger(new CropTypeChanger(Lists.newArrayList(Blocks.POTATOES, Blocks.CARROTS, Blocks.AIR)));

        NORMAL
                .addChanger(tableChanger)
                .addChanger(new CropTypeChanger(Lists.newArrayList(Blocks.WHEAT, Blocks.BEETROOTS, Blocks.POTATOES, Blocks.CARROTS)));

    }

    //hack to stop iron golem spawning in villages, also will stop any other spawning
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!disableIronGolems)
            return;
        if (event.getEntity() instanceof EntityIronGolem) {
            EntityIronGolem golem = (EntityIronGolem) event.getEntity();
            if (!golem.isPlayerCreated()) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

    @Override
    public boolean hasTerrainGen() {
        return true;
    }
}



