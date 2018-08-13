package betterwithmods.util;

import betterwithmods.BWMod;
import net.minecraft.util.text.TextComponentTranslation;

public class TooltipLib {


    public static final String BOOK = "book";

    public static final String ROTATE_WITH_HAND = "rotate_with_hand";

    public static final String RANDOM_OUTPUT = "random_output";

    public static final String CHANCE_OUTPUT = "chance_output";

    public static final String MILLSTONE_BLOCKED = "millstone_blocked";

    public static final String FURNACE_TIME = "furnace_fuel";

    public static final String HANDCRANK_EXHAUSTION = "handcrank_exhaustion";


    public static final String FISHING_NEEDS_OPEN_SKY = "fishing_needs_open_sky";
    public static final String FISHING_NEEDS_BAIT = "fishing_needs_bait";
    public static final String FISHING_NEEDS_DEEP_WATER = "fishing_needs_deep_water";

    public static final String FISHING_ROD_BAITED ="fishing_rod_baited";
    public static final String FISHING_ROD_UNBAITED ="fishing_rod_unbaited";

    public static final String BED_TOO_RESTLESS = "bed_too_restless";


    public static final String BEACON_SPAWN_SET = "beacon_spawn_set";
    public static final String BEACON_SPAWN_ALREDY_SET = "beacon_spawn_already_set";


    public static TextComponentTranslation getInfoComponent(String prefix, String base, Object... format) {
        return new TextComponentTranslation(String.format("%s.%s:%s", prefix, BWMod.MODID, base), format);
    }

    public static TextComponentTranslation getMessageComponent(String base, Object... format) {
        return getInfoComponent("message", base, format);
    }

    public static TextComponentTranslation getTooltipComponent(String base, Object... format) {
        return getInfoComponent("tooltip", base, format);
    }

    public static String getTooltip(String base, Object... format) {
        return getTooltipComponent(base, format).getFormattedText();
    }

    public static String getMessage(String base, Object... format) {
        return getMessageComponent(base, format).getFormattedText();
    }
}
