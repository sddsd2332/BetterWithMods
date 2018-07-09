package betterwithmods.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class BWAdvancements {
    public static final ConstructKilnTrigger CONSTRUCT_KILN = CriteriaTriggers.register(new ConstructKilnTrigger());
    public static final ConstructLibraryTrigger CONSTRUCT_LIBRARY = CriteriaTriggers.register(new ConstructLibraryTrigger());
    public static final InfernalEnchantedTrigger INFERNAL_ENCHANTED = CriteriaTriggers.register(new InfernalEnchantedTrigger());

    public static void registerAdvancements() {
    }
}
