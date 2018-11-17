package betterwithmods.module.hardcore.creatures;

import betterwithmods.common.items.ItemArcaneScroll;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.util.InfernalEnchantment;
import betterwithmods.util.WorldUtils;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;


public class HCEnchanting extends Feature {
    private static final HashMap<Class<? extends EntityLivingBase>, ScrollDrop> SCROLL_DROPS = Maps.newHashMap();

    private static Map<Enchantment, Integer> LEVEL_OVERRIDE = Maps.newHashMap();
    private static double dropChance, lootingDropBonus;
    private static boolean fuckMending;
    private static boolean steelRequiresInfernal;

    public static void addEnchantOverride(Enchantment enchantment, int max) {
        LEVEL_OVERRIDE.put(enchantment, max);
    }

    public static int getMaxLevel(Enchantment enchantment) {
        return LEVEL_OVERRIDE.getOrDefault(enchantment, enchantment.getMaxLevel());
    }

    public static boolean canEnchantSteel(Enchantment enchantment) {
        return !steelRequiresInfernal || enchantment instanceof InfernalEnchantment;
    }

    public static void addScrollDrop(Class<? extends EntityLivingBase> clazz, Enchantment enchantment) {
        addScrollDrop(clazz, (entity) -> ItemArcaneScroll.getScrollWithEnchant(enchantment));
    }

    public static void addScrollDrop(Class<? extends EntityLivingBase> clazz, ItemStack scroll) {
        addScrollDrop(clazz, (entity) -> scroll);
    }

    public static void addScrollDrop(Class<? extends EntityLivingBase> clazz, ScrollDrop scroll) {
        SCROLL_DROPS.put(clazz, scroll);
    }

    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        for (Class<? extends EntityLivingBase> entity : SCROLL_DROPS.keySet()) {
            if (entity.isAssignableFrom(event.getEntityLiving().getClass())) {
                ScrollDrop drop = SCROLL_DROPS.get(entity);
                if (drop.getScroll(event.getEntityLiving()) != null) {
                    double chance = event.getEntityLiving().getRNG().nextDouble() + (lootingDropBonus * event.getLootingLevel());
                    if (chance <= drop.getChance()) {
                        WorldUtils.addDrop(event, drop.getScroll(event.getEntityLiving()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;
        if (!event.player.getEntityWorld().isRemote) {
            int mod = EnchantmentHelper.getRespirationModifier(event.player);
            if (mod >= 5) {
                event.player.setAir(300);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Adds Arcane Scroll drops to specific mobs, used for enchanting with the Infernal Enchanter";
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        steelRequiresInfernal = loadProperty("Steel Requires Infernal Enchanter", true).setComment("Soulforged Steel tools can only be enchanted with the Infernal Enchanter").get();
        dropChance = loadProperty("Arcane Scroll Drop Chance", 0.001).setComment("Percentage chance that an arcane scroll will drop, does not effect some scrolls.").get();
        lootingDropBonus = loadProperty("Arcane Scroll Looting Bonus Multiplier", 0.1).setComment("Increase the chance of getting a scroll with looting enchants baseChance * ( lootingDropBonus * lootingLevel)").get();
        fuckMending = loadProperty("Disable Mending", true).setComment("Mending is a bad unbalanced pile of poo").get();

        addScrollDrop(EntitySlime.class, Enchantments.PROTECTION);
        addScrollDrop(EntityPigZombie.class, Enchantments.FIRE_PROTECTION);
        addScrollDrop(EntityBat.class, Enchantments.FEATHER_FALLING);
        addScrollDrop(EntityCreeper.class, Enchantments.BLAST_PROTECTION);
        addScrollDrop(AbstractSkeleton.class, (entity) -> {
            if (entity.world.provider.getDimensionType() == DimensionType.NETHER)
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.INFINITY);
            else
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.PROJECTILE_PROTECTION);
        });
        addScrollDrop(EntitySquid.class, Enchantments.RESPIRATION);
        addScrollDrop(EntityWitch.class, Enchantments.AQUA_AFFINITY);
        addScrollDrop(EntityZombie.class, Enchantments.SMITE);
        addScrollDrop(EntitySpider.class, Enchantments.BANE_OF_ARTHROPODS);
        addScrollDrop(EntityMagmaCube.class, Enchantments.FIRE_ASPECT);
        addScrollDrop(EntityEnderman.class, Enchantments.SILK_TOUCH);
        addScrollDrop(EntityGhast.class, Enchantments.PUNCH);
        addScrollDrop(EntityBlaze.class, Enchantments.FLAME);
        addScrollDrop(EntityPolarBear.class, Enchantments.FROST_WALKER);
        addScrollDrop(EntityGuardian.class, Enchantments.DEPTH_STRIDER);
        if (!fuckMending) {
            addScrollDrop(EntityShulker.class, new ScrollDrop() {
                @Override
                public ItemStack getScroll(EntityLivingBase entity) {
                    return ItemArcaneScroll.getScrollWithEnchant(Enchantments.MENDING);
                }

                @Override
                public double getChance() {
                    return 0.001;
                }
            });
        }
        addScrollDrop(EntityDragon.class, new ScrollDrop() {
            @Override
            public ItemStack getScroll(EntityLivingBase entity) {
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.SWEEPING);
            }

            //Always drops
            @Override
            public double getChance() {
                return 1;
            }
        });
        addScrollDrop(EntityWither.class, new ScrollDrop() {
            @Override
            public ItemStack getScroll(EntityLivingBase entity) {
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.KNOCKBACK);
            }

            //Always drops
            @Override
            public double getChance() {
                return 1;
            }
        });
        addScrollDrop(EntitySilverfish.class, entity -> {
            if (entity.world.provider.getDimensionType() == DimensionType.THE_END) {
                return ItemArcaneScroll.getScrollWithEnchant(Enchantments.EFFICIENCY);
            }
            return null;
        });

        addEnchantOverride(Enchantments.RESPIRATION, 5);


        //TODO
        // SHARPNESS -> Butcher Trade
        // LOOTING -> Farmer Trade
        // UNBREAKING -> Blacksmith Trade
        // FORTUNE ->  Priest Trade
        // POWER   -> Librarian Trade
        // LUCK_OF_THE_SEA, LURE -> Fisherman Trade
        /*
        BINDING_CURSE illager
        MENDING REMOVE
        VANISHING_CURSE illager
         */
    }


    @FunctionalInterface
    public interface ScrollDrop {
        ItemStack getScroll(EntityLivingBase entity);

        default double getChance() {
            return dropChance;
        }
    }

    @Override
    public boolean hasEvent() {
        return true;
    }
}
