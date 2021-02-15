package com.iqiyi.android.qigsaw.core.extension.fakecomponents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class OrientationCompat {
    OrientationCompat() {
    }

    static void fixedOrientation(Activity activity, int i) {
        if (i != -1 && VERSION.SDK_INT == 26 && activity.getApplicationInfo().targetSdkVersion > 26 && isTranslucentOrFloating(activity) && !isFixedOrientation(activity)) {
            try {
                Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(activity);
                Field declaredField2 = ActivityInfo.class.getDeclaredField("screenOrientation");
                declaredField2.setAccessible(true);
                if (declaredField2.getInt(obj) == -1) {
                    declaredField2.setInt(obj, i);
                }
            } catch (IllegalAccessException | NoSuchFieldException unused) {
            }
        }
    }

    @SuppressLint({"SoonBlockedPrivateApi"})
    private static boolean isFixedOrientation(Activity activity) {
        boolean z = false;
        try {
            Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(activity);
            Method declaredMethod = ActivityInfo.class.getDeclaredMethod("isFixedOrientation", new Class[0]);
            declaredMethod.setAccessible(true);
            z = ((Boolean) declaredMethod.invoke(obj, new Object[0])).booleanValue();
            return z;
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException unused) {
            return z;
        }
    }

    @SuppressLint({"PrivateApi"})
    private static boolean isTranslucentOrFloating(Activity activity) {
        try {
            Class cls = Class.forName("com.android.internal.R$styleable");
            Field declaredField = cls.getDeclaredField("Window");
            declaredField.setAccessible(true);
            TypedArray obtainStyledAttributes = activity.obtainStyledAttributes((int[]) declaredField.get(null));
            Field declaredField2 = cls.getDeclaredField("Window_windowIsTranslucent");
            declaredField2.setAccessible(true);
            Field declaredField3 = cls.getDeclaredField("Window_windowSwipeToDismiss");
            declaredField3.setAccessible(true);
            Field declaredField4 = cls.getDeclaredField("Window_windowIsFloating");
            declaredField4.setAccessible(true);
            boolean z = obtainStyledAttributes.getBoolean(((Integer) declaredField2.get(null)).intValue(), false);
            boolean z2 = !obtainStyledAttributes.hasValue(((Integer) declaredField2.get(null)).intValue()) && obtainStyledAttributes.getBoolean(((Integer) declaredField3.get(null)).intValue(), false);
            return obtainStyledAttributes.getBoolean(((Integer) declaredField4.get(null)).intValue(), false) || z || z2;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
            return false;
        }
    }

    static int releaseFixedOrientation(Activity activity) {
        if (VERSION.SDK_INT != 26 || activity.getApplicationInfo().targetSdkVersion <= 26 || !isTranslucentOrFloating(activity) || !isFixedOrientation(activity)) {
            return -1;
        }
        try {
            Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(activity);
            Field declaredField2 = ActivityInfo.class.getDeclaredField("screenOrientation");
            declaredField2.setAccessible(true);
            int i = declaredField2.getInt(obj);
            if (i != -1) {
                try {
                    declaredField2.setInt(obj, -1);
                } catch (IllegalAccessException | NoSuchFieldException unused) {
                }
            }
            return i;
        } catch (IllegalAccessException | NoSuchFieldException unused2) {
            return -1;
        }
    }
}
