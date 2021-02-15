package com.miui.internal.variable.v16;

import android.os.Process;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Method;

public class Android_Os_Process_class extends com.miui.internal.variable.Android_Os_Process_class {
    private static final Method getFreeMemory;
    private static final Method getTotalMemory;

    static {
        String str = "()J";
        getTotalMemory = Method.of(Process.class, "getTotalMemory", str);
        getFreeMemory = Method.of(Process.class, "getFreeMemory", str);
    }

    public long getFreeMemory() {
        try {
            return getFreeMemory.invokeLong(null, null, new Object[0]);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.os.Process.getFreeMemory", e);
            return 0;
        }
    }

    public long getTotalMemory() {
        try {
            return getTotalMemory.invokeLong(null, null, new Object[0]);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.os.Process.getTotalMemory", e);
            return 0;
        }
    }
}
