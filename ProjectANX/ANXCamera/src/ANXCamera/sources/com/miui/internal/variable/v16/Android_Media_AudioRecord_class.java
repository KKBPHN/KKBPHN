package com.miui.internal.variable.v16;

import android.media.AudioRecord;
import miui.reflect.Method;
import miui.reflect.NoSuchMethodException;

public class Android_Media_AudioRecord_class extends com.miui.internal.variable.Android_Media_AudioRecord_class {
    private static final Method setParameters;

    static {
        Method method;
        try {
            method = Method.of(AudioRecord.class, "setParameters", "(Ljava/lang/String;)I");
        } catch (NoSuchMethodException unused) {
            method = null;
        }
        setParameters = method;
    }

    public boolean isExtraParamSupported() {
        return setParameters != null;
    }

    public int setParameters(AudioRecord audioRecord, String str) {
        Method method = setParameters;
        if (method == null) {
            return -1;
        }
        return method.invokeInt(audioRecord.getClass(), audioRecord, str);
    }
}
