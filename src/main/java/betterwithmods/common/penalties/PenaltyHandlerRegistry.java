package betterwithmods.common.penalties;

import betterwithmods.common.penalties.attribute.BWMAttributes;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;

public class PenaltyHandlerRegistry extends HashSet<PenaltyHandler<?, ?>> {

    public boolean canHeal(EntityPlayer player) {
        return stream().allMatch(handler -> handler.getPenalty(player).getBoolean(BWMAttributes.HEAL).getValue());
    }

    public boolean canJump(EntityPlayer player) {
        return stream().allMatch(handler -> handler.getPenalty(player).getBoolean(BWMAttributes.JUMP).getValue());
    }

    public boolean canSprint(EntityPlayer player) {
        return stream().allMatch(handler -> handler.getPenalty(player).getBoolean(BWMAttributes.SPRINT).getValue());
    }

    public boolean canAttack(EntityPlayer player) {
        return stream().allMatch(handler -> handler.getPenalty(player).getBoolean(BWMAttributes.ATTACK).getValue());
    }

    public boolean inPain(EntityPlayer player) {
        return stream().anyMatch(handler -> handler.getPenalty(player).getBoolean(BWMAttributes.PAIN).getValue());
    }

    public float getSpooked(EntityPlayer player) {
        return (float) stream().mapToDouble(handler -> handler.getPenalty(player).getFloat(BWMAttributes.SPOOKED).getValue().doubleValue()).reduce((a, b)-> a*b).orElse(0);
    }

    public boolean attackedByGrue(EntityPlayer player) {
        return stream().anyMatch(handler -> handler.getPenalty(player).getBoolean(BWMAttributes.GRUE).getValue());
    }

    public float getSpeedModifier(EntityPlayer player) {
        return (float) stream().mapToDouble(h -> h.getPenalty(player).getFloat(BWMAttributes.SPEED).getValue()).reduce((a, b) -> a*b).orElse(0);
    }

}
