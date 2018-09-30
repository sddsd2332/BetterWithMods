package betterwithmods.lib;

public class ModLib {
    public static final String MODID = "betterwithmods";
    public static final String VERSION = "%VERSION%";
    public static final String NAME = "Better With Mods";
    public static final String DEPENDENCIES = "before:survivalist;after:traverse;after:thaumcraft;after:natura;after:mantle;after:tconstruct;after:minechem;after:natura;after:terrafirmacraft;after:immersiveengineering;after:mekanism;after:thermalexpansion;after:ctm;after:geolosys;";
    public static final String GUI_FACTORY = "betterwithmods.client.gui.BWGuiFactory";
    public static final String MINECRAFT_VERISONS = "[1.12, 1.13)";
    public static final String SERVER_PROXY = "betterwithmods.proxy.BWMServerProxy";
    public static final String CLIENT_PROXY = "betterwithmods.proxy.BWMClientProxy";
}
