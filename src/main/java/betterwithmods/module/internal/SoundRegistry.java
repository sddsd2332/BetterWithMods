package betterwithmods.module.internal;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.RequiredFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
@GameRegistry.ObjectHolder(ModLib.MODID)
public class SoundRegistry extends RequiredFeature {

    public static final SoundEvent
            BLOCK_WOOD_CREAK = null,
            BLOCK_GRIND_NORMAL = null,
            BLOCK_WOOD_BELLOW = null,
            BLOCK_CHIME_WOOD = null,
            BLOCK_CHIME_METAL = null,
            BLOCK_GRIND_SCREAM = null,
            BLOCK_SAW_CUT = null,
            BLOCK_MECHANICAL_OVERPOWER = null,
            ENTITY_PLAYER_OOF = null;


    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(registerSound("BLOCK_WOOD_CREAK"));
        event.getRegistry().register(registerSound("BLOCK_GRIND_NORMAL"));
        event.getRegistry().register(registerSound("BLOCK_WOOD_BELLOW"));
        event.getRegistry().register(registerSound("BLOCK_CHIME_WOOD"));
        event.getRegistry().register(registerSound("BLOCK_CHIME_METAL"));
        event.getRegistry().register(registerSound("BLOCK_GRIND_SCREAM"));
        event.getRegistry().register(registerSound("BLOCK_SAW_CUT"));
        event.getRegistry().register(registerSound("BLOCK_MECHANICAL_OVERPOWER"));
        event.getRegistry().register(registerSound("ENTITY_PLAYER_OOF"));

    }


    private static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(ModLib.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
