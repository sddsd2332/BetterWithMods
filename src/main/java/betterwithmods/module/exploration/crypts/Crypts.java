package betterwithmods.module.exploration.crypts;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Crypts extends Feature {

    public static int cryptSpawnChance;
    public static boolean enchantedWeapons;
    public static boolean potionEffects;
    public static boolean armoredMobs;

    public static boolean spawnMiniboss;
    public static boolean minibossDropsSpecialLoot;

    @Override
    public String getDescription() {
        return "Generates crypts rarely in the world. These are like mini-strongholds that contain difficult mobs, and a few rewards.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        cryptSpawnChance = loadProperty("Spawn Chance", 1).setComment("The chance a crypt will spawn in X out of 1000 chunks").get();
        enchantedWeapons = loadProperty("Mobs Have Enchanted Weapons", true).setComment("Spawned mobs will have randomly enchanted weapons").get();
        potionEffects = loadProperty("Mobs Have Potion Effects", true).setComment("Spawned mobs will have random dangerous potion effects").get();
        armoredMobs = loadProperty("Mobs Have Armor", true).setComment("Spawned mobs will have random pieces of leather and iron armor").get();

        spawnMiniboss = loadProperty("Spawn Miniboss", true).setComment("Replaces the mob spawners with a hard to kill miniboss. This miniboss is effected by other config options").get();
        minibossDropsSpecialLoot = loadProperty("Miniboss Special Loot", true).setComment("On death a miniboss will drop special loot with enchantments and lore").get();

        GameRegistry.registerWorldGenerator(new CryptGenerator(), 0);
    }
}