package com.miui.internal.variable.v21;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class Android_Content_Res_Resources_class extends com.miui.internal.variable.v16.Android_Content_Res_Resources_class {
    public void buildProxy() {
        attachMethod("loadDrawable", "(Landroid/util/TypedValue;ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleLoadDrawable(0, null, null, 0, null);
    }

    /* access modifiers changed from: protected */
    public Drawable handleLoadDrawable(long j, Resources resources, TypedValue typedValue, int i, Theme theme) {
        Drawable originalLoadDrawable = originalLoadDrawable(j, resources, typedValue, i, theme);
        if (originalLoadDrawable != null && originalLoadDrawable.isStateful()) {
            com.miui.internal.variable.Android_Content_Res_Resources_class.DrawableClass.setId(originalLoadDrawable, i);
        }
        return originalLoadDrawable;
    }

    /* access modifiers changed from: protected */
    public Drawable originalLoadDrawable(long j, Resources resources, TypedValue typedValue, int i, Theme theme) {
        throw new IllegalStateException("com.miui.internal.variable.v21.Android_Content_Res_Resources_class.originalLoadDrawable(long, Resources, TypedValue, int, Resources.Theme)");
    }
}
