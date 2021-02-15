package miuix.animation.internal;

import android.util.ArrayMap;
import java.util.concurrent.atomic.AtomicLong;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.controller.AnimState;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.utils.CommonUtils;

class TransitionInfo {
    private static AtomicLong sIdGenerator = new AtomicLong(0);
    AnimConfigLink config;
    ArrayMap fromPropValues = new ArrayMap();
    IAnimTarget target;
    ArrayMap toFlags = new ArrayMap();
    ArrayMap toPropValues = new ArrayMap();
    Object toTag;
    long transitionId = sIdGenerator.getAndIncrement();

    TransitionInfo(IAnimTarget iAnimTarget, AnimState animState, AnimState animState2, AnimConfigLink animConfigLink) {
        this.target = iAnimTarget;
        toPropValues(this.fromPropValues, animState);
        toPropValues(this.toPropValues, animState2);
        this.toTag = animState2.getTag();
        this.config = animConfigLink;
        animState2.getAllConfig(this.config);
    }

    private Number getValue(AnimState animState, FloatProperty floatProperty) {
        return floatProperty instanceof IIntValueProperty ? Integer.valueOf(animState.getInt(floatProperty)) : Float.valueOf(animState.getFloat(floatProperty));
    }

    private void toPropValues(ArrayMap arrayMap, AnimState animState) {
        if (animState != null) {
            for (FloatProperty floatProperty : animState.keySet()) {
                arrayMap.put(floatProperty, getValue(animState, floatProperty));
                long flags = animState.getFlags(floatProperty);
                if (flags != 0) {
                    this.toFlags.put(floatProperty, Long.valueOf(flags));
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransitionInfo{target=");
        sb.append(this.target);
        sb.append(", from=");
        String str = "    ";
        sb.append(CommonUtils.mapToString(this.fromPropValues, str));
        sb.append(", to=");
        sb.append(CommonUtils.mapToString(this.toPropValues, str));
        sb.append(", config=");
        sb.append(this.config);
        sb.append('}');
        return sb.toString();
    }
}
