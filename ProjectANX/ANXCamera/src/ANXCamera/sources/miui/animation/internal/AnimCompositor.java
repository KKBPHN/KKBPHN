package miui.animation.internal;

import android.util.ArrayMap;
import java.util.Map;
import miui.animation.IAnimTarget;
import miui.animation.property.FloatProperty;
import miui.animation.utils.CommonUtils;
import miui.animation.utils.LogUtils;

class AnimCompositor {
    private static final String MAP_PREFIX = "    ";

    private AnimCompositor() {
    }

    static Map createAnimInfo(IAnimTarget iAnimTarget, TransitionInfo transitionInfo) {
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("createAnimInfo, target = ");
            sb.append(iAnimTarget.getTargetObject());
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("tag = ");
            sb3.append(transitionInfo.toTag);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("from = ");
            ArrayMap arrayMap = transitionInfo.fromPropValues;
            String str = MAP_PREFIX;
            sb4.append(CommonUtils.mapToString(arrayMap, str));
            StringBuilder sb5 = new StringBuilder();
            sb5.append("to = ");
            sb5.append(CommonUtils.mapToString(transitionInfo.toPropValues, str));
            LogUtils.debug(sb2, sb3.toString(), sb4.toString(), sb5.toString());
        }
        ArrayMap arrayMap2 = new ArrayMap();
        for (FloatProperty floatProperty : transitionInfo.toPropValues.keySet()) {
            arrayMap2.put(floatProperty, createRunningInfo(iAnimTarget, floatProperty, transitionInfo));
        }
        return arrayMap2;
    }

    private static AnimRunningInfo createRunningInfo(IAnimTarget iAnimTarget, FloatProperty floatProperty, TransitionInfo transitionInfo) {
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("createAnimInfo for ");
            sb.append(iAnimTarget.getTargetObject());
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("toTag = ");
            sb3.append(transitionInfo.toTag);
            LogUtils.debug(sb2, floatProperty.getName(), sb3.toString());
        }
        AnimRunningInfo animRunningInfo = new AnimRunningInfo();
        animRunningInfo.transId = transitionInfo.transitionId;
        animRunningInfo.target = iAnimTarget;
        animRunningInfo.property = floatProperty;
        animRunningInfo.setFromInfo(transitionInfo);
        animRunningInfo.setToInfo(transitionInfo);
        animRunningInfo.setConfig(transitionInfo.config);
        return animRunningInfo;
    }
}
