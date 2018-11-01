package betterwithmods.lib;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class ModLib {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "%VERSION%";
    public static final String NAME = "Better With Mods";
    public static final String DEPENDENCIES = "before:survivalist;after:traverse;after:thaumcraft;after:natura;after:mantle;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering;after:mekanism;after:thermalexpansion;after:ctm;after:geolosys;";
    public static final String GUI_FACTORY = "betterwithmods.client.gui.BWGuiFactory";
    public static final String MINECRAFT_VERISONS = "[1.12, 1.13)";
    public static final String SERVER_PROXY = "betterwithmods.proxy.BWMServerProxy";
    public static final String CLIENT_PROXY = "betterwithmods.proxy.BWMClientProxy";
    public static final GameProfile SAW_PLAYER = new GameProfile(UUID.fromString("3168247D-1B32-4653-8F64-8191B3E9FDF3"), "[BWM Saw]");
    public static final GameProfile MINING_CHARGE = new GameProfile(UUID.fromString("3168247D-1B32-4653-8F64-8191B3E9FDF3"), "[BWM Mining Charge]");
    public static final GameProfile ADVANCED_DISPENSER = new GameProfile(UUID.fromString("B3F51BA1-3DEF-4229-B56C-48883BE91DC3"), "[BWM Advanced Dispenser]");
}
