package com.miui.internal.variable.v21;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_ListView.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_ListView.Interface;

public class Android_Widget_ListView_class extends com.miui.internal.variable.v16.Android_Widget_ListView_class implements Overridable {
    private Interface mImpl = new Interface() {
        public void fillGap(ListView listView, boolean z) {
            Android_Widget_ListView_class.this.handleFillGap(0, listView, z);
        }

        public void init(ListView listView, Context context, AttributeSet attributeSet, int i, int i2) {
            Android_Widget_ListView_class.this.handle_init_(0, listView, context, attributeSet, i, i2);
        }

        public void layoutChildren(ListView listView) {
            Android_Widget_ListView_class.this.handleLayoutChildren(0, listView);
        }
    };
    private Interface mOriginal;

    public Interface asInterface() {
        return this.mImpl;
    }

    /* access modifiers changed from: protected */
    public void attachSpecialMethod() {
        attachConstructor("(Landroid/content/Context;Landroid/util/AttributeSet;II)V");
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    /* access modifiers changed from: protected */
    public void callOriginalFillGap(long j, ListView listView, boolean z) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.fillGap(listView, z);
        } else {
            super.callOriginalFillGap(j, listView, z);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginalLayoutChildren(long j, ListView listView) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.layoutChildren(listView);
        } else {
            super.callOriginalLayoutChildren(j, listView);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginal_init_(long j, ListView listView, Context context, AttributeSet attributeSet, int i, int i2) {
        if (this.mOriginal == null) {
            original_init_(j, listView, context, attributeSet, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handle_init_(0, null, null, null, 0);
        super.handle();
    }

    /* access modifiers changed from: protected */
    public void handle_init_(long j, ListView listView, Context context, AttributeSet attributeSet, int i, int i2) {
        callOriginal_init_(j, listView, context, attributeSet, i, i2);
        doInit(listView, context);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void original_init_(long j, ListView listView, Context context, AttributeSet attributeSet, int i, int i2) {
        throw new IllegalStateException("com.miui.internal.variable.v21.Android_Widget_ListView_class.original_init_(long, ListView, Context, AttributeSet, int, int)");
    }
}
