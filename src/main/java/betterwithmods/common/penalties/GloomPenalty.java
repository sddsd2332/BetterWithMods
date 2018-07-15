package betterwithmods.common.penalties;

import betterwithmods.common.penalties.attribute.BWMAttributes;
import org.apache.commons.lang3.Range;

public class GloomPenalty extends Penalty<Integer> {
    public GloomPenalty(boolean grue, boolean jump, float spooked, String name, String lang, String category, float severity, Range<Integer> range) {
        super(lang, severity, BWMAttributes.getRange(category, name, "Numberic range for whether this penalty it active", range),
                BWMAttributes.GRUE.fromConfig(category, name, grue),
                BWMAttributes.JUMP.fromConfig(category, name, jump),
                BWMAttributes.SPOOKED.fromConfig(category, name, spooked)
        );
    }


}
