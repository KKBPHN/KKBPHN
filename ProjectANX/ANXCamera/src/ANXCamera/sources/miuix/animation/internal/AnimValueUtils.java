package miuix.animation.internal;

import miuix.animation.IAnimTarget;
import miuix.animation.controller.AnimState;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.ISpecificProperty;
import miuix.animation.styles.PropertyStyle;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.LogUtils;

public class AnimValueUtils {
    private AnimValueUtils() {
    }

    public static void applyProperty(IAnimTarget iAnimTarget, AnimState animState, long... jArr) {
        if (animState != null && iAnimTarget != null) {
            long j = jArr.length > 0 ? jArr[0] : 0;
            for (FloatProperty floatProperty : animState.keySet()) {
                if (animState.isEnabled(floatProperty) && (j == 0 || animState.hasFlags(floatProperty, j))) {
                    setDeltaValues(iAnimTarget, animState, floatProperty);
                }
            }
        }
    }

    private static float getRealValue(IAnimTarget iAnimTarget, FloatProperty floatProperty, float f) {
        float signum = Math.signum(f);
        float abs = Math.abs(f);
        return abs == 1000000.0f ? signum * CommonUtils.getSize(iAnimTarget, iAnimTarget.getType(floatProperty)) : abs == ((float) AnimState.VIEW_POS) ? iAnimTarget.getValue(floatProperty) * signum : f;
    }

    public static float getValue(IAnimTarget iAnimTarget, FloatProperty floatProperty, float f) {
        return floatProperty instanceof ISpecificProperty ? ((ISpecificProperty) floatProperty).getSpecificValue(f) : getRealValue(iAnimTarget, floatProperty, f);
    }

    private static void logValues(FloatProperty floatProperty, Object obj, Object obj2, String str) {
        if (LogUtils.isLogEnabled()) {
            Object[] objArr = new Object[4];
            objArr[0] = floatProperty.getName();
            StringBuilder sb = new StringBuilder();
            sb.append("fromValue = ");
            sb.append(obj);
            objArr[1] = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("toValue = ");
            sb2.append(obj2);
            objArr[2] = sb2.toString();
            if (str == null) {
                str = "";
            }
            objArr[3] = str;
            LogUtils.debug("setValues", objArr);
        }
    }

    private static void setDeltaValues(IAnimTarget iAnimTarget, AnimState animState, FloatProperty floatProperty) {
        boolean hasFlags = animState.hasFlags(floatProperty, 1);
        if (floatProperty instanceof IIntValueProperty) {
            int i = animState.getInt(floatProperty);
            if (hasFlags) {
                i += iAnimTarget.getIntValue((IIntValueProperty) floatProperty);
            }
            iAnimTarget.setIntValue((IIntValueProperty) floatProperty, i);
            return;
        }
        float f = animState.get(iAnimTarget, floatProperty);
        if (hasFlags) {
            f += iAnimTarget.getValue(floatProperty);
        }
        iAnimTarget.setValue(floatProperty, f);
    }

    static boolean setFloatValues(IAnimTarget iAnimTarget, PropertyStyle propertyStyle, AnimRunningInfo animRunningInfo, Number number, long j) {
        float value = iAnimTarget.getValue(animRunningInfo.property);
        float value2 = getValue(iAnimTarget, animRunningInfo.property, number.floatValue());
        if (CommonUtils.hasFlags(j, 1)) {
            value2 += value;
        }
        if (animRunningInfo.isPhysicsWithVelocity() || value != value2) {
            logValues(animRunningInfo.property, Float.valueOf(value), Float.valueOf(value2), null);
            propertyStyle.setValues(value, value2);
            return true;
        }
        logValues(animRunningInfo.property, Float.valueOf(value), Float.valueOf(value2), "same pos");
        return false;
    }

    static boolean setIntValues(IAnimTarget iAnimTarget, PropertyStyle propertyStyle, AnimRunningInfo animRunningInfo, Number number, long j) {
        int intValue = iAnimTarget.getIntValue((IIntValueProperty) animRunningInfo.property);
        int intValue2 = number.intValue();
        if (CommonUtils.hasFlags(j, 1)) {
            intValue2 += intValue;
        }
        if (animRunningInfo.isPhysicsWithVelocity() || intValue != intValue2) {
            logValues(animRunningInfo.property, Integer.valueOf(intValue), Integer.valueOf(intValue2), null);
            propertyStyle.setIntValues(intValue, intValue2);
            return true;
        }
        logValues(animRunningInfo.property, Integer.valueOf(intValue), Integer.valueOf(intValue2), "same pos");
        return false;
    }
}
