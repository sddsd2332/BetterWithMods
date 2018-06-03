package betterwithmods.common.penalties;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class PenaltyHandler<T extends Number & Comparable, P extends Penalty<T>> {
    protected Set<P> penalties = Sets.newHashSet();
    private P defaultPenalty;

    public PenaltyHandler() {
    }

    public void addDefault(P defaultPenalty) {
        this.defaultPenalty = defaultPenalty;
        this.addPenalty(defaultPenalty);
    }

    public void addPenalty(P penalty) {
        penalties.add(penalty);
    }

    @Nonnull
    public P getPenalty(T t) {
        return penalties.stream().filter(p -> p.inRange(t)).findFirst().orElse(defaultPenalty);
    }

    public abstract P getPenalty(EntityPlayer player);
}
