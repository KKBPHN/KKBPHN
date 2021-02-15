package com.miui.internal.variable;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;

public class VariableExceptionHandler {
    private static final String TAG = "ExceptionHandler";

    class Holder {
        static final VariableExceptionHandler INSTANCE = new VariableExceptionHandler();

        private Holder() {
        }
    }

    public static VariableExceptionHandler getInstance() {
        return Holder.INSTANCE;
    }

    public void onThrow(String str, Throwable th) {
        String str2;
        if (th instanceof InvocationTargetException) {
            th = th.getCause();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Something thrown - ");
        sb.append(str);
        Log.e(TAG, sb.toString(), th);
        if (str == null || str.length() == 0) {
            str2 = "Something thrown when using version/device dependent code";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Something thrown when using version/device dependent code - ");
            sb2.append(str);
            str2 = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str2);
        sb3.append(" - ");
        sb3.append(th.getMessage());
        throw new RuntimeException(sb3.toString(), th);
    }
}
