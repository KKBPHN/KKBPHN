package com.miui.internal.variable.v26;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.ResourcesImpl;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class;
import com.miui.internal.variable.Android_Graphics_Drawable_Drawable_class.Factory;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Content_Res_ResourcesImpl.Extension;
import com.miui.internal.variable.api.v29.Android_Content_Res_ResourcesImpl.Interface;

public class Android_Content_Res_ResourcesImpl_class extends com.miui.internal.variable.v24.Android_Content_Res_ResourcesImpl_class implements Overridable {
    private static final Android_Graphics_Drawable_Drawable_class DrawableClass = Factory.getInstance().get();
    private Interface mImpl = new Interface() {
        public Drawable loadDrawable(ResourcesImpl resourcesImpl, Resources resources, TypedValue typedValue, int i, int i2, Theme theme) {
            return Android_Content_Res_ResourcesImpl_class.this.handleLoadDrawable(0, resourcesImpl, resources, typedValue, i, i2, theme);
        }
    };
    private Interface mOriginal;

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    public void buildProxy() {
        attachMethod("loadDrawable", "(Landroid/content/res/Resources;Landroid/util/TypedValue;IILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;");
    }

    /* access modifiers changed from: protected */
    public Drawable callOriginalLoadDrawable(long j, ResourcesImpl resourcesImpl, Resources resources, TypedValue typedValue, int i, int i2, Theme theme) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.loadDrawable(resourcesImpl, resources, typedValue, i, i2, theme) : originalLoadDrawable(j, resourcesImpl, resources, typedValue, i, i2, theme);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleLoadDrawable(0, null, null, null, 0, 0, null);
    }

    /* access modifiers changed from: protected */
    public Drawable handleLoadDrawable(long j, ResourcesImpl resourcesImpl, Resources resources, TypedValue typedValue, int i, int i2, Theme theme) {
        Drawable callOriginalLoadDrawable = callOriginalLoadDrawable(j, resourcesImpl, resources, typedValue, i, i2, theme);
        if (callOriginalLoadDrawable != null && callOriginalLoadDrawable.isStateful()) {
            DrawableClass.setId(callOriginalLoadDrawable, i);
        }
        return callOriginalLoadDrawable;
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public Drawable originalLoadDrawable(long j, ResourcesImpl resourcesImpl, Resources resources, TypedValue typedValue, int i, int i2, Theme theme) {
        throw new IllegalStateException("com.miui.internal.variable.v26.Android_Content_Res_ResourcesImpl_class.originalLoadDrawable(long, ResourcesImpl, Resources, TypedValue, int, int, Resources.Theme)");
    }
}
