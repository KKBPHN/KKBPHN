package miuix.animation;

import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.internal.AnimRunnable;
import miuix.animation.internal.AnimTask;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.VelocityMonitor;

public abstract class IAnimTarget {
    public static final long FLAT_ONESHOT = 1;
    static final ConcurrentHashMap sHandlerCache = new ConcurrentHashMap();
    private static Map sPropertyMap = new ArrayMap();
    private static AtomicInteger sTargetIds = new AtomicInteger(Integer.MAX_VALUE);
    private final AnimRunnable mAnimRunnable = new AnimRunnable();
    private AnimTask mAnimTask = new AnimTask(this);
    private float mDefaultMinVisible = Float.MAX_VALUE;
    private long mFlags;
    /* access modifiers changed from: private */
    public Handler mHandler = getCurHandler();
    private final Integer mId = Integer.valueOf(sTargetIds.decrementAndGet());
    private Map mMinVisibleChanges = new ArrayMap();
    private Map mMonitors = new ArrayMap();
    private SparseArray mVelocityMap = new SparseArray();

    class MonitorInfo {
        VelocityMonitor monitor;
        ResetRunnable resetTask;

        private MonitorInfo() {
            this.monitor = new VelocityMonitor();
            this.resetTask = new ResetRunnable(this);
        }
    }

    class ResetRunnable implements Runnable {
        MonitorInfo mMonitorInfo;
        FloatProperty mProperty;
        WeakReference mTargetRef;

        ResetRunnable(MonitorInfo monitorInfo) {
            this.mMonitorInfo = monitorInfo;
        }

        /* access modifiers changed from: 0000 */
        public void post(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
            WeakReference weakReference = this.mTargetRef;
            if (weakReference == null || weakReference.get() != iAnimTarget) {
                this.mTargetRef = new WeakReference(iAnimTarget);
            }
            this.mProperty = floatProperty;
            iAnimTarget.mHandler.removeCallbacks(this);
            iAnimTarget.mHandler.postDelayed(this, 500);
        }

        public void run() {
            IAnimTarget iAnimTarget = (IAnimTarget) this.mTargetRef.get();
            if (iAnimTarget != null) {
                if (!iAnimTarget.getAnimTask().isRunning(this.mProperty)) {
                    iAnimTarget.setVelocity(this.mProperty, 0.0d);
                }
                this.mMonitorInfo.monitor.clear();
            }
        }
    }

    public IAnimTarget() {
        setMinVisibleChange(0.1f, 9, 10, 11);
        setMinVisibleChange(0.00390625f, 4, 14, 7, 8);
        setMinVisibleChange(0.002f, 2, 3);
    }

    private static synchronized Handler createAndCacheHandler(long j) {
        Handler handler;
        synchronized (IAnimTarget.class) {
            handler = (Handler) sHandlerCache.get(Long.valueOf(j));
            if (handler == null) {
                Looper myLooper = Looper.myLooper();
                if (myLooper == null) {
                    myLooper = Looper.getMainLooper();
                }
                Handler handler2 = new Handler(myLooper);
                sHandlerCache.put(Long.valueOf(j), handler2);
                handler = handler2;
            }
        }
        return handler;
    }

    public static Handler getCurHandler() {
        long id = Thread.currentThread().getId();
        Handler handler = (Handler) sHandlerCache.get(Long.valueOf(id));
        return handler == null ? createAndCacheHandler(id) : handler;
    }

    private MonitorInfo getMonitor(FloatProperty floatProperty) {
        MonitorInfo monitorInfo = (MonitorInfo) this.mMonitors.get(floatProperty);
        if (monitorInfo != null) {
            return monitorInfo;
        }
        MonitorInfo monitorInfo2 = new MonitorInfo();
        this.mMonitors.put(floatProperty, monitorInfo2);
        return monitorInfo2;
    }

    public boolean allowAnimRun() {
        return true;
    }

    public FloatProperty createProperty(String str, Class cls) {
        FloatProperty floatProperty = (FloatProperty) sPropertyMap.get(str);
        if (floatProperty == null) {
            floatProperty = (cls == Integer.TYPE || cls == Integer.class) ? new IntValueProperty(str) : new ValueProperty(str);
            sPropertyMap.put(str, floatProperty);
        }
        return floatProperty;
    }

    public void executeAnim(long j, long j2) {
        this.mAnimRunnable.init(this, j, j2);
        post(this.mAnimRunnable);
    }

