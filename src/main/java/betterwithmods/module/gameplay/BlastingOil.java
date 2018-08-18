package betterwithmods.module.gameplay;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.util.CapabilityUtils;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BlastingOil extends Feature {

    private static HashMap<EntityItem, Double> highestPoint = Maps.newHashMap();
    public boolean disableBlastingOilEvents;
    public List<String> blacklistDamageSources;

    @Override
    public String getDescription() {
        return "Make blasting oil very dangerous";
    }

    //TODO
//    @Override
//    public void setupConfig() {
//        disableBlastingOilEvents = loadPropBool("Disable Blasting Oil", "Don't process blasting oil explosions, as they are have major performance impact", false);
//        blacklistDamageSources = Lists.newArrayList(loadPropStringList("Blasting oil damage source blacklist", "Disallow these damage sources from disturbing blasting oil", new String[]{
//                "drown",
//                "cramming",
//                "generic",
//                "wither",
//                "starve",
//                "outOfWorld"
//        }));
//    }

    @SubscribeEvent
    public void onPlayerTakeDamage(LivingHurtEvent e) {
        if (disableBlastingOilEvents)
            return;

        if (blacklistDamageSources.contains(e.getSource().damageType))
            return;

        DamageSource BLAST_OIL = new DamageSource("blastingoil");
        EntityLivingBase living = e.getEntityLiving();
        if (living.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {

            IItemHandler inventory = CapabilityUtils.getEntityInventory(living);
            int count = 0;
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);

                if (!stack.isEmpty() && stack.isItemEqual(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BLASTING_OIL))) {
                    count += stack.getCount();
                    inventory.extractItem(i, stack.getCount(), false);
                }
            }
            if (count > 0) {
                living.attackEntityFrom(BLAST_OIL, Float.MAX_VALUE);
                living.getEntityWorld().createExplosion(null, living.posX, living.posY + living.height / 16, living.posZ, (float) (Math.sqrt(count / 5) / 2.5 + 1), true);
            }
        }
    }

    @SubscribeEvent
    public void onHitGround(TickEvent.WorldTickEvent event) {
        if (disableBlastingOilEvents)
            return;
        List<EntityItem> items;
        World world = event.world;
        if (world.isRemote || event.phase != TickEvent.Phase.END)
            return;

        synchronized (world.loadedEntityList) {
            items = world.loadedEntityList.stream().filter(e -> e instanceof EntityItem && ((EntityItem) e).getItem().isItemEqual(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BLASTING_OIL))).map(e -> (EntityItem) e).collect(Collectors.toList());
        }
        HashSet<EntityItem> toRemove = new HashSet<>();
        items.forEach(item -> {
            boolean ground = item.onGround;
            if (item.isBurning() || (ground && Math.abs(item.posY - highestPoint.getOrDefault(item, item.posY)) > 2.0)) {
                int count = item.getItem().getCount();
                if (count > 0) {
                    world.createExplosion(item, item.posX, item.posY + item.height / 16, item.posZ, (float) (Math.sqrt(count / 5) / 2.5 + 1), true);
                    toRemove.add(item);
                    item.setDead();
                }
            }
            if (item.motionY > 0 || !highestPoint.containsKey(item))
                highestPoint.put(item, item.posY);
        });
        toRemove.forEach(highestPoint::remove);
    }
}
