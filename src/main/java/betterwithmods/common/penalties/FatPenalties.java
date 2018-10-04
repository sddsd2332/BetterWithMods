package betterwithmods.common.penalties;

import betterwithmods.library.common.modularity.impl.Feature;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.Range;

public class FatPenalties extends PenaltyHandler<Float, BasicPenalty<Float>> {
    public FatPenalties(Feature feature) {
        super();
        addDefault(new BasicPenalty<>(true, true, true, true, true, false, 1f, 0f, "none", "fat_penalty.none", feature, Range.between(0f, 36f)));
        addPenalty(new BasicPenalty<>(true, true, true, false, true, false, 1f, 1 / 4f, "plump", "fat_penalty.plump", feature, Range.between(36f, 42f)));
        addPenalty(new BasicPenalty<>(true, true, true, false, true, false, 1f, 2 / 4f, "chubby", "fat_penalty.chubby", feature, Range.between(42f, 48f)));
        addPenalty(new BasicPenalty<>(false, false, false, false, true, false, 0.5f, 3 / 4f, "fat", "fat_penalty.fat", feature, Range.between(48f, 52f)));
        addPenalty(new BasicPenalty<>(false, false, false, false, true, false, 0.25f, 1f, "obese", "fat_penalty.obese", feature, Range.between(52f, 60f)));
    }

    @Override
    public BasicPenalty<Float> getPenalty(EntityPlayer player) {
        float level = player.getFoodStats().getSaturationLevel();
        return getPenalty(level);
    }
}
