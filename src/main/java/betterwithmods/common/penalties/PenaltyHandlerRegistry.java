package betterwithmods.common.penalties;

import betterwithmods.common.penalties.attribute.Attribute;
import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.common.penalties.attribute.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PenaltyHandlerRegistry extends HashSet<PenaltyHandler<?, ?>> {

    private final Predicate<Boolean> p = Boolean::booleanValue;

    public boolean canHeal(EntityPlayer player) {
        return booleanAttributes(player, BWMAttributes.HEAL).allMatch(p);
    }

    public boolean canJump(EntityPlayer player) {
        return booleanAttributes(player, BWMAttributes.JUMP).allMatch(p);
    }

    public boolean canSprint(EntityPlayer player) {
        return booleanAttributes(player, BWMAttributes.SPRINT).allMatch(p);
    }

    public boolean canAttack(EntityPlayer player) {
        return booleanAttributes(player, BWMAttributes.ATTACK).allMatch(p);
    }

    public boolean inPain(EntityPlayer player) {
        return booleanAttributes(player, BWMAttributes.PAIN).anyMatch(p);
    }

    public float getSpooked(EntityPlayer player) {
        return floatAttributes(player, BWMAttributes.SPOOKED).reduce((a, b) -> a * b).orElse(0f);
    }

    public float getSpeedModifier(@Nonnull EntityPlayer player) {
        return floatAttributes(player, BWMAttributes.SPEED).reduce((a, b) -> a * b).orElse(0f);
    }

    public boolean attackedByGrue(EntityPlayer player) {
        return booleanAttributes(player, BWMAttributes.GRUE).anyMatch(p);
    }

    @SuppressWarnings("unchecked")
    private Stream<Penalty<?>> handlers(@Nonnull EntityPlayer player) {
        return (Stream<Penalty<?>>) stream().map(handler -> handler.getPenalty(player)).filter(Objects::nonNull);
    }

    private Stream<Boolean> booleanAttributes(EntityPlayer player, Attribute<Boolean> attribute) {
        return handlers(player).map(penalty -> penalty.getBoolean(attribute)).filter(Objects::nonNull).map(IAttributeInstance::getValue);
    }

    private Stream<Float> floatAttributes(EntityPlayer player, Attribute<Float> attribute) {
        return handlers(player).map(penalty -> penalty.getFloat(attribute)).filter(Objects::nonNull).map(IAttributeInstance::getValue);
    }

}
