package betterwithmods.common.penalties;

import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.util.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.Range;

public class ArmorPenalties extends PenaltyHandler<Integer, BasicPenalty<Integer>> {

    public ArmorPenalties(Feature feature) {
        addDefault(new BasicPenalty<>(true, true, true, true, true, false, 1f, 0f, "light", "armor_penalty.light", feature, Range.between(0, 10)));
        addDefault(new BasicPenalty<>(true, true, true, true, true, false, 0.9f, 0f, "medium", "armor_penalty.medium", feature, Range.between(13, 23)));
        addDefault(new BasicPenalty<>(true, false, true, true, true, false, 0.8f, 0f, "heavy", "armor_penalty.heavy", feature, Range.between(24, 30)));
    }


    @Override
    public BasicPenalty<Integer> getPenalty(EntityPlayer player) {
        return getPenalty(PlayerUtils.getWornArmorWeight(player));
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }
}
