package com.miui.internal.variable.v21;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_View_ViewGroup.Extension;
import com.miui.internal.variable.api.v29.Android_View_ViewGroup.Interface;
import miui.reflect.Field;
import miui.reflect.Method;

public class Android_View_ViewGroup_class extends com.miui.internal.variable.v19.Android_View_ViewGroup_class implements Overridable {
    protected static int R_styleable_ViewGroup_touchscreenBlocksFocus;
    protected static final int R_styleable_ViewGroup_transitionGroup;
    protected static Method setTouchscreenBlocksFocus;
    protected static final Method setTransitionGroup;
    private Interface mImpl = new Interface() {
        public void addInArray(ViewGroup viewGroup, View view, int i) {
            Android_View_ViewGroup_class.this.handleAddInArray(0, viewGroup, view, i);
        }

        public void init(ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
            Android_View_ViewGroup_class.this.handle_init_(0, viewGroup, context, attributeSet, i, i2);
        }

        public void onChildVisibilityChanged(ViewGroup viewGroup, View view, int i, int i2) {
            Android_View_ViewGroup_class.this.handleOnChildVisibilityChanged(0, viewGroup, view, i, i2);
        }

        public void removeFromArray(ViewGroup viewGroup, int i) {
            Android_View_ViewGroup_class.this.handleRemoveFromArray(0, viewGroup, i);
        }

        public void removeFromArray(ViewGroup viewGroup, int i, int i2) {
            Android_View_ViewGroup_class.this.handleRemoveFromArray(0, viewGroup, i, i2);
        }

        public boolean resolveLayoutDirection(ViewGroup viewGroup) {
            return Android_View_ViewGroup_class.this.handleResolveLayoutDirection(0, viewGroup);
        }
    };
    private Interface mOriginal;

    static {
        String str = "I";
        R_styleable_ViewGroup_transitionGroup = Field.of(com.miui.internal.variable.v16.Android_View_ViewGroup_class.com_android_internal_R_styleable, "ViewGroup_transitionGroup", str).getInt(null);
        R_styleable_ViewGroup_touchscreenBlocksFocus = 0;
        String str2 = "(Z)V";
        setTransitionGroup = Method.of(ViewGroup.class, "setTransitionGroup", str2);
        setTouchscreenBlocksFocus = null;
        try {
            R_styleable_ViewGroup_touchscreenBlocksFocus = Field.of(com.miui.internal.variable.v16.Android_View_ViewGroup_class.com_android_internal_R_styleable, "ViewGroup_touchscreenBlocksFocus", str).getInt(null);
            setTouchscreenBlocksFocus = Method.of(ViewGroup.class, "setTouchscreenBlocksFocus", str2);
        } catch (Exception unused) {
        }
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    /* access modifiers changed from: protected */
    public void attachPrivateMethods() {
        String str = "(Landroid/content/Context;Landroid/util/AttributeSet;II)V";
        attachMethod("initFromAttributes", str);
        attachConstructor(str);
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    /* access modifiers changed from: protected */
    public void callOriginalAddInArray(long j, ViewGroup viewGroup, View view, int i) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.addInArray(viewGroup, view, i);
        } else {
            super.callOriginalAddInArray(j, viewGroup, view, i);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginalOnChildVisibilityChanged(long j, ViewGroup viewGroup, View view, int i, int i2) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.onChildVisibilityChanged(viewGroup, view, i, i2);
        } else {
            super.callOriginalOnChildVisibilityChanged(j, viewGroup, view, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginalRemoveFromArray(long j, ViewGroup viewGroup, int i) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.removeFromArray(viewGroup, i);
        } else {
            super.callOriginalRemoveFromArray(j, viewGroup, i);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginalRemoveFromArray(long j, ViewGroup viewGroup, int i, int i2) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.removeFromArray(viewGroup, i, i2);
        } else {
            super.callOriginalRemoveFromArray(j, viewGroup, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean callOriginalResolveLayoutDirection(long j, ViewGroup viewGroup) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.resolveLayoutDirection(viewGroup) : super.callOriginalResolveLayoutDirection(j, viewGroup);
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i) {
        if (this.mOriginal == null) {
            super.callOriginal_init_(j, viewGroup, context, attributeSet, i);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.init(viewGroup, context, attributeSet, i, i2);
        } else {
            original_init_(j, viewGroup, context, attributeSet, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handle_init_(0, null, null, null, 0, 0);
        handleInitFromAttributes(0, null, null, null, 0, 0);
        super.handle();
    }

    /* access modifiers changed from: protected */
    public void handleInitFromAttributes(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
        callOriginal_init_(j, viewGroup, context, attributeSet, i, i2);
        initFromAttributes(viewGroup, context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void originalInitFromAttributes(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v21.Android_View_ViewGroup_class.originalInitFromAttributes(long, ViewGroup, Context, AttributeSet, int, int)");
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, ViewGroup viewGroup, Context context, AttributeSet attributeSet, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v21.Android_View_ViewGroup_class.original_init_(long, ViewGroup, Context, AttributeSet, int, int)");
    }

    /* access modifiers changed from: protected */
    public void processAttribute(ViewGroup viewGroup, int i, TypedArray typedArray, Context context) {
        if (i == R_styleable_ViewGroup_transitionGroup) {
            setTransitionGroup.invoke(ViewGroup.class, viewGroup, Boolean.valueOf(typedArray.getBoolean(i, false)));
        }
        if (i == R_styleable_ViewGroup_touchscreenBlocksFocus) {
            setTouchscreenBlocksFocus.invoke(ViewGroup.class, viewGroup, Boolean.valueOf(typedArray.getBoolean(i, false)));
            return;
        }
        super.processAttribute(viewGroup, i, typedArray, context);
    }
}
