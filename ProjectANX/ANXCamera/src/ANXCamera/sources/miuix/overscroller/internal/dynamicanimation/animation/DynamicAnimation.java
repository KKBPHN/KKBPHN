package miuix.overscroller.internal.dynamicanimation.animation;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import java.util.ArrayList;

public abstract class DynamicAnimation implements AnimationFrameCallback {
    public static final ViewProperty ALPHA = new ViewProperty("alpha") {
        public float getValue(View view) {
            return view.getAlpha();
        }

        public void setValue(View view, float f) {
            view.setAlpha(f);
        }
    };
    public static final float MIN_VISIBLE_CHANGE_ALPHA = 0.00390625f;
    public static final float MIN_VISIBLE_CHANGE_PIXELS = 1.0f;
    public static final float MIN_VISIBLE_CHANGE_ROTATION_DEGREES = 0.1f;
    public static final float MIN_VISIBLE_CHANGE_SCALE = 0.002f;
    public static final ViewProperty ROTATION = new ViewProperty("rotation") {
        public float getValue(View view) {
            return view.getRotation();
        }

        public void setValue(View view, float f) {
            view.setRotation(f);
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty("rotationX") {
        public float getValue(View view) {
            return view.getRotationX();
        }

        public void setValue(View view, float f) {
            view.setRotationX(f);
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty("rotationY") {
        public float getValue(View view) {
            return view.getRotationY();
        }

        public void setValue(View view, float f) {
            view.setRotationY(f);
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty("scaleX") {
        public float getValue(View view) {
            return view.getScaleX();
        }

        public void setValue(View view, float f) {
            view.setScaleX(f);
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty("scaleY") {
        public float getValue(View view) {
            return view.getScaleY();
        }

        public void setValue(View view, float f) {
            view.setScaleY(f);
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") {
        public float getValue(View view) {
            return (float) view.getScrollX();
        }

        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") {
        public float getValue(View view) {
            return (float) view.getScrollY();
        }

        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }
    };
    private static final float THRESHOLD_MULTIPLIER = 0.75f;
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") {
        public float getValue(View view) {
            return view.getTranslationX();
        }

        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") {
        public float getValue(View view) {
            return view.getTranslationY();
        }

        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") {
        public float getValue(View view) {
            return view.getTranslationZ();
        }

        public void setValue(View view, float f) {
            view.setTranslationZ(f);
        }
    };
    private static final float UNSET = Float.MAX_VALUE;
    public static final ViewProperty X = new ViewProperty("x") {
        public float getValue(View view) {
            return view.getX();
        }

        public void setValue(View view, float f) {
            view.setX(f);
        }
    };
    public static final ViewProperty Y = new ViewProperty("y") {
        public float getValue(View view) {
            return view.getY();
        }

        public void setValue(View view, float f) {
            view.setY(f);
        }
    };
    public static final ViewProperty Z = new ViewProperty("z") {
        public float getValue(View view) {
            return view.getZ();
        }

        public void setValue(View view, float f) {
            view.setZ(f);
        }
    };
    private final ArrayList mEndListeners;
    private long mLastFrameTime;
    private boolean mManualAnim;
    float mMaxValue;
    float mMinValue;
    private float mMinVisibleChange;
    final FloatPropertyCompat mProperty;
    boolean mRunning;
    boolean mStartValueIsSet;
    final Object mTarget;
    private final ArrayList mUpdateListeners;
    float mValue;
    float mVelocity;

    class MassState {
        float mValue;
        float mVelocity;

        MassState() {
        }
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    public abstract class ViewProperty extends FloatPropertyCompat {
        private ViewProperty(String str) {
            super(str);
        }
    }

    DynamicAnimation(Object obj, FloatPropertyCompat floatPropertyCompat) {
        float f;
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -this.mMaxValue;
        this.mLastFrameTime = 0;
        this.mEndListeners = new ArrayList();
        this.mUpdateListeners = new ArrayList();
        this.mTarget = obj;
        this.mProperty = floatPropertyCompat;
        FloatPropertyCompat floatPropertyCompat2 = this.mProperty;
        if (floatPropertyCompat2 == ROTATION || floatPropertyCompat2 == ROTATION_X || floatPropertyCompat2 == ROTATION_Y) {
            f = 0.1f;
        } else if (floatPropertyCompat2 == ALPHA || floatPropertyCompat2 == SCALE_X || floatPropertyCompat2 == SCALE_Y) {
            this.mMinVisibleChange = 0.00390625f;
            return;
        } else {
            f = 1.0f;
        }
        this.mMinVisibleChange = f;
    }

    DynamicAnimation(final FloatValueHolder floatValueHolder) {
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -this.mMaxValue;
        this.mLastFrameTime = 0;
        this.mEndListeners = new ArrayList();
        this.mUpdateListeners = new ArrayList();
        this.mTarget = null;
        this.mProperty = new FloatPropertyCompat("FloatValueHolder") {
            public float getValue(Object obj) {
                return floatValueHolder.getValue();
            }

            public void setValue(Object obj, float f) {
                floatValueHolder.setValue(f);
            }
        };
        this.mMinVisibleChange = 1.0f;
    }

    private void endAnimationInternal(boolean z) {
        this.mRunning = false;
        if (!this.mManualAnim) {
            AnimationHandler.getInstance().removeCallback(this);
        }
        this.mManualAnim = false;
        this.mLastFrameTime = 0;
        this.mStartValueIsSet = false;
        for (int i = 0; i < this.mEndListeners.size(); i++) {
            if (this.mEndListeners.get(i) != null) {
                ((OnAnimationEndListener) this.mEndListeners.get(i)).onAnimationEnd(this, z, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mEndListeners);
    }

    private float getPropertyValue() {
        return this.mProperty.getValue(this.mTarget);
    }

    private static void removeEntry(ArrayList arrayList, Object obj) {
        int indexOf = arrayList.indexOf(obj);
        if (indexOf >= 0) {
            arrayList.set(indexOf, null);
        }
    }

    private static void removeNullEntries(ArrayList arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    private void startAnimationInternal(boolean z) {
        if (!this.mRunning) {
            this.mManualAnim = z;
            this.mRunning = true;
            if (!this.mStartValueIsSet) {
                this.mValue = getPropertyValue();
            }
            float f = this.mValue;
            if (f > this.mMaxValue || f < this.mMinValue) {
                StringBuilder sb = new StringBuilder();
                sb.append("Starting value(");
                sb.append(this.mValue);
                sb.append(") need to be in between min value(");
                sb.append(this.mMinValue);
                sb.append(") and max value(");
                sb.append(this.mMaxValue);
                sb.append(")");
                throw new IllegalArgumentException(sb.toString());
            } else if (!z) {
                AnimationHandler.getInstance().addAnimationFrameCallback(this, 0);
            }
        }
    }

    public DynamicAnimation addEndListener(OnAnimationEndListener onAnimationEndListener) {
        if (!this.mEndListeners.contains(onAnimationEndListener)) {
            this.mEndListeners.add(onAnimationEndListener);
        }
        return this;
    }

    public DynamicAnimation addUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        if (!isRunning()) {
            if (!this.mUpdateListeners.contains(onAnimationUpdateListener)) {
                this.mUpdateListeners.add(onAnimationUpdateListener);
            }
            return this;
        }
        throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
    }

    public void cancel() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
        } else if (this.mRunning) {
            endAnimationInternal(true);
        }
    }

    public boolean doAnimationFrame(long j) {
        long j2 = this.mLastFrameTime;
        if (j2 == 0) {
            this.mLastFrameTime = j;
            setPropertyValue(this.mValue);
            return false;
        }
        long j3 = j - j2;
        this.mLastFrameTime = j;
        boolean updateValueAndVelocity = updateValueAndVelocity(j3);
        this.mValue = Math.min(this.mValue, this.mMaxValue);
        this.mValue = Math.max(this.mValue, this.mMinValue);
        setPropertyValue(this.mValue);
        if (updateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return updateValueAndVelocity;
    }

    public abstract float getAcceleration(float f, float f2);

    public float getMinimumVisibleChange() {
        return this.mMinVisibleChange;
    }

    /* access modifiers changed from: 0000 */
    public float getValueThreshold() {
        return this.mMinVisibleChange * 0.75f;
    }

    public abstract boolean isAtEquilibrium(float f, float f2);

    public boolean isRunning() {
        return this.mRunning;
    }

    public void removeEndListener(OnAnimationEndListener onAnimationEndListener) {
        removeEntry(this.mEndListeners, onAnimationEndListener);
    }

    public void removeUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        removeEntry(this.mUpdateListeners, onAnimationUpdateListener);
    }

    public DynamicAnimation setMaxValue(float f) {
        this.mMaxValue = f;
        return this;
    }

    public DynamicAnimation setMinValue(float f) {
        this.mMinValue = f;
        return this;
    }

    public DynamicAnimation setMinimumVisibleChange(float f) {
        if (f > 0.0f) {
            this.mMinVisibleChange = f;
            setValueThreshold(f * 0.75f);
            return this;
        }
        throw new IllegalArgumentException("Minimum visible change must be positive.");
    }

    /* access modifiers changed from: 0000 */
    public void setPropertyValue(float f) {
        this.mProperty.setValue(this.mTarget, f);
        for (int i = 0; i < this.mUpdateListeners.size(); i++) {
            if (this.mUpdateListeners.get(i) != null) {
                ((OnAnimationUpdateListener) this.mUpdateListeners.get(i)).onAnimationUpdate(this, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mUpdateListeners);
    }

    public DynamicAnimation setStartValue(float f) {
        this.mValue = f;
        this.mStartValueIsSet = true;
        return this;
    }

    public DynamicAnimation setStartVelocity(float f) {
        this.mVelocity = f;
        return this;
    }

    public abstract void setValueThreshold(float f);

    public void start() {
        start(false);
    }

    public void start(boolean z) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        } else if (!this.mRunning) {
            startAnimationInternal(z);
        }
    }

    public abstract boolean updateValueAndVelocity(long j);
}
