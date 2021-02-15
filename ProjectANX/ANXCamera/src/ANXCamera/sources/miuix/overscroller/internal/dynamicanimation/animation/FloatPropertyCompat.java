package miuix.overscroller.internal.dynamicanimation.animation;

import android.util.FloatProperty;

public abstract class FloatPropertyCompat {
    final String mPropertyName;

    public FloatPropertyCompat(String str) {
        this.mPropertyName = str;
    }

    public static FloatPropertyCompat createFloatPropertyCompat(final FloatProperty floatProperty) {
        return new FloatPropertyCompat(floatProperty.getName()) {
            public float getValue(Object obj) {
                return ((Float) floatProperty.get(obj)).floatValue();
            }

            public void setValue(Object obj, float f) {
                floatProperty.setValue(obj, f);
            }
        };
    }

    public abstract float getValue(Object obj);

    public abstract void setValue(Object obj, float f);
}
