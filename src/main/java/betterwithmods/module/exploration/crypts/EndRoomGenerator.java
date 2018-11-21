package betterwithmods.module.exploration.crypts;

import betterwithmods.library.utils.spawning.SpawnerBuilder;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumHand;
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
            if (tokens.length == 0)
                return;

            BlockPos dataPos = entry.getKey();

            switch (tokens[0]) {
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
        TileEntity mainSpawner = world.getTileEntity(dataPos.down());

        if (mainSpawner instanceof TileEntityMobSpawner) {
            TileEntityMobSpawner spawner = (TileEntityMobSpawner) mainSpawner;

            NBTTagCompound spawnerData = generateMiniboss(random);

            spawner.deserializeNBT(spawnerData);
            world.notifyBlockUpdate(spawner.getPos(), world.getBlockState(spawner.getPos()), world.getBlockState(spawner.getPos()), 0);
        }
    }

    private NBTTagCompound generateMiniboss(Random random) {
        ItemStack[] armor = new ItemStack[]{new ItemStack(Items.IRON_BOOTS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.IRON_HELMET)};
        String[] minibossTypes = new String[]{"skeleton", "zombie"};

        boolean[] activeArmorPieces = new boolean[]{random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean()};
        boolean[] enchantedArmorPieces = new boolean[]{random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean()};

        String minibossType = minibossTypes[random.nextInt(minibossTypes.length)];
        SpawnerBuilder spawnerData = SpawnerBuilder.create(new ResourceLocation(minibossType));

        spawnerData.withHealth(50);
        for (int armorSlot = 0; armorSlot < armor.length; armorSlot++) {
            if (Crypts.armoredMobs && activeArmorPieces[armorSlot]) {
                ItemStack armorPiece = armor[armorSlot];
                if (Crypts.enchantedWeapons && enchantedArmorPieces[armorSlot]) {
                    EnchantmentHelper.addRandomEnchantment(random, armorPiece, random.nextInt(Crypts.maxEnchantmentLevel), false);
                }

                spawnerData.withStackInSlot(armorPiece, armorSlot, 100);
            }
        }

        if (minibossType.equals(minibossTypes[0])) {
            ItemStack bow = new ItemStack(Items.BOW);

            if (Crypts.enchantedWeapons) {
                EnchantmentHelper.addRandomEnchantment(random, bow, random.nextInt(Crypts.maxEnchantmentLevel), false);
                EnchantmentHelper.addRandomEnchantment(random, bow, random.nextInt(Crypts.maxEnchantmentLevel), false);
            }

            spawnerData.withStackInHand(bow, EnumHand.MAIN_HAND, 100);
        } else {
            ItemStack axe = new ItemStack(Items.IRON_AXE);

            if (Crypts.enchantedWeapons) {
                EnchantmentHelper.addRandomEnchantment(random, axe, random.nextInt(Crypts.maxEnchantmentLevel), false);
                EnchantmentHelper.addRandomEnchantment(random, axe, random.nextInt(Crypts.maxEnchantmentLevel), false);
            }

            spawnerData.withStackInHand(axe, EnumHand.MAIN_HAND, 100);
        }

        return spawnerData.build();
    }


    private void generateSpawnerFirst(Random random, World world, BlockPos dataPos) {
        if (Crypts.spawnMiniboss) return;

    }

    private void generateSpawnerSecond(Random random, World world, BlockPos dataPos) {
        if (Crypts.spawnMiniboss) return;

    }


}
