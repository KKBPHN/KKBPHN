package miui.animation;

import android.os.Handler;
import android.os.Looper;
import miui.animation.property.ColorProperty;
import miui.animation.property.FloatProperty;
import miui.animation.property.IIntValueProperty;
import miui.animation.property.IntValueProperty;
import miui.animation.property.ValueProperty;
import miui.animation.property.ValueTargetObject;

public class ValueTarget extends IAnimTarget {
    private static final float DEFAULT_MIN_VALUE = 0.002f;
    static ITargetCreator sCreator = new ITargetCreator() {
        public IAnimTarget createTarget(Object obj) {
            return new ValueTarget(obj);
        }
    };
    private Handler mHandler;
    private ValueTargetObject mTargetObj;

    public ValueTarget() {
        this(null);
    }

    private ValueTarget(Object obj) {
        if (obj == null) {
            obj = Integer.valueOf(getId());
        }
        this.mTargetObj = new ValueTargetObject(obj);
        Looper myLooper = Looper.myLooper();
        if (myLooper != Looper.getMainLooper()) {
            this.mHandler = new Handler(myLooper);
        }
    }

    public float getDefaultMinVisible() {
        return 0.002f;
    }

    public int getIntValue(String str) {
        return getIntValue((IIntValueProperty) new IntValueProperty(str));
    }

    public int getIntValue(IIntValueProperty iIntValueProperty) {
        Integer num = (Integer) this.mTargetObj.getPropertyValue(iIntValueProperty.getName(), Integer.TYPE);
        if (num == null) {
            return 0;
        }
        return num.intValue();
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
        Float f = (Float) this.mTargetObj.getPropertyValue(floatProperty.getName(), Float.TYPE);
        if (f == null) {
            return 0.0f;
        }
        return f.floatValue();
    }

    public double getVelocity(String str) {
        return getVelocity(new ValueProperty(str));
    }

    public boolean isValid() {
        return this.mTargetObj.isValid();
    }

    public void post(Runnable runnable) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public void setIntValue(String str, int i) {
        setIntValue((IIntValueProperty) new IntValueProperty(str), i);
    }

    public void setIntValue(IIntValueProperty iIntValueProperty, int i) {
        this.mTargetObj.setPropertyValue(iIntValueProperty.getName(), Integer.TYPE, Integer.valueOf(i));
    }

    public void setValue(String str, float f) {
        setValue((FloatProperty) new ValueProperty(str), f);
    }

    public void setValue(FloatProperty floatProperty, float f) {
        this.mTargetObj.setPropertyValue(floatProperty.getName(), Float.TYPE, Float.valueOf(f));
    }
}
