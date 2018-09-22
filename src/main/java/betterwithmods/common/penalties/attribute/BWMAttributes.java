package betterwithmods.common.penalties.attribute;

import betterwithmods.lib.ModLib;
import betterwithmods.module.Feature;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Range;

public class BWMAttributes {

    public static Attribute<Boolean> JUMP, SWIM, HEAL, SPRINT, ATTACK, PAIN, GRUE;
    public static Attribute<Float> SPEED, SPOOKED;

    public static void registerAttributes() {
        JUMP = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "jump"), true).setDescription("Can the player jump with this penalty active?");
        SWIM = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "swim"), true).setDescription("Can the player swim with this penalty active?");
        HEAL = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "heal"), true).setDescription("Can the player heal with this penalty active?");
        SPRINT = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "sprint"), true).setDescription("Can the player sprint with this penalty active?");
        ATTACK = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "attack"), true).setDescription("Can the player attack with this penalty active?");
        PAIN = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "pain"), false).setDescription("Is the player in pain? (Plays the OOF noise periodically)");
        GRUE = new BooleanAttribute(new ResourceLocation(ModLib.MODID, "grue"), false).setDescription("Can the player be eaten by the Grue when this is active?");

        SPEED = new FloatAttribute(new ResourceLocation(ModLib.MODID, "speed"), 1f).setDescription("Speed modifier when this penalty is active. (Multiplies the player's existing speed)");
        SPOOKED = new FloatAttribute(new ResourceLocation(ModLib.MODID, "spooked"), 0f).setDescription("Does the player start to go insane when this is active?");
    }

    public static boolean isCustom(Feature feature) {
        return feature.loadProperty("Customized", false).subCategory("penalties").setComment("Set this to true to allow more granular configs to generate and make the penalties work as you please. Requires the game to be started to generate more configs.").get();
    }

    public static <T> AttributeInstance<T> getConfigAttribute(Feature feature, IAttribute<T> parent, String penalty, String desc, T defaultValue) {
        return new AttributeInstance<>(parent, isCustom(feature) ? feature.loadProperty(parent.getRegistryName().getPath(), defaultValue).subCategory(penalty).get() : defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable> Range<T> getRange(Feature feature, String penalty, String comment, Range<T> defaultValue) {
        if (isCustom(feature)) {
            Number max = defaultValue.getMaximum(), min = defaultValue.getMinimum();
            if (max instanceof Float) {
                float upper = feature.loadProperty("Upper Range", max.floatValue()).subCategory(penalty).setComment(comment).get();
                float lower = feature.loadProperty("Lower Range", min.floatValue()).subCategory(penalty).setComment(comment).get();
                return (Range<T>) Range.between(upper, lower);
            } else if (max instanceof Integer) {
                int upper = feature.loadProperty("Upper Range", max.intValue()).subCategory(penalty).get();
                int lower = feature.loadProperty("Lower Range", min.intValue()).subCategory(penalty).get();
                return (Range<T>) Range.between(upper, lower);
            }
        } else {
            return defaultValue;
        }
        return null;
    }

}
