package miuix.animation.property;

import android.util.Property;

public abstract class FloatProperty extends Property {
    final String mPropertyName;

    public FloatProperty(String str) {
        super(Float.class, str);
        this.mPropertyName = str;
    }

    public Float get(Object obj) {
        return Float.valueOf(obj == null ? 0.0f : getValue(obj));
    }

    public abstract float getValue(Object obj);

    public final void set(Object obj, Float f) {
        if (obj != null) {
            setValue(obj, f.floatValue());
        }
    }

    public abstract void setValue(Object obj, float f);

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("{");
        sb.append("mPropertyName='");
        sb.append(this.mPropertyName);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
