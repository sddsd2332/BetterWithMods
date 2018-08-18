package betterwithmods.common.penalties;

import betterwithmods.common.penalties.attribute.BWMAttributes;
import betterwithmods.module.Feature;
import org.apache.commons.lang3.Range;

public class BasicPenalty<T extends Number & Comparable> extends Penalty<T> {


    public BasicPenalty(boolean jump, boolean swim, boolean heal, boolean sprint, boolean attack, boolean pain, float speed, float severity, String name, String lang, Feature feature, Range<T> range) {
        super(lang, severity, BWMAttributes.getRange(feature, name, "Numberic range for whether this penalty it active", range),
                BWMAttributes.JUMP.fromConfig(feature, name, jump),
                BWMAttributes.SWIM.fromConfig(feature, name, swim),
                BWMAttributes.HEAL.fromConfig(feature, name, heal),
                BWMAttributes.SPRINT.fromConfig(feature, name, sprint),
                BWMAttributes.ATTACK.fromConfig(feature, name, attack),
                BWMAttributes.PAIN.fromConfig(feature, name, pain),
                BWMAttributes.SPEED.fromConfig(feature, name, speed)
        );

        if (BWMAttributes.isCustom(feature)) {
            feature.config().setDescription(feature.getName() + "." + name, "Configure values for the " + name + " penalty");
        }
    }


}

