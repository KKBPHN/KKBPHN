package com.miui.internal.variable.v16;

import android.widget.GridView;
import com.miui.internal.util.TaggingDrawableHelper;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Widget_GridView.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_GridView.Interface;

public class Android_Widget_GridView_class extends com.miui.internal.variable.Android_Widget_GridView_class implements Overridable {
    private Interface mImpl = new Interface() {
        public void fillGap(GridView gridView, boolean z) {
            Android_Widget_GridView_class.this.handleFillGap(0, gridView, z);
        }

        public void layoutChildren(GridView gridView) {
            Android_Widget_GridView_class.this.handleLayoutChildren(0, gridView);
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
        attachMethod("layoutChildren", "()V");
        attachMethod("fillGap", "(Z)V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalFillGap(long j, GridView gridView, boolean z) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.fillGap(gridView, z);
        } else {
            originalFillGap(j, gridView, z);
        }
    }

    /* access modifiers changed from: protected */
    public void callOriginalLayoutChildren(long j, GridView gridView) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.layoutChildren(gridView);
        } else {
            originalLayoutChildren(j, gridView);
        }
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleLayoutChildren(0, null);
        handleFillGap(0, null, false);
    }

    /* access modifiers changed from: protected */
    public void handleFillGap(long j, GridView gridView, boolean z) {
        callOriginalFillGap(j, gridView, z);
        TaggingDrawableHelper.tagChildSequenceState(gridView);
    }

    /* access modifiers changed from: protected */
    public void handleLayoutChildren(long j, GridView gridView) {
        callOriginalLayoutChildren(j, gridView);
        TaggingDrawableHelper.tagChildSequenceState(gridView);
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }

    /* access modifiers changed from: protected */
    public void originalFillGap(long j, GridView gridView, boolean z) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_GridView_class.originalFillGap(long, GridView, boolean)");
    }

    /* access modifiers changed from: protected */
    public void originalLayoutChildren(long j, GridView gridView) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Widget_GridView_class.originalLayoutChildren(long, GridView)");
    }
}
