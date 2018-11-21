package betterwithmods.module.compat.jei;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.lib.ModLib;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class JEILib {

    public static final IIngredientType<IOutput> IOUTPUT = () -> IOutput.class;

    public static final String CAULDRON_BASE = "cauldron";
    public static final String CRUCIBLE_BASE = "crucible";
    public static final String KILN_BASE = "kiln";

    public static final String SAW_BASE = "saw";
    public static final String MILLSTONE_BASE = "millstone";
    public static final String TURNTABLE_BASE = "turntable";
    public static final String HOPPER_BASE = "filtered_hopper";
    public static final String ANVIL_BASE = "steel_anvil";

    public static final String SAW_UID = uid(SAW_BASE);
    public static final String MILLSTONE_UID = uid(MILLSTONE_BASE);
    public static final String TURNTABLE_UID = uid(TURNTABLE_BASE);
    public static final String HOPPER_UID = uid(HOPPER_BASE);
    public static final String ANVIL_UID = uid(ANVIL_BASE);

    public static final String HOPPER_THROW = "filtered_hopper_throw";
    public static final String HOPPER_FILTER = "filtered_hopper_filter";
    public static final String HOPPER_OUTPUT = "filtered_hopper_output";
    public static final String HOPPER_PLACE = "filtered_hopper_place";

    public static final ResourceLocation ANVIL_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/steel_anvil.png");
    public static final ResourceLocation MILLSTONE_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/mill.png");
    public static final ResourceLocation KILN_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/kiln.png");
    public static final ResourceLocation HOPPER_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/hopper.png");
    public static final ResourceLocation SAW_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/saw.png");
    public static final ResourceLocation COOKINGPOT_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/cooking.png");
    public static final ResourceLocation TURNTABLE_TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/jei/turntable.png");


    public static String uid(String name) {
        return new ResourceLocation(ModLib.MODID, name).toString();
    }

    public static String name(String name) {
        return I18n.format(String.format("inventory.%s.title", uid(name)));
    }
}

