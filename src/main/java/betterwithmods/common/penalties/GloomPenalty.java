package betterwithmods.common.penalties;

import betterwithmods.common.penalties.attribute.BWMAttributes;
import org.apache.commons.lang3.Range;

public class GloomPenalty extends Penalty<Integer> {
    public GloomPenalty(boolean grue, boolean jump, float spooked, String name, String lang, String category, float severity, Range<Integer> range) {
        super(lang, severity, BWMAttributes.getRange(category, name, "Numberic range for whether this penalty it active", range),
                BWMAttributes.getBooleanAttribute(BWMAttributes.GRUE, category, name, "Does the player get attacked by the Grue?", grue),
                BWMAttributes.getBooleanAttribute(BWMAttributes.JUMP, category, name, "Can the player jump with this penalty active?", jump),
                BWMAttributes.getFloatAttribute(BWMAttributes.SPOOKED, category, name, "Is the player starting to hear things in the dark?", spooked)
        );
    }
}
