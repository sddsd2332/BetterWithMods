package betterwithmods.common.advancements;

import betterwithmods.lib.ModLib;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class BWAdvancements {

    public static final ConstructLibraryTrigger CONSTRUCT_LIBRARY = CriteriaTriggers.register(new ConstructLibraryTrigger());
    public static final InfernalEnchantedTrigger INFERNAL_ENCHANTED = CriteriaTriggers.register(new InfernalEnchantedTrigger());

    public static final SimpleTrigger CONSTRUCT_KILN = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "construct_kiln")));
    public static final SimpleTrigger SPAWN_HOPPER_FRIEND = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "spawn_hopper_friend")));
    public static final SimpleTrigger EXPLOSIVE_RECIPE = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "explosive_recipe")));


    public static void registerAdvancements() {
    }

    public static void triggerNearby(World world, AxisAlignedBB aabb, SimpleTrigger trigger) {
        world.getEntitiesWithinAABB(EntityPlayerMP.class, aabb).forEach(trigger::trigger);
    }
}
