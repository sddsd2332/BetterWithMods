package betterwithmods.common.penalties;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.Range;

public class HungerPenalties extends PenaltyHandler<Integer, BasicPenalty<Integer>> {
    public HungerPenalties(Feature feature) {
        super();
        addDefault(new BasicPenalty<>(true, true, true, true, true, false, 1f, 0f, "none", "hunger_penalty.none", feature, Range.between(60, 25)));
        addPenalty(new BasicPenalty<>(true, true, false, true, true, false, 0.75f, 1 / 5f, "peckish", "hunger_penalty.peckish", feature, Range.between(24, 18)));
        addPenalty(new BasicPenalty<>(true, true, false, false, true, false, 0.75f, 2 / 5f, "hungry", "hunger_penalty.hungry", feature, Range.between(17, 13)));
        addPenalty(new BasicPenalty<>(false, false, false, false, true, false, 0.5f, 3 / 5f, "famished", "hunger_penalty.famished", feature, Range.between(12, 7)));
        addPenalty(new BasicPenalty<>(false, false, false, false, true, false, 0.25f, 4 / 5f, "starving", "hunger_penalty.starving", feature, Range.between(6, 1)));
        addPenalty(new BasicPenalty<>(false, false, false, false, true, false, 0.25f, 1, "dying", "hunger_penalty.dying", feature, Range.between(0, -1)));
    }

    @Override
    public BasicPenalty<Integer> getPenalty(EntityPlayer player) {
        int level = player.getFoodStats().getFoodLevel();
        return getPenalty(level);
    }
}
