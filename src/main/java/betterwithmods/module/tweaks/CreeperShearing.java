package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.EntityShearedCreeper;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.EntityUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

/**
 * Created by primetoxinz on 4/20/17.
 */
@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class CreeperShearing extends Feature {

    public static Set<ResourceLocation> CREEPERS;

    @Override
    public void onInit(FMLInitializationEvent event) {
        CREEPERS = config().loadResourceLocations("Creepers", getCategory(), "List of valid creepers", new String[]{"minecraft:creeper"});
    }

    private static boolean isMatching(EntityLivingBase entity) {
        return CREEPERS.stream().anyMatch(r -> EntityList.isMatchingName(entity, r));
    }

    @SubscribeEvent
    public static void mobDrops(LivingDropsEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (isMatching(entity)) {
            double chance = entity.getRNG().nextDouble() + (0.1 * event.getLootingLevel());
            if (chance <= 0.05) {
                WorldUtils.addDrop(event, new ItemStack(BWMItems.CREEPER_OYSTER));
            }
        }
    }

    @SubscribeEvent
    public static void shearCreeper(PlayerInteractEvent.EntityInteractSpecific e) {
        if (e.getTarget() instanceof EntityLivingBase) {
            EntityLivingBase creeper = (EntityLivingBase) e.getTarget();
            if (isMatching(creeper)) {
                if (e.getSide().isServer() && creeper.isEntityAlive() && !e.getItemStack().isEmpty()) {
                    Item item = e.getItemStack().getItem();
                    if (item instanceof ItemShears || item.getHarvestLevel(e.getItemStack(), "shear", e.getEntityPlayer(), null) > 0) {
                        if (e.getEntityPlayer().getCooldownTracker().hasCooldown(item))
                            return;
                        e.getEntityPlayer().getCooldownTracker().setCooldown(item, 20);
                        e.getItemStack().damageItem(1, e.getEntityLiving());
                        InventoryUtils.ejectStack(e.getWorld(), creeper.posX, creeper.posY, creeper.posZ, new ItemStack(BWMItems.CREEPER_OYSTER));
                        EntityShearedCreeper shearedCreeper = new EntityShearedCreeper(e.getWorld());
                        creeper.attackEntityFrom(new DamageSource(""), 0);
                        EntityUtils.copyEntityInfo(creeper, shearedCreeper);
                        e.getWorld().playSound(null, shearedCreeper.posX, shearedCreeper.posY, shearedCreeper.posZ, SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.HOSTILE, 1, 0.3F);
                        e.getWorld().playSound(null, shearedCreeper.posX, shearedCreeper.posY, shearedCreeper.posZ, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.HOSTILE, 1, 1F);
                        creeper.setDead();
                        e.getWorld().spawnEntity(shearedCreeper);
                    }
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Shearing a Creeper will removes its ability to explode, making him very sad";
    }
}
