package com.miui.internal.variable.v16;

import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Field;
import miui.reflect.Method;

public class Android_Content_SyncStatusInfo_class extends com.miui.internal.variable.Android_Content_SyncStatusInfo_class {
    private static final Class CLASS;
    private static final Method GET_LAST_FAILURE_MESG_AS_INT = Method.of(CLASS, "getLastFailureMesgAsInt", "(I)I");
    private static final Field INITIALIZE;
    private static final Field LAST_FAILURE_TIME;
    private static final Field LAST_SUCCESS_TIME;
    private static final Field PENDING;

    static {
        Class cls;
        try {
            cls = Class.forName(com.miui.internal.variable.Android_Content_SyncStatusInfo_class.NAME);
        } catch (ClassNotFoundException e) {
            VariableExceptionHandler.getInstance().onThrow(com.miui.internal.variable.Android_Content_SyncStatusInfo_class.NAME, e);
            cls = null;
        }
        CLASS = cls;
        String str = "Z";
        PENDING = Field.of(CLASS, "pending", str);
        INITIALIZE = Field.of(CLASS, "initialize", str);
        String str2 = "J";
        LAST_SUCCESS_TIME = Field.of(CLASS, "lastSuccessTime", str2);
        LAST_FAILURE_TIME = Field.of(CLASS, "lastFailureTime", str2);
    }

    public boolean getInitialize(Object obj) {
        try {
            return INITIALIZE.getBoolean(obj);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.SyncStatusInfo.initialize", e);
            return false;
        }
    }

    public int getLastFailureMesgAsInt(Object obj, int i) {
        try {
            return GET_LAST_FAILURE_MESG_AS_INT.invokeInt(null, obj, Integer.valueOf(i));
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.SyncStatusInfo.getLastFailureMesgAsInt", e);
            return i;
        }
    }

    public long getLastFailureTime(Object obj) {
        try {
            return LAST_FAILURE_TIME.getLong(obj);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.SyncStatusInfo.lastFailureTime", e);
            return 0;
        }
    }

    public long getLastSuccessTime(Object obj) {
        try {
            return LAST_SUCCESS_TIME.getLong(obj);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.SyncStatusInfo.lastSuccessTime", e);
            return 0;
        }
    }

    public boolean getPending(Object obj) {
        try {
            return PENDING.getBoolean(obj);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.content.SyncStatusInfo.pending", e);
            return false;
        }
    }
}
