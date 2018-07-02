package betterwithmods.module.compat;

import betterwithmods.BWMod;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.gameplay.miniblocks.MiniBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Natura extends CompatFeature {

    @GameRegistry.ObjectHolder("natura:overworld_planks")
    public static Item OVERWORLD_PLANKS;

    @GameRegistry.ObjectHolder("natura:nether_planks")
    public static Item NETHER_PLANKS;

    public Natura() {
        super("natura");
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void afterItemRegister(RegistryEvent.Register<Item> event) {
        if (BWMod.MODULE_LOADER.isFeatureEnabled(MiniBlocks.class)) {
            MiniBlocks.forceMiniBlock(Ingredient.fromItem(OVERWORLD_PLANKS));
            MiniBlocks.forceMiniBlock(Ingredient.fromItem(NETHER_PLANKS));
        }
    }
}
