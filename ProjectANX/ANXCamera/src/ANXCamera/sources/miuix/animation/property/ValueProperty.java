package miuix.animation.property;

import java.util.Objects;

public class ValueProperty extends FloatProperty {
    public ValueProperty(String str) {
        super(str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !ValueProperty.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        return Objects.equals(getName(), ((ValueProperty) obj).getName());
    }

    public float getValue(Object obj) {
        if (obj instanceof ValueTargetObject) {
            Float f = (Float) ((ValueTargetObject) obj).getPropertyValue(getName(), Float.TYPE);
            if (f != null) {
                return f.floatValue();
            }
        }
        return 0.0f;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{getName()});
    }

    public void setValue(Object obj, float f) {
        if (obj instanceof ValueTargetObject) {
            ((ValueTargetObject) obj).setPropertyValue(getName(), Float.TYPE, Float.valueOf(f));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValueProperty{name=");
        sb.append(getName());
        sb.append('}');
        return sb.toString();
    }
}
