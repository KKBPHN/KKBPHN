package com.miui.internal.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;

public class ContextHelper {
    private static final String TAG = "ContextHelper";

    private ContextHelper() {
    }

    public static Activity getActivityContextFromView(View view) {
        Context context = ((ViewGroup) view.getRootView()).getChildAt(0).getContext();
        boolean contains = context.getClass().getName().contains("com.android.internal.policy.DecorContext");
        String str = TAG;
        if (contains) {
            try {
                Field declaredField = context.getClass().getDeclaredField("mPhoneWindow");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(context);
                Object invoke = obj.getClass().getMethod("getContext", new Class[0]).invoke(obj, new Object[0]);
                if (invoke != null && (invoke instanceof Context)) {
                    context = (Context) invoke;
                }
            } catch (Exception e) {
                Log.e(str, e.getMessage());
            }
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        Log.i(str, "fail to get activity");
        return null;
    }
}
