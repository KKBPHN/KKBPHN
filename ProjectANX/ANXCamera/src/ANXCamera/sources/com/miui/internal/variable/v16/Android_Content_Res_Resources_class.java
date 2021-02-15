package com.miui.internal.variable.v16;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Field;

public class Android_Content_Res_Resources_class extends com.miui.internal.variable.Android_Content_Res_Resources_class {
    public void buildProxy() {
        attachMethod("loadDrawable", "(Landroid/util/TypedValue;I)Landroid/graphics/drawable/Drawable;");
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleLoadDrawable(0, null, null, 0);
    }

    /* access modifiers changed from: protected */
    public Drawable handleLoadDrawable(long j, Resources resources, TypedValue typedValue, int i) {
        Drawable originalLoadDrawable = originalLoadDrawable(j, resources, typedValue, i);
        if (originalLoadDrawable != null && originalLoadDrawable.isStateful()) {
            com.miui.internal.variable.Android_Content_Res_Resources_class.DrawableClass.setId(originalLoadDrawable, i);
        }
        return originalLoadDrawable;
    }

    /* access modifiers changed from: protected */
    public Drawable originalLoadDrawable(long j, Resources resources, TypedValue typedValue, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Content_Res_Resources_class.originalLoadDrawable(long, Resources, TypedValue, int)");
    }

    public void setAssetManager(Resources resources, AssetManager assetManager) {
        String str = "mAssets";
        try {
            if (com.miui.internal.variable.Android_Content_Res_Resources_class.mAssets == null) {
                com.miui.internal.variable.Android_Content_Res_Resources_class.mAssets = Field.of(Resources.class, str, "Landroid/content/res/AssetManager;");
            }
            com.miui.internal.variable.Android_Content_Res_Resources_class.mAssets.set((Object) resources, (Object) assetManager);
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow(str, e);
        }
    }
}
