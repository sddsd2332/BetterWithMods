package betterwithmods.module.hardcore.crafting.brewing;

import betterwithmods.library.lib.ReflectionLib;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IRegistryDelegate;

public class MixPredicateHelper {

    public static void setReagent(Object mixPredicate, Ingredient ingredient) {
        ReflectionHelper.setPrivateValue(ReflectionLib.CLAZZ_MIXPREDICATE, mixPredicate, ingredient, ReflectionLib.MIXPREDICATE_REAGENT);
    }

    public static Ingredient getReagent(Object mixPredicate) {
        return ReflectionHelper.getPrivateValue(ReflectionLib.CLAZZ_MIXPREDICATE, mixPredicate, ReflectionLib.MIXPREDICATE_REAGENT);
    }

    public static PotionType getInputPotionType(Object mixPredicate) {
        Object value = ReflectionHelper.getPrivateValue(ReflectionLib.CLAZZ_MIXPREDICATE, mixPredicate, ReflectionLib.MIXPREDICATE_INPUT);
        if(value instanceof IRegistryDelegate) {
            return ((IRegistryDelegate<PotionType>) value).get();
        } else {
            return (PotionType) value;
        }
    }

    public static PotionType getOutputPotionType(Object mixPredicate) {

        Object value = ReflectionHelper.getPrivateValue(ReflectionLib.CLAZZ_MIXPREDICATE, mixPredicate, ReflectionLib.MIXPREDICATE_OUTPUT);
        if(value instanceof IRegistryDelegate) {
            return ((IRegistryDelegate<PotionType>) value).get();
        } else {
            return (PotionType) value;
        }
    }

}