    public void executeOnInitialized(Runnable runnable) {
        post(runnable);
    }

    public AnimTask getAnimTask() {
        return this.mAnimTask;
    }

    public float getDefaultMinVisible() {
        return 1.0f;
    }

    public int getId() {
        return this.mId.intValue();
    }

    public int getIntValue(IIntValueProperty iIntValueProperty) {
        Object targetObject = getTargetObject();
        if (targetObject != null) {
            return iIntValueProperty.getIntValue(targetObject);
        }
        return Integer.MAX_VALUE;
    }

    public void getLocationOnScreen(int[] iArr) {
        iArr[1] = 0;
        iArr[0] = 0;
    }

    public float getMinVisibleChange(Object obj) {
        Float f = (Float) this.mMinVisibleChanges.get(obj);
        if (f == null && (obj instanceof FloatProperty)) {
            int type = getType((FloatProperty) obj);
            if (type != -1) {
                f = (Float) this.mMinVisibleChanges.get(Integer.valueOf(type));
            }
        }
        if (f != null) {
            return f.floatValue();
        }
        float f2 = this.mDefaultMinVisible;
        return f2 != Float.MAX_VALUE ? f2 : getDefaultMinVisible();
    }

    public abstract FloatProperty getProperty(int i);

    public abstract Object getTargetObject();

    public abstract int getType(FloatProperty floatProperty);

    public float getValue(int i) {
        return getValue(getProperty(i));
    }

    public float getValue(FloatProperty floatProperty) {
        Object targetObject = getTargetObject();
        if (targetObject != null) {
            return floatProperty.getValue(targetObject);
        }
        return Float.MAX_VALUE;
    }

    public double getVelocity(int i) {
        Double d = (Double) this.mVelocityMap.get(i);
        if (d != null) {
            return d.doubleValue();
        }
        return 0.0d;
    }

    public double getVelocity(FloatProperty floatProperty) {
        return getVelocity(floatProperty.getName().hashCode());
    }

    public boolean hasFlags(long j) {
        return CommonUtils.hasFlags(this.mFlags, j);
    }

    public boolean isValid() {
        return true;
    }

    public void onFrameEnd(boolean z) {
    }

    public void post(Runnable runnable) {
        if (this.mHandler.getLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            this.mHandler.post(runnable);
        }
    }

    public IAnimTarget setDefaultMinVisibleChange(float f) {
        this.mDefaultMinVisible = f;
        return this;
    }

    public void setFlags(long j) {
        this.mFlags = j;
    }

    public void setIntValue(IIntValueProperty iIntValueProperty, int i) {
        Object targetObject = getTargetObject();
        if (targetObject != null && i != Integer.MAX_VALUE) {
            iIntValueProperty.setIntValue(targetObject, i);
        }
    }

    public IAnimTarget setMinVisibleChange(float f, int... iArr) {
        for (int valueOf : iArr) {
            this.mMinVisibleChanges.put(Integer.valueOf(valueOf), Float.valueOf(f));
        }
        return this;
    }

    public IAnimTarget setMinVisibleChange(float f, String... strArr) {
        for (String valueProperty : strArr) {
            setMinVisibleChange((Object) new ValueProperty(valueProperty), f);
        }
        return this;
    }

    public IAnimTarget setMinVisibleChange(Object obj, float f) {
        this.mMinVisibleChanges.put(obj, Float.valueOf(f));
        return this;
    }

    public void setValue(FloatProperty floatProperty, float f) {
        Object targetObject = getTargetObject();
        if (targetObject != null && f != Float.MAX_VALUE) {
            floatProperty.setValue(targetObject, f);
        }
    }

    public void setVelocity(int i, double d) {
        if (d != 3.4028234663852886E38d) {
            this.mVelocityMap.put(i, Double.valueOf(d));
        }
    }

    public void setVelocity(FloatProperty floatProperty, double d) {
        setVelocity(floatProperty.getName().hashCode(), d);
    }

    public boolean shouldUseIntValue(FloatProperty floatProperty) {
        return floatProperty instanceof IIntValueProperty;
    }

    public void trackVelocity(FloatProperty floatProperty, double d) {
        MonitorInfo monitor = getMonitor(floatProperty);
        monitor.monitor.update(d);
        float velocity = monitor.monitor.getVelocity(0);
        if (velocity != 0.0f) {
            monitor.resetTask.post(this, floatProperty);
            setVelocity(floatProperty, (double) velocity);
        }
    }
}
