package betterwithmods.common.penalties.attribute;

import betterwithmods.BWMod;
import betterwithmods.module.ConfigHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Range;

public class BWMAttributes {

    public static Attribute<Boolean> JUMP, SWIM, HEAL, SPRINT, ATTACK, PAIN;
    public static Attribute<Float> SPEED;

    public static Attribute<Boolean> GRUE;
    public static Attribute<Float> SPOOKED;


    public static void registerAttributes() {
        JUMP = new Attribute<>(new ResourceLocation(BWMod.MODID, "jump"), true);
        SWIM = new Attribute<>(new ResourceLocation(BWMod.MODID, "swim"), true);
        HEAL = new Attribute<>(new ResourceLocation(BWMod.MODID, "heal"), true);
        SPRINT = new Attribute<>(new ResourceLocation(BWMod.MODID, "sprint"), true);
        ATTACK = new Attribute<>(new ResourceLocation(BWMod.MODID, "attack"), true);
        PAIN = new Attribute<>(new ResourceLocation(BWMod.MODID, "pain"), false);

        SPEED = new Attribute<>(new ResourceLocation(BWMod.MODID, "speed"), 1f);

        GRUE = new Attribute<>(new ResourceLocation(BWMod.MODID, "grue"), false);
        SPOOKED = new Attribute<>(new ResourceLocation(BWMod.MODID, "spooked"), 0f);
    }

    public static AttributeInstance<Boolean> getBooleanAttribute(IAttribute<Boolean> parent, String category, String penalty, String desc, Boolean defaultValue) {
        boolean value = ConfigHelper.loadPropBool(parent.getRegistryName().getResourcePath(), String.join(".", category, penalty), desc, defaultValue);
        return new AttributeInstance<>(parent, value);
    }

    public static AttributeInstance<Float> getFloatAttribute(IAttribute<Float> parent, String category, String penalty, String desc, Float defaultValue) {
        float value = (float) ConfigHelper.loadPropDouble(parent.getRegistryName().getResourcePath(), String.join(".", category, penalty), desc, defaultValue);
        return new AttributeInstance<>(parent, value);
    }


    public static <T extends Number & Comparable> Range<T> getRange(String category, String penalty, String desc, Range<T> defaultValue) {
        Number max = defaultValue.getMaximum(), min = defaultValue.getMinimum();
        if (max instanceof Float) {
            Float upper = (float) ConfigHelper.loadPropDouble("Upper Range", String.join(".", category, penalty), desc, max.doubleValue());
            Float lower = (float) ConfigHelper.loadPropDouble("Lower Range", String.join(".", category, penalty), desc, min.doubleValue());
            return (Range<T>) Range.between(upper, lower);
        } else if (max instanceof Integer) {
            Integer upper = ConfigHelper.loadPropInt("Upper Range", String.join(".", category, penalty), desc, max.intValue());
            Integer lower = ConfigHelper.loadPropInt("Lower Range", String.join(".", category, penalty), desc, min.intValue());
            return (Range<T>) Range.between(upper, lower);
        }
        return null;
    }

}
