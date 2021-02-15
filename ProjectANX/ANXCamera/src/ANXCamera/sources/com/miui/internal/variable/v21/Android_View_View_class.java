package com.miui.internal.variable.v21;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.miui.internal.util.TaggingDrawableHelper;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_View_View.Extension;
import com.miui.internal.variable.api.v29.Android_View_View.Interface;

public class Android_View_View_class extends com.miui.internal.variable.v19.Android_View_View_class implements Overridable {
    private Interface mImpl = new Interface() {
        public void init(View view, Context context, AttributeSet attributeSet, int i, int i2) {
            Android_View_View_class.this.handle_init_(0, view, context, attributeSet, i, i2);
        }

        public int[] onCreateDrawableState(View view, int i) {
            return Android_View_View_class.this.handleOnCreateDrawableState(0, view, i);
        }

        public void refreshDrawableState(View view) {
            Android_View_View_class.this.handleRefreshDrawableState(0, view);
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
        super.buildProxy();
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;II)V");
    }

    /* access modifiers changed from: protected */
    public int[] callOriginalOnCreateDrawableState(long j, View view, int i) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.onCreateDrawableState(view, i) : super.callOriginalOnCreateDrawableState(j, view, i);
    }

    /* access modifiers changed from: protected */
    public void callOriginalRefreshDrawableState(long j, View view) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.refreshDrawableState(view);
        } else {
            super.callOriginalRefreshDrawableState(j, view);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, View view, Context context, AttributeSet attributeSet, int i, int i2) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.init(view, context, attributeSet, i, i2);
        } else {
            original_init_(j, view, context, attributeSet, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handle_init_(0, null, null, null, 0, 0);
        super.handle();
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, View view, Context context, AttributeSet attributeSet, int i, int i2) {
        callOriginal_init_(j, view, context, attributeSet, i, i2);
        TaggingDrawableHelper.initViewSequenceStates(view, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, View view, Context context, AttributeSet attributeSet, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v21.Android_View_View_class.original_init_(long, View, Context, AttributeSet, int, int)");
    }
}
