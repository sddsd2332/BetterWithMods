package betterwithmods.module.internal;

import betterwithmods.common.entity.*;
import betterwithmods.common.entity.item.EntityFallingBlockCustom;
import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import com.google.common.collect.Lists;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.Arrays;
import java.util.List;


public class EntityRegistry extends RequiredFeature {

    public static int TOTAL_ENTITY_IDS;

    private static List<EntityEntry> REGISTRY = Lists.newArrayList();

    public static void registerEntity(EntityEntry entry) {
        REGISTRY.add(entry);
    }

    public static void registerEntities(EntityEntry... entries) {
        REGISTRY.addAll(Arrays.asList(entries));
    }

    public static String entityName(String name) {
        return ModLib.MODID + ":" + name;
    }

    @Override
    public boolean hasEvent() {
        return true;
    }

    @SubscribeEvent
    public void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(REGISTRY.toArray(new EntityEntry[0]));
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMAttributes.registerAttributes();

        registerEntities(
                EntityEntryBuilder.create()
                        .entity(EntityJungleSpider.class)
                        .egg(0x3C6432, 0x648C50)
                        .name(entityName("jungle_spider"))
                        .id(entityName("jungle_spider"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityShearedCreeper.class)
                        .name(entityName("sheared_creeper"))
                        .id(entityName("sheared_creeper"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntitySitMount.class)
                        .name(entityName("sit_mount"))
                        .id(entityName("sit_mount"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 20, false)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntitySpiderWeb.class)
                        .name(entityName("spider_web"))
                        .id(entityName("spider_web"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityHCFishHook.class)
                        .name(entityName("fishing_hook"))
                        .id(entityName("fishing_hook"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 20, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityExtendingRope.class)
                        .name(entityName("extending_rope"))
                        .id(entityName("extending_rope"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityUrn.class)
                        .name(entityName("urn"))
                        .id(entityName("urn"), TOTAL_ENTITY_IDS++)
                        .tracker(10, 50, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityDynamite.class)
                        .name(entityName("dynamite"))
                        .id(entityName("dynamite"), TOTAL_ENTITY_IDS++)
                        .tracker(10, 50, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityMiningCharge.class)
                        .name(entityName("mining_charge"))
                        .id(entityName("mining_charge"), TOTAL_ENTITY_IDS++)
                        .tracker(10, 50, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityBroadheadArrow.class)
                        .name(entityName("broadhead_arrow"))
                        .id(entityName("broadhead_arrow"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityFallingGourd.class)
                        .name(entityName("falling_gourd"))
                        .id(entityName("falling_gourd"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityFallingBlockCustom.class)
                        .name(entityName("falling_block_custom"))
                        .id(entityName("falling_block_custom"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 20, true)
                        .build()
        );
    }
}
