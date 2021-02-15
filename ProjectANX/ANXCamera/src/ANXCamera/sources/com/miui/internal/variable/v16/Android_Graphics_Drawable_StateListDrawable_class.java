package com.miui.internal.variable.v16;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class.Factory;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Method;

public class Android_Graphics_Drawable_StateListDrawable_class extends com.miui.internal.variable.Android_Graphics_Drawable_StateListDrawable_class {
    private static final Method getStateCount = Method.of(StateListDrawable.class, "getStateCount", "()I");
    private static final Method getStateDrawable = Method.of(StateListDrawable.class, "getStateDrawable", "(I)Landroid/graphics/drawable/Drawable;");
    private static final Method getStateSet = Method.of(StateListDrawable.class, "getStateSet", "(I)[I");

    public int getLayoutDirection(Drawable drawable) {
        return Factory.getInstance().get().getLayoutDirection(drawable);
    }

    public int getStateCount(StateListDrawable stateListDrawable) {
        int i = 0;
        try {
            i = getStateCount.invokeInt(null, stateListDrawable, new Object[0]);
            return i;
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.StateListDrawable.getStateCount", e);
            return i;
        }
    }

    public Drawable getStateDrawable(StateListDrawable stateListDrawable, int i) {
        try {
            return (Drawable) getStateDrawable.invokeObject(null, stateListDrawable, Integer.valueOf(i));
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.StateListDrawable.getStateDrawable", e);
            return null;
        }
    }

    public int[] getStateSet(StateListDrawable stateListDrawable, int i) {
        try {
            return (int[]) getStateSet.invokeObject(null, stateListDrawable, Integer.valueOf(i));
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.graphics.drawable.StateListDrawable.getStateSet", e);
            return null;
        }
    }
}
