package com.miui.internal.variable.v16;

import android.graphics.drawable.Drawable;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class.Factory;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Method;

public class Android_Graphics_Drawable_AnimatedRotateDrawable_class extends com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class {
    private static final Class CLASS;
    private static final Method setFramesCount;
    private static final Method setFramesDuration;
    private static final Method start;
    private static final Method stop;

    static {
        Class cls;
        try {
            cls = Class.forName(com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class.NAME);
        } catch (ClassNotFoundException e) {
            VariableExceptionHandler.getInstance().onThrow(com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class.NAME, e);
            cls = null;
        }
        CLASS = cls;
        String str = "(I)V";
        setFramesCount = Method.of(CLASS, "setFramesCount", str);
        setFramesDuration = Method.of(CLASS, "setFramesDuration", str);
        String str2 = "()V";
        start = Method.of(CLASS, "start", str2);
        stop = Method.of(CLASS, BaseEvent.STOP, str2);
    }

    public int getLayoutDirection(Drawable drawable) {
        return Factory.getInstance().get().getLayoutDirection(drawable);
    }

    public void setFramesCount(Drawable drawable, int i) {
        try {
            setFramesCount.invoke(null, drawable, Integer.valueOf(i));
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.AnimatedRotateDrawable.setFramesCount", e);
        }
    }

    public void setFramesDuration(Drawable drawable, int i) {
        try {
            setFramesDuration.invoke(null, drawable, Integer.valueOf(i));
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.AnimatedRotateDrawable.setFramesDuration", e);
        }
    }

    public void start(Drawable drawable) {
        try {
            start.invoke(null, drawable, new Object[0]);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.AnimatedRotateDrawable.start", e);
        }
    }

    public void stop(Drawable drawable) {
        try {
            stop.invoke(null, drawable, new Object[0]);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.AnimatedRotateDrawable.stop", e);
        }
    }
}
