package betterwithmods.module.hardcore.world.structures;

import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.block.recipe.BlockIngredientSpecial;
import betterwithmods.common.registry.block.recipe.BlockMaterialIngredient;
import betterwithmods.common.registry.block.recipe.StateIngredient;
import betterwithmods.library.event.StructureSetBlockEvent;
import betterwithmods.module.Feature;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

/**
 * Created by primetoxinz on 5/21/17.
 */

@Mod.EventBusSubscriber
public class HCVillages extends Feature {

    public static boolean disableAllComplexBlocks;
    public static boolean disableVillagerSpawning;
    public static boolean disableIronGolems;

    private int normalRadius, semiabandonedRadius;

    @Override
    public String getDescription() {
        return "Makes it so villages with in the reaches of the spawn zone are abandoned and gradually gain more resources the further out. What this means to be gained by the player.";
    }

    private Set<StructureChanger> VILLAGE = Sets.newHashSet();

    private StructureChanger ABANDONED, SEMIABANDONED, NORMAL;

    @Override
    public void onInit(FMLInitializationEvent event) {

        semiabandonedRadius = loadProperty("Semi-Abandoned Village Radius",  2000).setComment("Block radius from 0,0 at which villages are now semi-abandoned, all villages inside this radius are abandoned").get();
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
                .addChanger(new BiomeIngredientChanger(new BlockIngredient(new ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.getMetadata())), Blocks.PLANKS.getDefaultState()))
                .addChanger(new IngredientChanger(new StateIngredient(Blocks.CRAFTING_TABLE), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new StateIngredient(Blocks.BOOKSHELF), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new StateIngredient(Blocks.TORCH), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockMaterialIngredient(Material.GLASS), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockMaterialIngredient(Material.PLANTS), Blocks.AIR.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockMaterialIngredient(Material.WATER), Blocks.DIRT.getDefaultState()))
                .addChanger(new IngredientChanger(new BlockIngredientSpecial((world, pos) -> world.getBlockState(pos).getBlock() instanceof BlockDoor), Blocks.AIR.getDefaultState()));

        SEMIABANDONED = NORMAL = ABANDONED;
//                .addChanger(tableChanger)
//                .addChanger(new IngredientChanger(new BlockMaterialIngredient(Material.WATER), Blocks.AIR.getDefaultState()))
//                .addChanger(new IngredientChanger(new BlockMaterialIngredient(Material.GLASS), Blocks.AIR.getDefaultState()));

        NORMAL.addChanger(tableChanger);

    }


    @SubscribeEvent
    public void biomeSpecificVillage(BiomeEvent.GetVillageBlockID event) {

    }

    @SubscribeEvent
    public void onStructureSetBlock(StructureSetBlockEvent event) {
        if (event.getComponent() instanceof StructureVillagePieces.Village) {
//            System.out.printf("/tp %s ~ %s\n", event.getPos().getX(), event.getPos().getZ());
            StructureChanger.convert(VILLAGE, event);
        }
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

}


