package miuix.animation.property;

import java.util.Objects;

public class ColorProperty extends FloatProperty implements IIntValueProperty {
    private int mColorValue;

    public ColorProperty(String str) {
        super(str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ColorProperty.class != obj.getClass()) {
            return false;
        }
        return this.mPropertyName.equals(((ColorProperty) obj).mPropertyName);
    }

    public int getIntValue(Object obj) {
        if (obj instanceof ValueTargetObject) {
            this.mColorValue = ((Integer) ((ValueTargetObject) obj).getPropertyValue(getName(), Integer.TYPE)).intValue();
        }
        return this.mColorValue;
    }

    public float getValue(Object obj) {
        return 0.0f;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mPropertyName});
    }

    public void setIntValue(Object obj, int i) {
        this.mColorValue = i;
        if (obj instanceof ValueTargetObject) {
            ((ValueTargetObject) obj).setPropertyValue(getName(), Integer.TYPE, Integer.valueOf(i));
        }
    }

    public void setValue(Object obj, float f) {
    }
}
