package betterwithmods.common;

import betterwithmods.lib.ModLib;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = ModLib.MODID)
public class BWMSounds {
    @GameRegistry.ObjectHolder("betterwithmods:block.wood.creak")
    public static final SoundEvent WOODCREAK = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.stone.grind")
    public static final SoundEvent STONEGRIND = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.wood.bellow")
    public static final SoundEvent BELLOW = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.wood.chime")
    public static final SoundEvent WOODCHIME = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.metal.chime")
    public static final SoundEvent METALCHIME = null;
    @GameRegistry.ObjectHolder("betterwithmods:entity.player.oof")
    public static final SoundEvent OOF = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.metal.hacksaw")
    public static final SoundEvent METAL_HACKSAW = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.millstone.netherrack")
    public static final SoundEvent MILLSTONE_NETHERRACK = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.saw.cut")
    public static final SoundEvent SAW_CUT = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.bloodwood.break")
    public static final SoundEvent BLOODWOOD_BREAK = null;
    @GameRegistry.ObjectHolder("betterwithmods:block.mechanical.overpower")
    public static final SoundEvent MECHANICAL_OVERPOWER = null;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(registerSound("block.wood.creak"));
        event.getRegistry().register(registerSound("block.stone.grind"));
        event.getRegistry().register(registerSound("block.wood.bellow"));
        event.getRegistry().register(registerSound("block.wood.chime"));
        event.getRegistry().register(registerSound("block.metal.chime"));
        event.getRegistry().register(registerSound("block.metal.hacksaw"));
        event.getRegistry().register(registerSound("block.millstone.netherrack"));
        event.getRegistry().register(registerSound("block.saw.cut"));
        event.getRegistry().register(registerSound("block.mechanical.overpower"));
        event.getRegistry().register(registerSound("entity.player.oof"));

    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(ModLib.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
