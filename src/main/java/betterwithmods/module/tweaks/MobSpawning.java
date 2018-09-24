package betterwithmods.module.tweaks;

import betterwithmods.common.entity.EntityJungleSpider;
import betterwithmods.common.entity.SpawningWhitelist;
import betterwithmods.library.utils.ingredient.BlockMaterialIngredient;
import betterwithmods.library.modularity.impl.Feature;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class MobSpawning extends Feature {
    public static final SpawningWhitelist NETHER = new SpawningWhitelist();
    public static final SpawningWhitelist SLIME = new SpawningWhitelist();

    private static boolean slime, nether, witches, jungleSpiders;

    @Override
    public void onInit(FMLInitializationEvent event) {

        slime = loadProperty("Limit Slime Spawning", true).setComment("Slimes can only spawn on natural blocks").get();
        nether = loadProperty("Limit Nether Spawning", true).setComment("Nether Mobs can only spawn on nether blocks").get();
        witches = loadProperty("Limit Witch Spawning", true).setComment("Witches can only spawn in swamps").get();
        jungleSpiders = loadProperty("Jungle Spider Spawning", true).setComment("Jungle Spiders can spawn in jungles").get();

        if (nether) {
            NETHER.addBlock(Blocks.NETHERRACK);
            NETHER.addBlock(Blocks.NETHER_BRICK);
            NETHER.addBlock(Blocks.SOUL_SAND);
            NETHER.addBlock(Blocks.GRAVEL);
            NETHER.addBlock(Blocks.QUARTZ_BLOCK);
        }

        if (slime) {
            SLIME.addIngredient(new BlockMaterialIngredient(Material.GRASS, Material.ROCK, Material.GROUND));
        }

        for (Biome biome : Biome.REGISTRY) {
            if (jungleSpiders && BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                EntityRegistry.addSpawn(EntityJungleSpider.class, 100, 1, 3, EnumCreatureType.MONSTER, biome);
            }
            if (witches && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                EntityRegistry.removeSpawn(EntityWitch.class, EnumCreatureType.MONSTER, biome);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Nether Mobs can only spawn on nether blocks and Slimes can only spawn on natural blocks. Also adjusts whether witches only spawn in swamps and if jungle spiders spawn in jungles.";
    }


    @SubscribeEvent
    public static void denySlimeSpawns(LivingSpawnEvent.CheckSpawn event) {
        if (event.getResult() == Event.Result.ALLOW)
            return;
        if (!slime)
            return;
        World world = event.getWorld();
        if (world != null && world.provider.getDimensionType() == DimensionType.OVERWORLD) {
            if (event.getEntityLiving() instanceof EntitySlime) {
                BlockPos pos = new BlockPos(event.getEntity().posX, event.getEntity().posY - 1, event.getEntity().posZ);
                if (SLIME.isBlacklisted(world, pos, world.getBlockState(pos)))
                    event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void denyNetherSpawns(LivingSpawnEvent.CheckSpawn event) {
        if (event.getResult() == Event.Result.ALLOW)
            return;
        if (!nether)
            return;
        World world = event.getWorld();
        if (world != null && world.provider.getDimensionType() == DimensionType.NETHER) {
            if (event.getEntityLiving().isCreatureType(EnumCreatureType.MONSTER, false)) {
                double monX = event.getEntity().posX;
                double monY = event.getEntity().posY;
                double monZ = event.getEntity().posZ;
                int x = MathHelper.floor(monX);
                int y = MathHelper.floor(monY);
                int z = MathHelper.floor(monZ);
                BlockPos pos = new BlockPos(x, y - 1, z);
                IBlockState state = world.getBlockState(pos);
                if (!world.isAirBlock(pos) && NETHER.isBlacklisted(world, pos, state))
                    event.setResult(Event.Result.DENY);
            }
        }
    }



}
