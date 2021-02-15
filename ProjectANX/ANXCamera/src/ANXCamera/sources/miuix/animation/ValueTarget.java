package miuix.animation;

import miuix.animation.property.ColorProperty;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.property.ValueTargetObject;
import miuix.animation.property.ViewProperty;

public class ValueTarget extends IAnimTarget {
    private static final float DEFAULT_MIN_VALUE = 0.002f;
    static ITargetCreator sCreator = new ITargetCreator() {
        public IAnimTarget createTarget(Object obj) {
            return new ValueTarget(obj);
        }
    };
    private ValueTargetObject mTargetObj;

    public ValueTarget() {
        this(null);
    }

    private ValueTarget(Object obj) {
        if (obj == null) {
            obj = Integer.valueOf(getId());
        }
        this.mTargetObj = new ValueTargetObject(obj);
    }

    private boolean isPredefinedProperty(Object obj) {
        return (obj instanceof ValueProperty) || (obj instanceof ViewProperty) || (obj instanceof ColorProperty);
    }

    public float getDefaultMinVisible() {
        return 0.002f;
    }

    public int getIntValue(String str) {
        return getIntValue((IIntValueProperty) new IntValueProperty(str));
    }

    public int getIntValue(IIntValueProperty iIntValueProperty) {
        boolean isPredefinedProperty = isPredefinedProperty(iIntValueProperty);
        ValueTargetObject valueTargetObject = this.mTargetObj;
        if (!isPredefinedProperty) {
            return iIntValueProperty.getIntValue(valueTargetObject.getRealObject());
        }
        Integer num = (Integer) valueTargetObject.getPropertyValue(iIntValueProperty.getName(), Integer.TYPE);
        return num == null ? 0 : num.intValue();
    }

    public float getMinVisibleChange(Object obj) {
        if (!(obj instanceof IIntValueProperty) || (obj instanceof ColorProperty)) {
            return super.getMinVisibleChange(obj);
        }
        return 1.0f;
    }

    public FloatProperty getProperty(int i) {
        return null;
    }

    public Object getTargetObject() {
        return this.mTargetObj;
    }

    public int getType(FloatProperty floatProperty) {
        return -1;
    }

    public float getValue(String str) {
        return getValue((FloatProperty) new ValueProperty(str));
    }

    public float getValue(FloatProperty floatProperty) {
        boolean isPredefinedProperty = isPredefinedProperty(floatProperty);
        ValueTargetObject valueTargetObject = this.mTargetObj;
        if (!isPredefinedProperty) {
            return floatProperty.getValue(valueTargetObject.getRealObject());
        }
        Float f = (Float) valueTargetObject.getPropertyValue(floatProperty.getName(), Float.TYPE);
        return f == null ? 0.0f : f.floatValue();
    }

    public double getVelocity(String str) {
        return getVelocity(str.hashCode());
    }

    public boolean isValid() {
        return this.mTargetObj.isValid();
    }

    public void setIntValue(String str, int i) {
        setIntValue((IIntValueProperty) new IntValueProperty(str), i);
    }

    public void setIntValue(IIntValueProperty iIntValueProperty, int i) {
        boolean isPredefinedProperty = isPredefinedProperty(iIntValueProperty);
        ValueTargetObject valueTargetObject = this.mTargetObj;
        if (isPredefinedProperty) {
            valueTargetObject.setPropertyValue(iIntValueProperty.getName(), Integer.TYPE, Integer.valueOf(i));
        } else {
            iIntValueProperty.setIntValue(valueTargetObject.getRealObject(), i);
        }
    }

    public void setValue(String str, float f) {
        setValue((FloatProperty) new ValueProperty(str), f);
    }

    public void setValue(FloatProperty floatProperty, float f) {
        boolean isPredefinedProperty = isPredefinedProperty(floatProperty);
        ValueTargetObject valueTargetObject = this.mTargetObj;
        if (isPredefinedProperty) {
            valueTargetObject.setPropertyValue(floatProperty.getName(), Float.TYPE, Float.valueOf(f));
        } else {
            floatProperty.setValue(valueTargetObject.getRealObject(), f);
        }
    }

    public void setVelocity(String str, double d) {
        setVelocity(str.hashCode(), d);
    }
}
