package betterwithmods.module.internal;

import betterwithmods.common.advancements.ConstructLibraryTrigger;
import betterwithmods.common.advancements.InfernalEnchantedTrigger;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.advancements.Advancements;
import betterwithmods.library.common.advancements.SimpleTrigger;
import betterwithmods.library.common.advancements.StringTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;

public class AdvancementRegistry extends Advancements {


    public static ConstructLibraryTrigger CONSTRUCT_LIBRARY;
    public static InfernalEnchantedTrigger INFERNAL_ENCHANTED;
    public static SimpleTrigger CONSTRUCT_KILN, SPAWN_HOPPER_FRIEND, EXPLOSIVE_RECIPE, SHEAR_CREEPER;
    public static StringTrigger STATE_TRIGGER;

    @Override
    public void register() {
        CONSTRUCT_LIBRARY = CriteriaTriggers.register(new ConstructLibraryTrigger());
        INFERNAL_ENCHANTED = CriteriaTriggers.register(new InfernalEnchantedTrigger());
        CONSTRUCT_KILN = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "construct_kiln")));
        SPAWN_HOPPER_FRIEND = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "spawn_hopper_friend")));
        EXPLOSIVE_RECIPE = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "explosive_recipe")));
        SHEAR_CREEPER = CriteriaTriggers.register(new SimpleTrigger(new ResourceLocation(ModLib.MODID, "shear_creeper")));
        STATE_TRIGGER = CriteriaTriggers.register(new StringTrigger(new ResourceLocation(ModLib.MODID, "state_trigger")));
    }

}