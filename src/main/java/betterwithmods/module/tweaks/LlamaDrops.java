package betterwithmods.module.tweaks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class LlamaDrops extends Feature {

    public static final ResourceLocation LLAMA_LOOT = new ResourceLocation(ModLib.MODID, "entity/llama");

    //Override loottables
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(LootTableList.ENTITIES_LLAMA)) {
            LootTable table = event.getLootTableManager().getLootTableFromLocation(LLAMA_LOOT);
            event.setTable(table);
        }
    }

    @Override
    public String getDescription() {
        return "Add mutton to Llama drops";
    }


    @Override
    public boolean hasEvent() {
        return true;
    }
}
