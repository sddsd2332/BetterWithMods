package betterwithmods.module.internal;

import betterwithmods.common.entity.*;
import betterwithmods.common.entity.item.EntityFallingBlockCustom;
import betterwithmods.lib.ModLib;
import betterwithmods.library.modularity.impl.RequiredFeature;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class EntityRegistry extends RequiredFeature {

    public static int TOTAL_ENTITY_IDS;

    private static List<EntityEntry> REGISTRY = Lists.newArrayList();

    public static void registerEntity(EntityEntry entry) {
        REGISTRY.add(entry);
    }

    public static void registerEntities(EntityEntry... entries) {
        REGISTRY.addAll(Arrays.asList(entries));
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        registerEntities(
                EntityEntryBuilder.create()
                        .entity(EntityJungleSpider.class)
                        .egg(0x3C6432, 0x648C50)
                        .id(new ResourceLocation(ModLib.MODID, "jungle_spider"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityShearedCreeper.class)
                        .id(new ResourceLocation(ModLib.MODID, "sheared_creeper"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntitySitMount.class)
                        .id(new ResourceLocation(ModLib.MODID, "sit_mount"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 20, false)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntitySpiderWeb.class)
                        .id(new ResourceLocation(ModLib.MODID, "spider_web"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntitySpiderWeb.class)
                        .id(new ResourceLocation(ModLib.MODID, "spider_web"), TOTAL_ENTITY_IDS++)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityHCFishHook.class)
                        .id(new ResourceLocation(ModLib.MODID, "fishing_hook"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 20, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityExtendingRope.class)
                        .id(new ResourceLocation(ModLib.MODID, "extending_rope"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityUrn.class)
                        .id(new ResourceLocation(ModLib.MODID, "urn"), TOTAL_ENTITY_IDS++)
                        .tracker(10, 50, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityDynamite.class)
                        .id(new ResourceLocation(ModLib.MODID, "dynamite"), TOTAL_ENTITY_IDS++)
                        .tracker(10, 50, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityMiningCharge.class)
                        .id(new ResourceLocation(ModLib.MODID, "mining_charge"), TOTAL_ENTITY_IDS++)
                        .tracker(10, 50, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityBroadheadArrow.class)
                        .id(new ResourceLocation(ModLib.MODID, "broadhead_arrow"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityFallingGourd.class)
                        .id(new ResourceLocation(ModLib.MODID, "falling_gourd"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityFallingBlockCustom.class)
                        .id(new ResourceLocation(ModLib.MODID, "falling_block_custom"), TOTAL_ENTITY_IDS++)
                        .tracker(64, 20, true)
                        .build()
        );
    }

    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(REGISTRY.toArray(new EntityEntry[0]));
    }
}
