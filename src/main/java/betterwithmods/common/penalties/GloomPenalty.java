package betterwithmods.common.penalties;

import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.library.common.modularity.impl.Feature;
import org.apache.commons.lang3.Range;

public class GloomPenalty extends Penalty<Integer> {
    public GloomPenalty(boolean grue, boolean jump, float spooked, String name, String lang, Feature feature, float severity, Range<Integer> range) {
        super(lang, severity, BWMAttributes.getRange(feature, name, "Numberic range for whether this penalty it active", range),
                BWMAttributes.GRUE.fromConfig(feature, name, grue),
                BWMAttributes.JUMP.fromConfig(feature, name, jump),
                BWMAttributes.SPOOKED.fromConfig(feature, name, spooked)
        );
    }


}
