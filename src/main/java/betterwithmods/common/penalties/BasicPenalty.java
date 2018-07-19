package betterwithmods.common.penalties;

import betterwithmods.BWMod;
import betterwithmods.common.penalties.attribute.BWMAttributes;
import org.apache.commons.lang3.Range;

public class BasicPenalty<T extends Number & Comparable> extends Penalty<T> {


    public BasicPenalty(boolean jump, boolean swim, boolean heal, boolean sprint, boolean attack, boolean pain, float speed, float severity, String name, String lang, String category, Range<T> range) {
        super(lang, severity, BWMAttributes.getRange(category, name, "Numberic range for whether this penalty it active", range),
                BWMAttributes.JUMP.fromConfig(category, name, jump),
                BWMAttributes.SWIM.fromConfig(category, name, swim),
                BWMAttributes.HEAL.fromConfig(category, name, heal),
                BWMAttributes.SPRINT.fromConfig(category, name, sprint),
                BWMAttributes.ATTACK.fromConfig(category, name, attack),
                BWMAttributes.PAIN.fromConfig(category, name, pain),
                BWMAttributes.SPEED.fromConfig(category, name, speed)
        );

        if(BWMAttributes.isCustom(category)) {
            BWMod.MODULE_LOADER.configHelper.setDescription(category + "." + name, "Configure values for the " + name + " penalty");
        }
    }


}

