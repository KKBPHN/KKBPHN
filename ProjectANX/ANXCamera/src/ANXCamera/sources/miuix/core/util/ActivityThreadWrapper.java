package miuix.core.util;

import android.app.Application;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ActivityThreadWrapper {
    private static ActivityThreadWrapper sInstance;
    private Object mCurrentActivityThread = null;
    private volatile Application mCurrentApplication = null;
    private Object mInitialLoadedApk;
    private String mInitialPackageName;

    private ActivityThreadWrapper() {
        try {
            Method declaredMethod = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            this.mCurrentActivityThread = declaredMethod.invoke(null, new Object[0]);
        } catch (Exception unused) {
            this.mCurrentActivityThread = null;
        }
    }

    public static ActivityThreadWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new ActivityThreadWrapper();
        }
        return sInstance;
    }

    public Object currentActivityThread() {
        return this.mCurrentActivityThread;
    }

    public Application currentApplication() {
        try {
            if (this.mCurrentApplication == null) {
                Method declaredMethod = this.mCurrentActivityThread.getClass().getDeclaredMethod("currentApplication", new Class[0]);
                declaredMethod.setAccessible(true);
                this.mCurrentApplication = (Application) declaredMethod.invoke(null, new Object[0]);
            }
            return this.mCurrentApplication;
        } catch (Exception unused) {
            return null;
        }
    }

    public Object getInitialLoadedApk(Object obj) {
        getInitialPackageName(obj);
        return this.mInitialLoadedApk;
    }

    public String getInitialPackageName(Object obj) {
        try {
            if (this.mInitialPackageName == null) {
                Field declaredField = obj.getClass().getDeclaredField("mPackages");
                declaredField.setAccessible(true);
                Map map = (Map) declaredField.get(obj);
                this.mInitialPackageName = (String) map.keySet().iterator().next();
                this.mInitialLoadedApk = ((WeakReference) map.values().iterator().next()).get();
            }
            return this.mInitialPackageName;
        } catch (Exception unused) {
            return null;
        }
    }
}
