package betterwithmods.module.exploration.crypts;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.Map;
import java.util.Random;

public class EndRoomGenerator {

    private Template template;
    private BlockPos entrancePos;
    private BlockPos secretPos;

    public EndRoomGenerator(Template template) {
        this.template = template;
    }

    public void generate(Random random, World world, BlockPos basePos, PlacementSettings placementSettings) {
        template.addBlocksToWorld(world, basePos, placementSettings);

        Map<BlockPos, String> dataBlocks = template.getDataBlocks(basePos, placementSettings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String[] tokens = entry.getValue().split(" ");
            if(tokens.length == 0)
                return;

            BlockPos dataPos = entry.getKey();

            switch(tokens[0]) {
                case "Crypt_End_Chest":
                    generateChest(random, world, dataPos);
                    break;
                case "Crypt_End_Spawner_Main":
                    generateSpawnerMain(random, world, dataPos);
                    break;
                case "Crypt_End_Spawner_One":
                    generateSpawnerFirst(random, world, dataPos);
                    break;
                case "Crypt_End_Spawner_Two":
                    generateSpawnerSecond(random, world, dataPos);
                    break;
                case "Crypt_End_Entrance":
                    entrancePos = dataPos;
                    break;
                case "Crypt_End_Secret":
                    secretPos = dataPos;
                    break;
                default:
                    break;
            }
        }
    }

    private void generateChest(Random random, World world, BlockPos dataPos) {

    }

    private void generateSpawnerMain(Random random, World world, BlockPos dataPos) {
        if(Crypts.spawnMiniboss) {

        } else {
            TileEntity mainSpawner = world.getTileEntity(dataPos.down());

            if(mainSpawner instanceof TileEntityMobSpawner) {
                TileEntityMobSpawner spawner = (TileEntityMobSpawner) mainSpawner;

                NBTTagCompound spawnerData = new NBTTagCompound();
                NBTTagCompound entityData = new NBTTagCompound();

                entityData.setString("id", "minecraft:skeleton");

                spawnerData.setTag("SpawnPotentials", new NBTTagCompound());

            }
        }
    }

    private void generateSpawnerFirst(Random random, World world, BlockPos dataPos) {
        if(Crypts.spawnMiniboss) return;

    }

    private void generateSpawnerSecond(Random random, World world, BlockPos dataPos) {
        if(Crypts.spawnMiniboss) return;

    }
}
